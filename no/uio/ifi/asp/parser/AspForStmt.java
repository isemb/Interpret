package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

import java.util.ArrayList;

import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspForStmt extends AspCompoundStmt {

    AspName name;
    AspExpr expr;
    AspSuite suite;

    
    AspForStmt(int n) {
        super(n);
    }

    
    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");
        AspForStmt fs = new AspForStmt(s.curLineNum());
        skip(s, forToken);
        fs.name = AspName.parse(s);
        skip(s, inToken);
        fs.expr = AspExpr.parse(s);
        skip(s, colonToken);
        fs.suite = AspSuite.parse(s);
        leaveParser("for stmt");
        return fs;
    }


    @Override
    void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        expr.prettyPrint();
        prettyWrite(": ");
        suite.prettyPrint();
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        if(v instanceof RuntimeListValue){
            ArrayList<RuntimeValue> exprList = new ArrayList<>();
            exprList = ((RuntimeListValue) v).getList("exprList", this);
            int iteration = 0;
            for (RuntimeValue exprs: exprList) {
                iteration++;
                curScope.assign(name.name, exprs);
                trace("for #" + iteration + ": " + name.name + " = " + exprs);
                suite.eval(curScope);
            }
        } else {
            RuntimeValue.runtimeError("For loop range is not a list!", lineNum);
        }
        return null;
    }
}
