package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


class AspAssignment extends AspSmallStmt {

    AspName name;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expr;


    AspAssignment(int n) {
        super(n);
    }


    static AspAssignment parse(Scanner s) {
        enterParser("assignment");
        AspAssignment a = new AspAssignment(s.curLineNum());

        a.name = AspName.parse(s);
        while(s.curToken().kind != equalToken) {
            a.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, equalToken);
        a.expr = AspExpr.parse(s);

        leaveParser("assignment");
        return a;
    }

    
    @Override
    void prettyPrint() {
        name.prettyPrint();
        if (!subscriptions.isEmpty()) {
            for (AspSubscription s: subscriptions) {
                s.prettyPrint();
            }
        }
        prettyWrite(" = ");
        expr.prettyPrint();
    }



    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (subscriptions.isEmpty()) {
            curScope.assign(name.name, expr.eval(curScope));
            trace(name.name + " = " + curScope.find(name.name, this));
        } else {
            RuntimeValue a = name.eval(curScope); //RuntimeListValue
            RuntimeValue tmp = a;
            String output = name.name;
            for (int i = 0; i < subscriptions.size()-1 ; i++) {
                RuntimeValue inx = subscriptions.get(i).eval(curScope);
                tmp = tmp.evalSubscription(inx, this);
                output += "[" + inx + "]";
            }
            RuntimeValue tmpInx = subscriptions.get(subscriptions.size()-1).eval(curScope);
            RuntimeValue e = expr.eval(curScope);
            trace(output + "[" + tmpInx + "]" + " = " + e);
            tmp.evalAssignElem(tmpInx, e, this);
        }
        return null;
    }
}
