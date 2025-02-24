package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspSubscription extends AspPrimarySuffix {

    AspExpr aspExpr;


    AspSubscription(int n) {
        super(n);
    }


    static AspSubscription parse(Scanner s) {
        enterParser("subscription");
        AspSubscription su = new AspSubscription(s.curLineNum());

        skip(s, leftBracketToken);
        su.aspExpr = AspExpr.parse(s);
        skip(s, rightBracketToken);
        
        leaveParser("subscription");
        return su;
    }


    @Override
    void prettyPrint() {
       prettyWrite("[");
       aspExpr.prettyPrint();
       prettyWrite("]");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return  aspExpr.eval(curScope);
    }
}
