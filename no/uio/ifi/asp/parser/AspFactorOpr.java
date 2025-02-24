package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspFactorOpr extends AspSyntax {

    TokenKind operatorKind;


    AspFactorOpr(int n) {
        super(n);
    }

    
    static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");
        AspFactorOpr fo = new AspFactorOpr(s.curLineNum());

        switch(s.curToken().kind) {
            case astToken:
                fo.operatorKind = astToken; 
                skip(s, astToken);
                break;
            case slashToken:
                fo.operatorKind = slashToken;
                skip(s, slashToken);
                break;
            case percentToken:
                fo.operatorKind = percentToken;
                skip(s, percentToken);
                break;
            case doubleSlashToken:
                fo.operatorKind = doubleSlashToken;
                skip(s, doubleSlashToken);
                break;
            default:
                parserError("Expected a factor opr an but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("factor opr");
        return fo;
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
