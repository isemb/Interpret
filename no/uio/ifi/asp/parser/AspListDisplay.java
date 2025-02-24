package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


class AspListDisplay extends AspAtom {

    
    ArrayList<AspExpr> exprList = new ArrayList<>();


    AspListDisplay(int n) {
        super(n);
    }


    static AspListDisplay parse(Scanner s) {
        enterParser("list display");
        AspListDisplay ls = new AspListDisplay(s.curLineNum());

        skip(s, leftBracketToken);

        if (s.curToken().kind != rightBracketToken) {
            while (true) {
                ls.exprList.add(AspExpr.parse(s));

                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightBracketToken);
        
        leaveParser("list display");
        return ls;
    }


    @Override
    void prettyPrint() {
        prettyWrite("[");

        if (!exprList.isEmpty()) {
            int nPrinted = 0;
            
            for (AspExpr ae: exprList) {
                if (nPrinted > 0) {
                    prettyWrite(", ");
                }
                ae.prettyPrint(); ++nPrinted;
            }
        }
        prettyWrite("]");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> runtimeList= new ArrayList<RuntimeValue>();
        if(!exprList.isEmpty()){
            RuntimeValue v;
            for (AspExpr aspExpr : exprList) {
                v = aspExpr.eval(curScope);
                runtimeList.add(v);
            }
        }
        return new RuntimeListValue(runtimeList);
    }
}
