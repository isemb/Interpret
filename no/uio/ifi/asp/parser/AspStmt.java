// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


abstract class AspStmt extends AspSyntax {

    AspStmt(int n) {
	    super(n);
    }


    static AspStmt parse(Scanner s) {
        enterParser("stmt");
        AspStmt stmt = null;
        
        if (s.curToken().kind == ifToken ||
            s.curToken().kind == forToken || 
            s.curToken().kind == whileToken ||
            s.curToken().kind == defToken) {

            AspCompoundStmt cmpStmt = AspCompoundStmt.parse(s); 
            stmt = cmpStmt;
        } else { 
            AspSmallStmtList stmtList = AspSmallStmtList.parse(s);
            stmt = stmtList;
        }
        leaveParser("stmt");
        return stmt;
    }
}
