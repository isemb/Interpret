package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;


abstract class AspSmallStmt extends AspSyntax {

    AspSmallStmt(int n) {
        super(n);
    }

    
    static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");
        AspSmallStmt ss = null;

        switch (s.curToken().kind) {
            case globalToken:
                ss = AspGlobalStmt.parse(s); 
                break;
            case passToken:
                ss = AspPassStmt.parse(s); 
                break;
            case returnToken:
                ss = AspReturnStmt.parse(s); 
                break;
            case nameToken:
                // Conditional/ternary operator for true/false
                ss = s.anyEqualToken() ? AspAssignment.parse(s) : AspExprStmt.parse(s); 
                break;
            case notToken:
            case plusToken:
            case minusToken: 
            case integerToken:
            case floatToken:
            case stringToken:
            case trueToken:
            case falseToken:
            case noneToken:
            case leftParToken:
            case leftBracketToken:
            case leftBraceToken:
                ss =  AspExprStmt.parse(s); 
                break;
            default:
                parserError("Expected a small stmt but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("small stmt");
        return ss;
    }
}
