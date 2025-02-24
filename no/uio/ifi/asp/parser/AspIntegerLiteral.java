package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspIntegerLiteral extends AspAtom {

    TokenKind kind;
    long integerLiteral;


    AspIntegerLiteral(int n) {
        super(n);
    }


    static AspIntegerLiteral parse(Scanner s) {
        enterParser("integer literal");
        AspIntegerLiteral il = new AspIntegerLiteral(s.curLineNum());
        
        if(s.curToken().kind == integerToken) {
            il.kind = integerToken;
            il.integerLiteral = s.curToken().integerLit;
            skip(s, integerToken);
        } else {
            parserError("Expected an integer literal but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("integer literal");
        return il;
    }


    @Override
    void prettyPrint() {
        prettyWrite(Long.toString(integerLiteral));
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeIntValue(integerLiteral);
    }
}
