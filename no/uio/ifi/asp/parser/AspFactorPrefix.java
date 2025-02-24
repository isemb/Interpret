package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspFactorPrefix extends AspSyntax {

    TokenKind factorPrefixKind;


    AspFactorPrefix(int n) {
        super(n);
    }


    static AspFactorPrefix parse(Scanner s) {
        enterParser("factor prefix");

        AspFactorPrefix fp = new AspFactorPrefix(s.curLineNum());

        switch(s.curToken().kind) {
            case plusToken:
                fp.factorPrefixKind = plusToken; 
                skip(s, plusToken);
                break;
            case minusToken:
                fp.factorPrefixKind = minusToken;
                skip(s, minusToken);
                break;
            default:
                parserError("Expected a factor prefix but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("factor prefix");
        return fp;
    }


    @Override
    void prettyPrint() {
        prettyWrite(factorPrefixKind.toString() + " ");
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        throw new UnsupportedOperationException("Unimplemented method 'eval'");
    }
}

