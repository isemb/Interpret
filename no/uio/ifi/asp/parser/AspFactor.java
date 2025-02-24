package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

import java.util.ArrayList;
import java.util.HashMap;


class AspFactor extends AspSyntax {

    ArrayList<AspPrimary> primaries = new ArrayList<>();
    ArrayList<AspFactorOpr> factorOprs = new ArrayList<>();
    HashMap<Integer, AspFactorPrefix> factorPrefixes = new HashMap<>();
    

    AspFactor(int n) {
        super(n);
    }


    static AspFactor parse(Scanner s) {
        enterParser("factor");
        AspFactor f = new AspFactor(s.curLineNum());

        int loopNum = 0;

        while(true) {
            if (s.isFactorPrefix()) {
                f.factorPrefixes.put(loopNum, AspFactorPrefix.parse(s));

            }
            f.primaries.add(AspPrimary.parse(s));
            
            if (!s.isFactorOpr()) {
                break;
            }
            f.factorOprs.add(AspFactorOpr.parse(s));
            loopNum += 1;
        }
        leaveParser("factor");
        return f;
    }


    @Override
    void prettyPrint() {
        int nPrinted = 0;
        
        for (AspPrimary p: primaries) {
            if (nPrinted > 0) {
                factorOprs.get(nPrinted-1).prettyPrint();
            }
            if (factorPrefixes.containsKey(nPrinted)) {
                factorPrefixes.get(nPrinted).prettyPrint();
            }
            p.prettyPrint(); ++nPrinted;
        }
    }


    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {

        RuntimeValue v = primaries.get(0).eval(curScope);
        RuntimeValue v2 = null;

        if(primaries.size() > 1 && !factorOprs.isEmpty()) {
            for (int i = 1; i < primaries.size(); i++) {
                TokenKind kOprs;
                TokenKind kPrefix;
                v2 = primaries.get(i).eval(curScope);

                if (factorPrefixes.containsKey(i-1) && primaries.indexOf(primaries.get(i-1)) == i-1) {
                    kPrefix = factorPrefixes.get(i-1).factorPrefixKind;
                    v = switch (kPrefix) {
                        case plusToken -> v.evalPositive(this);
                        case minusToken -> v.evalNegate(this);
                        default -> v;
                    };
                }

                if(factorPrefixes.containsKey(i) && primaries.indexOf(primaries.get(i)) == i){
                    kPrefix = factorPrefixes.get(i).factorPrefixKind;
                    v2 = switch (kPrefix) {
                        case plusToken -> v2.evalPositive(this);
                        case minusToken -> v2.evalNegate(this);
                        default -> v2;
                    };

                }
                
                if (factorOprs.size() > i-1) {

                    kOprs = factorOprs.get(i - 1).operatorKind;
                    v = switch (kOprs) {
                        case doubleSlashToken -> v.evalIntDivide(v2, this);
                        case astToken -> v.evalMultiply(v2, this);
                        case slashToken -> v.evalDivide(v2, this);
                        case percentToken -> v.evalModulo(v2, this);
                        default -> v;
                    };
                }
            }
        } else { //kun et tall
            if (factorPrefixes.containsKey(0)) {
                TokenKind kPrefix = factorPrefixes.get(0).factorPrefixKind;
                v = switch (kPrefix) {
                    case plusToken -> v.evalPositive(this);
                    case minusToken -> v.evalNegate(this);
                    default -> v;
                };
            }
        }
        return v;
    }
}
