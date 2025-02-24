package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspNoneLiteral extends AspAtom {

    TokenKind noneKind;

    
    AspNoneLiteral(int n) {
        super(n);
    }


    static AspNoneLiteral parse(Scanner s) {
        enterParser("none literal");
        AspNoneLiteral nl = new AspNoneLiteral(s.curLineNum());
        if((s.curToken().kind) == noneToken) {
            nl.noneKind = noneToken;
            skip(s, noneToken);
        } else {
            parserError("Expected a none literal but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("none literal");
        return nl;
    }


    @Override
    void prettyPrint() {
        prettyWrite(" None ");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeNoneValue();
    }
}
