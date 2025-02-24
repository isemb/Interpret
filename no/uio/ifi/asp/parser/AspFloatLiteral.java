package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspFloatLiteral extends AspAtom {

    TokenKind kind;
    double floatLiteral;
    //boolean value;


    AspFloatLiteral(int n) {
        super(n);
    }


    static AspFloatLiteral parse(Scanner s) {
        enterParser("float literal");
        AspFloatLiteral fl = new AspFloatLiteral(s.curLineNum());
        
        if(s.curToken().kind == floatToken) {
            fl.kind = floatToken;
            fl.floatLiteral = s.curToken().floatLit;
            skip(s, floatToken);
        } else {
            parserError("Expected a float literal but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("float literal");
        return fl;
    }


    @Override
    void prettyPrint() {
        prettyWrite(Double.toString(floatLiteral));
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeFloatValue(floatLiteral);
    }
}
