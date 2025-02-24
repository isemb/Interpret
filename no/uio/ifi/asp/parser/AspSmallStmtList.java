package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspSmallStmtList extends AspStmt {

    ArrayList<AspSmallStmt> smallStmts = new ArrayList<>();
    Boolean semiColonBeforeNewline = false;

    
    AspSmallStmtList(int n) {
	    super(n);
    }


    static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");
        AspSmallStmtList ssl = new AspSmallStmtList(s.curLineNum());

        while (true) {
            ssl.smallStmts.add(AspSmallStmt.parse(s));

            if (s.curToken().kind == newLineToken) {
                break;
            }
            skip(s, semicolonToken);

            if (s.curToken().kind == newLineToken) {
                ssl.semiColonBeforeNewline = true;
                break;
            }
        } 
        skip(s, newLineToken);

        leaveParser("small stmt list");
        return ssl;
    }


    @Override
    public void prettyPrint() {
        int nPrinted = 0;

        for (AspSmallStmt ss: smallStmts) {
            if (nPrinted > 0) {
                prettyWrite(semicolonToken.toString() + " ");
            }
            ss.prettyPrint(); ++nPrinted;
        }
        // We don't need to print the last semicolon on the line
        prettyWriteLn();
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        for (AspSmallStmt s: smallStmts) {
            v = s.eval(curScope);
        }
        return v;
    }
}
