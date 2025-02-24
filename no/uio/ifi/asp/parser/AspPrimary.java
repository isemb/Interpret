package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

import java.util.ArrayList;


class AspPrimary extends AspSyntax {

    AspAtom atom;
    ArrayList<AspPrimarySuffix> primarySuffixs = new ArrayList<>();


    AspPrimary(int n) {
        super(n);
    }


    static AspPrimary parse(Scanner s) {
        enterParser("primary");
        AspPrimary p = new AspPrimary(s.curLineNum());

        p.atom = AspAtom.parse(s);
        while (s.curToken().kind == leftParToken || s.curToken().kind == leftBracketToken) {
            p.primarySuffixs.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return p;
    }


    @Override
    void prettyPrint() {
        atom.prettyPrint();
        if (!primarySuffixs.isEmpty()) {
            for (AspPrimarySuffix ps : primarySuffixs) {
                ps.prettyPrint();
            }
        }
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue returnvalue = atom.eval(curScope);
        ArrayList<RuntimeValue> arguments = new ArrayList<RuntimeValue>();
        for (int i = 0; i < primarySuffixs.size(); i++) {
            RuntimeValue ps = primarySuffixs.get(i).eval(curScope);

            if (primarySuffixs.get(i) instanceof AspSubscription) {

                if (returnvalue instanceof RuntimeListValue) {
                    returnvalue = returnvalue.evalSubscription(ps, this);
                } else if (returnvalue instanceof RuntimeStringValue) {
                    returnvalue = returnvalue.evalSubscription(ps, this);
                } else if (returnvalue instanceof RuntimeDictValue) {
                    returnvalue = returnvalue.evalSubscription(ps, this);
                } else {
                    String err = "Subscription illegal for " + returnvalue.getClass().getSimpleName() + "!";
                    Main.error(err);
                }
            } else if (primarySuffixs.get(i) instanceof AspArguments) {
                String funcName = returnvalue.toString();
                String output = "Call function " + funcName + " with params ";
                arguments = ((RuntimeListValue)ps).getList("primary", this);

                trace("Call function " + funcName + " with params " + arguments);
                return returnvalue.evalFuncCall(arguments, this);

            }
        }
        return returnvalue;
    }
}