package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


class AspExprStmt extends AspSmallStmt {

    AspExpr expr;


    AspExprStmt(int n) {
        super(n);
    }


    static AspExprStmt parse(Scanner s){
        enterParser("expr stmt");
        AspExprStmt es = new AspExprStmt(s.curLineNum());

        es.expr = AspExpr.parse(s);

        leaveParser("expr stmt");
        return es;
        
    }

    
    @Override
    void prettyPrint() {
        expr.prettyPrint();
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        if (v instanceof RuntimeStringValue) {
            trace("Expression statement produced "+ v);
            return v;
        }
        trace("Expression statement produced "+ v);
        return v;
    }
}
