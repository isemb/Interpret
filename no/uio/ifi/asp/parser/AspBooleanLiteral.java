package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspBooleanLiteral extends AspAtom {

    TokenKind boolKind;
    boolean value;


    AspBooleanLiteral(int n) {
        super(n);
    }


    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");
        AspBooleanLiteral bl = new AspBooleanLiteral(s.curLineNum());
        
        switch(s.curToken().kind) {
            case trueToken:
                bl.boolKind = trueToken;
                bl.value = true; 
                skip(s, trueToken);
                break;
            case falseToken:
                bl.boolKind = falseToken;
                bl.value = false;
                skip(s, falseToken);
                break;
            default:
                parserError("Expected a boolean literal but found a " +
                s.curToken().kind + "!", s.curLineNum());
                
        }
        leaveParser("boolean literal");
        return bl;
    }


    @Override
    void prettyPrint() {
        prettyWrite(boolKind.toString());
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(value);
    }
}
