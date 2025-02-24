// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.NoSuchElementException;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;


// Source: Langmyhr, D., & Runde, R. K. (2023). "uke-45-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
// URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-45-utdeling.pdf, slide 38.
public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }
        });

        // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-45-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
        // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-45-utdeling.pdf, slide 39.
        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0) {
                        System.out.print(" ");
                    }
                    if (!(actualParams.get(i) instanceof RuntimeListValue) && !(actualParams.get(i) instanceof RuntimeDictValue)) {
                        System.out.print(actualParams.get(i).toString().replace("'", ""));
                    }
                    if (actualParams.get(i) instanceof RuntimeListValue || actualParams.get(i) instanceof RuntimeDictValue) {
                        System.out.print(actualParams.get(i).toString());
                    }
                }
                System.out.println();
                return new RuntimeNoneValue();
            }
        });

        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                long intValue = actualParams.get(0).getIntValue("int", where);
                return new RuntimeIntValue(intValue);
            }
        });

        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.print(actualParams.get(0).toString().replace("'", ""));
                String userInput = keyboard.nextLine();
                return new RuntimeStringValue(userInput);
            }
        });

        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "str", where);
                String stringValue = actualParams.get(0).getStringValue("str", where);
                return new RuntimeStringValue(stringValue);
            }
        });

        // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                double floatValue = actualParams.get(0).getFloatValue("float", where);
                return new RuntimeFloatValue(floatValue);
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                ArrayList<RuntimeValue> rangeList = new ArrayList<RuntimeValue>();
                RuntimeValue i = actualParams.get(0);
                RuntimeValue next = actualParams.get(1);
                int y = (int)next.getIntValue("int", where);
                for (int x= (int)i.getIntValue("int", where); x < y; x++) {
                    RuntimeValue iv = new RuntimeIntValue((long)x);
                    rangeList.add(iv);
                }
                return  new RuntimeListValue(rangeList);
            }
        });
    }

    
    private void checkNumParams(ArrayList<RuntimeValue> actArgs, int nCorrect, String id, AspSyntax where) {
	    if (actArgs.size() != nCorrect) {
	        RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
        }
    }
}
