package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspTermOpr extends AspSyntax {

    TokenKind oprKind;


    AspTermOpr(int n) {
        super(n);
    }


    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");
        AspTermOpr to = new AspTermOpr(s.curLineNum());

        switch(s.curToken().kind) {
            case plusToken:
                to.oprKind = plusToken; 
                skip(s, plusToken);
                break;
            case minusToken:
                to.oprKind = minusToken;
                skip(s, minusToken);
                break;
            default:
                parserError("Expected a term opr but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("term opr");
        return to;
    }


    @Override
    void prettyPrint() {
        prettyWrite(" " + oprKind.toString() + " "); 
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}
