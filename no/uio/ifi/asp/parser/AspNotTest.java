package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspNotTest extends AspSyntax {

    AspComparison comparison;
    Boolean hasNot = false;


    AspNotTest(int n) {
        super(n);
    }


    static AspNotTest parse (Scanner s) {
        enterParser("not test");
        AspNotTest nt = new AspNotTest(s.curLineNum()); 

        if (s.curToken().kind == notToken) {
            skip(s, notToken);
            nt.hasNot = true;
        }
        nt.comparison = AspComparison.parse(s);
        
        leaveParser("not test");
        return nt;
    }


    @Override
    void prettyPrint() {
        if(hasNot) prettyWrite(notToken.toString() + " ");
        comparison.prettyPrint();
    }


    // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-41-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
    // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-41-utdeling.pdf, slide 12.
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = comparison.eval(curScope);
        if (hasNot) {
            v = v.evalNot(this);
        }
        return v;
    }

}
