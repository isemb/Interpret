package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;


abstract class AspPrimarySuffix extends AspSyntax {

    AspPrimarySuffix(int n) {
        super(n);
    }

    
    static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");
        AspPrimarySuffix ps = null;

        switch (s.curToken().kind) {
            case leftParToken:
                ps = AspArguments.parse(s);
                break;
            case leftBracketToken:
                ps = AspSubscription.parse(s);
                break;
            default:
                parserError("Expected a primary suffix but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        
        leaveParser("primary suffix");
        return ps;
    }
}
