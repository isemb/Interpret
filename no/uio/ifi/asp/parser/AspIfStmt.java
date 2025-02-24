package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


class AspIfStmt extends AspCompoundStmt {

    ArrayList<AspExpr> exprs = new ArrayList<>();
    ArrayList<AspSuite> suites = new ArrayList<>();
    AspSuite suite;
    Boolean hasElse = false;


    AspIfStmt(int n) {
        super(n);
    }


    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");
        AspIfStmt ifs = new AspIfStmt(s.curLineNum());
        
        skip(s, ifToken);

        while(true) {
           ifs.exprs.add(AspExpr.parse(s));
           skip(s, colonToken);
           ifs.suites.add(AspSuite.parse(s));

           if (s.curToken().kind != elifToken) {
                break;
            }
            skip(s, elifToken); 
        }
        if (s.curToken().kind == elseToken) {
            skip(s, elseToken);
            ifs.hasElse = true;
            skip(s, colonToken);
            ifs.suite = AspSuite.parse(s);
        }
        leaveParser("if stmt");
        return ifs;
    }


    @Override
    void prettyPrint() {
        prettyWrite(ifToken.toString() + " ");
        int nPrinted = 0;
        
        for (AspExpr ex: exprs) {
            if (nPrinted > 0) {
                prettyWrite(elifToken.toString());
            }
            ex.prettyPrint();
            prettyWrite(colonToken.toString() + " ");
            suites.get(nPrinted).prettyPrint();
            ++nPrinted;
        }
        if (hasElse) {
            prettyWrite(elseToken.toString());
            prettyWrite(colonToken.toString());
            suite.prettyPrint();
        }
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (int i = 0; i < exprs.size(); i++) {
            if( exprs.get(i).eval(curScope).getBoolValue("expr", this)){
                trace("if True " + "alt #" + (i+1) + ": ...");
                suites.get(i).eval(curScope);
                return null;
            }
        }
       if (suite != null){
            trace("else: ...");
            suite.eval(curScope);
        }
        return null;
    }
}
