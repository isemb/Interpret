package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspStringLiteral extends AspAtom {

    TokenKind kind;
    String stringLiteral;


    AspStringLiteral(int n) {
        super(n);
    }


    static AspStringLiteral parse(Scanner s) {
        enterParser("string literal");
        AspStringLiteral sl = new AspStringLiteral(s.curLineNum());
        
        if(s.curToken().kind == stringToken) {
            sl.kind = stringToken;
            sl.stringLiteral = s.curToken().stringLit;
            skip(s, stringToken);
        } else {
            parserError("Expected a string literal but found a " +
            s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("string literal");
        return sl;
    }


    @Override
    void prettyPrint() {
        if (stringLiteral.contains("\"")) {
            prettyWrite("\'" + stringLiteral + "\'"); 
        } 
        else {
            prettyWrite("\"" + stringLiteral + "\""); 
        }
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(stringLiteral);
    }
}
