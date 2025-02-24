package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspInnerExpr extends AspAtom {

    AspExpr aspExpr;

    
    AspInnerExpr(int n) {
        super(n);
    }


    static AspInnerExpr parse(Scanner s) {
        enterParser("inner expr");
        AspInnerExpr ie = new AspInnerExpr(s.curLineNum());
        
        skip(s, leftParToken);
        ie.aspExpr = AspExpr.parse(s);
        skip(s, rightParToken);
        
        leaveParser("inner expr");
        return ie;
    }


    @Override
    void prettyPrint() {
        prettyWrite(leftParToken.toString());
        aspExpr.prettyPrint();
        prettyWrite(rightParToken.toString());
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return aspExpr.eval(curScope);
    }
}
