package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;


class AspComparison extends AspSyntax {

    ArrayList<AspTerm> aspTerms = new ArrayList<>();
    ArrayList<AspCompOpr> compOprs = new ArrayList<>();

    
    AspComparison(int n) {
        super(n);
    }


    static AspComparison parse (Scanner s) {
        enterParser("comparison");
        AspComparison c = new AspComparison(s.curLineNum());

        while (true) {
            c.aspTerms.add(AspTerm.parse(s));

            if (!s.isCompOpr()) {
                break;
            }
            c.compOprs.add(AspCompOpr.parse(s));
        }
        leaveParser("comparison");
        return c;
    }


    @Override
    void prettyPrint() {
        int nPrinted = 0;
        
        for (AspTerm t: aspTerms) {
            if (nPrinted > 0) {
                compOprs.get(nPrinted-1).prettyPrint();
            }
            t.prettyPrint(); ++nPrinted;
        }
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aspTerms.get(0).eval(curScope);
        if (aspTerms.size() > 1) {
            for (int i = 1; i < aspTerms.size(); ++i) {
                TokenKind k = compOprs.get(i-1).operatorKind;
                RuntimeValue v1 = aspTerms.get(i-1).eval(curScope);
                RuntimeValue v2 = aspTerms.get(i).eval(curScope);

                switch (k) {
                    case lessToken:
                        v = v1.evalLess(v2, this); break;
                    case greaterToken:
                        v = v1.evalGreater(v2, this); break;
                    case doubleEqualToken:
                        v = v1.evalEqual(v2, this); break;
                    case greaterEqualToken:
                        v = v1.evalGreaterEqual(v2, this); break;
                    case lessEqualToken:
                        v = v1.evalLessEqual(v2, this); break;
                    case notEqualToken:
                        v = v1.evalNotEqual(v2, this); break;
                    default:
                        Main.panic("Illegal comparison operator: " + k + "!");
                }
                if (!v.getBoolValue("comp opr", this)) {
                    break;
                }
            }
        }
        return v;
    }
}
