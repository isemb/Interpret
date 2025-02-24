package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


class AspArguments extends AspPrimarySuffix {

    ArrayList<AspExpr> exprs = new ArrayList<>();


    AspArguments(int n) {
        super(n);
    }


    static AspArguments parse(Scanner s) {
        enterParser("arguments");
        AspArguments a = new AspArguments(s.curLineNum());

        skip(s, leftParToken);
        if (s.curToken().kind != rightParToken) {
            while (true) {
                a.exprs.add(AspExpr.parse(s));
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);
        leaveParser("arguments");
        return a;
    }


    @Override
    void prettyPrint() {
        prettyWrite("(");
        if(!exprs.isEmpty()){
            int nPrinted = 0;
            for(AspExpr ae: exprs){
                if(nPrinted > 0) {
                    prettyWrite(", ");
                }
                ae.prettyPrint(); ++nPrinted;
            }
        }
        prettyWrite(")");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        ArrayList<RuntimeValue> exprList = new ArrayList<>();
        if(!exprs.isEmpty()){
            for (AspExpr aspExpr : exprs) {
                v = aspExpr.eval(curScope);
                exprList.add(v);
            }
        }
        //return runtimeListvalue
        RuntimeListValue listV = new RuntimeListValue(exprList);
        return listV;
    }
}
