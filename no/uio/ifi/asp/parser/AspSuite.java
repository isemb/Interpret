package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


public class AspSuite extends AspAtom {

    AspSmallStmtList ssl;
    ArrayList<AspStmt> stmts = new ArrayList<>();


    AspSuite(int n) {
        super(n);
    }


    static AspSuite parse(Scanner s) {
        enterParser("suite");
        AspSuite su = new AspSuite(s.curLineNum());
        
        switch (s.curToken().kind) {
            case globalToken:
            case passToken:
            case returnToken:
            case notToken:
            case plusToken:
            case minusToken: 
            case nameToken:
            case integerToken:
            case floatToken:
            case stringToken:
            case trueToken:
            case falseToken:
            case noneToken:
            case leftParToken:
            case leftBracketToken:
            case leftBraceToken:
                su.ssl = AspSmallStmtList.parse(s); 
                break;
            case newLineToken:
                skip(s, newLineToken);
                skip(s, indentToken);
                
                while (s.curToken().kind != dedentToken) {
                    su.stmts.add(AspStmt.parse(s));
                }
                skip(s, dedentToken);
                break;
            default:
                parserError("Expected a suite but found a " +
                s.curToken().kind + "!", s.curLineNum());
        }
        leaveParser("suite");
        return su;
    }


    @Override
    void prettyPrint() {
        if (ssl != null) {
            ssl.prettyPrint();
        } else {
            prettyWriteLn();
            prettyIndent();

            for (AspStmt as: stmts) {
                as.prettyPrint();
            }
            prettyDedent();
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = null;
        if(ssl != null){
            v = ssl.eval(curScope);
            return v;
        }
        for (int i = 0; i <stmts.size(); i++) {
            v = stmts.get(i).eval(curScope);
        }

        return v;
    }
}
