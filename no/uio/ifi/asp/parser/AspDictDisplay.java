package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;
import java.util.HashMap;


class AspDictDisplay extends AspAtom {

    ArrayList<AspStringLiteral> stringLiterals = new ArrayList<>();
    ArrayList<AspExpr> exprs = new ArrayList<>();


    AspDictDisplay(int n) {
        super(n);
    }


    static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");
        AspDictDisplay dd = new AspDictDisplay(s.curLineNum());

        skip(s, leftBraceToken);
        if (s.curToken().kind != rightBraceToken) {
            while (true) {
                test(s, stringToken);
                dd.stringLiterals.add(AspStringLiteral.parse(s));
                skip(s, colonToken);
                dd.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightBraceToken);

        leaveParser("dict display");
        return dd;
    }


    @Override
    void prettyPrint() {
        prettyWrite("{");
        if(!stringLiterals.isEmpty()) {
            int nPrinted = 0;
            for (AspStringLiteral sl : stringLiterals) {
                if (nPrinted > 0) {
                    prettyWrite(", ");
                }
                sl.prettyPrint();
                prettyWrite(":");
                exprs.get(nPrinted).prettyPrint(); ++nPrinted;
            }
        }
        prettyWrite("}");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        HashMap<RuntimeStringValue, RuntimeValue> dictionary = new HashMap<RuntimeStringValue, RuntimeValue>();
        if(!stringLiterals.isEmpty()){
            RuntimeValue s;
            RuntimeValue e;
            //RuntimeValue v = stringLiterals.get(0).eval(curScope);
            for (int i = 0; i < stringLiterals.size(); i++) {
                s = stringLiterals.get(i).eval(curScope);
                e = exprs.get(i).eval(curScope);
                dictionary.put((RuntimeStringValue)s, e);
            }
        }
        return new RuntimeDictValue(dictionary);
    }
}
