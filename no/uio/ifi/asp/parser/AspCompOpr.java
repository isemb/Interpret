package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspCompOpr extends AspSyntax {

    TokenKind operatorKind;


    AspCompOpr(int n) {
        super(n);
    }


    static AspCompOpr parse (Scanner s) {
        enterParser("comp opr");
        AspCompOpr co = new AspCompOpr(s.curLineNum());

        switch(s.curToken().kind) {
            case lessToken:
                co.operatorKind = lessToken; 
                skip(s, lessToken);
                break;
            case greaterToken:
                co.operatorKind = greaterToken;
                skip(s, greaterToken);
                break;
            case doubleEqualToken:
                co.operatorKind = doubleEqualToken;
                skip(s, doubleEqualToken);
                break;
            case greaterEqualToken:
                co.operatorKind = greaterEqualToken;
                skip(s, greaterEqualToken);
                break;
            case lessEqualToken:
                co.operatorKind = lessEqualToken;
                skip(s, lessEqualToken);
                break;
            case notEqualToken:
                co.operatorKind = notEqualToken;
                skip(s, notEqualToken);
                break;
            default:
                parserError("Expected a comp opr but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("comp opr");
        return co;
    }


    @Override
    void prettyPrint() {
        prettyWrite(" " + operatorKind.toString() + " ");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}
