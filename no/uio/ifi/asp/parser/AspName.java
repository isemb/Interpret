package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


public class AspName extends AspAtom {

    TokenKind kind;
    String name;


    AspName(int n) {
        super(n);
    }

    public String getName() {
        return name;
    }


    static AspName parse(Scanner s) {
        enterParser("name");
        AspName n = new AspName(s.curLineNum());
        
        if(s.curToken().kind == nameToken) {
            n.kind = nameToken;
            n.name = s.curToken().name;
            skip(s, nameToken);
        } else {
            parserError("Expected a name token but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("name");
        return n;
    }


    @Override
    void prettyPrint() {
        prettyWrite(name);
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name,this);
    }
}
