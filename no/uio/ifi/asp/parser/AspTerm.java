package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

import java.util.ArrayList;


class AspTerm extends AspSyntax {

    ArrayList<AspFactor> aspFactors = new ArrayList<>();
    ArrayList<AspTermOpr> termOpr = new ArrayList<>();


    AspTerm(int n) {
        super(n);
    }


    static AspTerm parse(Scanner s) {
        enterParser("term");
        AspTerm t = new AspTerm(s.curLineNum());
        while (true) {
            t.aspFactors.add(AspFactor.parse(s));
            if (!s.isTermOpr()) {
                break;
            }
            t.termOpr.add(AspTermOpr.parse(s));
        }
        leaveParser("term");
        return t;
    }


    @Override
    void prettyPrint() {
       int nPrinted = 0;
        for (AspFactor f: aspFactors) { 
            if (nPrinted > 0) {
                termOpr.get(nPrinted-1).prettyPrint();
            }
            f.prettyPrint(); ++nPrinted;
        }
    }

    // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-41-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
    // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-41-utdeling.pdf, slide 22.
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aspFactors.get(0).eval(curScope);
        for (int i = 1; i < aspFactors.size(); ++i) {
            TokenKind k = termOpr.get(i-1).oprKind;
            switch (k) {
                case minusToken:
                    v = v.evalSubtract(aspFactors.get(i).eval(curScope), this); break;
                case plusToken:
                    v = v.evalAdd(aspFactors.get(i).eval(curScope), this); break;
                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }
        }
        return v;
    }
}
