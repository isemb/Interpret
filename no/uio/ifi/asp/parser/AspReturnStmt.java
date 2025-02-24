package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspReturnStmt extends AspSmallStmt {

    AspExpr expr;


    AspReturnStmt(int n) {
        super(n);
    }


    static AspReturnStmt parse(Scanner s){
        enterParser("return stmt");
        AspReturnStmt rs = new AspReturnStmt(s.curLineNum());
        
        skip(s, returnToken);
        rs.expr = AspExpr.parse(s);
        
        leaveParser("return stmt");
        return rs;
    }

    
    @Override
    void prettyPrint() {
        prettyWrite(returnToken.toString() + " ");
        expr.prettyPrint();
    }


    // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-45-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
    // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-45-utdeling.pdf, slide 32.
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        System.out.println("AspReturnStmt.eval kalt");
        RuntimeValue v = expr.eval(curScope);
        trace("return " + v.showInfo());
        throw new RuntimeReturnValue(v, lineNum);
    }
}
