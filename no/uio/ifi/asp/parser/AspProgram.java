// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspProgram extends AspSyntax {
    
    ArrayList<AspStmt> stmts = new ArrayList<>();


    AspProgram(int n) {
	    super(n);
    }


    public static AspProgram parse(Scanner s) {
	    enterParser("program");
        AspProgram ap = new AspProgram(s.curLineNum());
	    while (s.curToken().kind != eofToken) {
	        ap.stmts.add(AspStmt.parse(s));
	    }
	    leaveParser("program");
	    return ap;
    }


    @Override
    public void prettyPrint() {
        if (!stmts.isEmpty()) {
            for (AspStmt s: stmts) {
                s.prettyPrint();
            }
        }
    }


    // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-45-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
    // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-45-utdeling.pdf, slide 33.
    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = new RuntimeNoneValue();
        
        if (stmts.size() != 0) {
            for (int i = 0; i < stmts.size(); i ++) {
                try {
                    v = stmts.get(i).eval(curScope);
                } catch (RuntimeReturnValue rrv) {
                    RuntimeValue.runtimeError("Return statement outside function!", rrv.lineNum);
                }
            }
        }
        return v;
    }
}
