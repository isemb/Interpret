package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.parser.*;
import no.uio.ifi.asp.runtime.*;

import java.util.ArrayList;

public class RuntimeFunc extends RuntimeValue{
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    @Override
    String typeName() {
        return "def";
    }

    public RuntimeFunc(String funcName) {
        name = funcName;
    }

    public RuntimeFunc(AspFuncDef fd, RuntimeScope rs, String funcName) {
        def = fd;
        defScope = rs;
        name = funcName;
    }

    public String toString() {
        return name;
    }


    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) {
        ArrayList<AspName> formalParams = def.getAspNames();
        if (!(actualParams.size() == formalParams.size())) {
            runtimeError("Actual params not the same size as formal params!", where);
        }

        RuntimeScope funcScope = new RuntimeScope(defScope);

        if (!formalParams.isEmpty()) {
            for (int i = 0; i < actualParams.size(); i++) {
                funcScope.assign(formalParams.get(i).getName(), actualParams.get(i));
            }
        }

        AspSuite suite = def.getSuite();

        // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-45-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
        // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-45-utdeling.pdf, slide 32.
        try {
            suite.eval(funcScope);
        } catch (RuntimeReturnValue rrv) {
            return rrv.value;
        }
        return new RuntimeNoneValue();
    }
}
