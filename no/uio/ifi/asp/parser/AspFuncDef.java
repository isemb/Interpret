package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


import java.util.ArrayList;


public class AspFuncDef extends AspCompoundStmt {

    AspName name;
    ArrayList<AspName> nameList = new ArrayList<>();
    AspSuite suite;


    AspFuncDef(int n) {
        super(n);
    }


    public AspSuite getSuite() {
        return suite;
    }


    public ArrayList<AspName> getAspNames() {return nameList;
    }


    static AspFuncDef parse(Scanner s) {
        enterParser("func def");
        AspFuncDef fd = new AspFuncDef(s.curLineNum());
        
        skip(s, defToken);
        fd.name = AspName.parse(s);
        skip(s, leftParToken);
        if (s.curToken().kind != rightParToken) {
            while (true) {
                fd.nameList.add(AspName.parse(s));
                if (s.curToken().kind != commaToken) {
                    break;
                }
                skip(s, commaToken);
            }
        }
        skip(s, rightParToken);
        skip(s, colonToken);
        fd.suite = AspSuite.parse(s);

        leaveParser("func def");
        return fd;
    }


    @Override
    void prettyPrint() {
        prettyWrite("def ");
        name.prettyPrint();
        prettyWrite(" (");
        if(!nameList.isEmpty()) {
            int nPrinted = 0;
            for (AspName n: nameList) {
                if (nPrinted > 0) {
                    prettyWrite(", ");
                }   
                n.prettyPrint(); ++nPrinted;
            }
        }
        prettyWrite(")");
        prettyWrite(": ");
        suite.prettyPrint();
        prettyWriteLn();
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeFunc f = new RuntimeFunc(this, curScope, name.name);
        trace("def " + name.name);
        curScope.assign(name.name, f);
        return f;
    }
}
