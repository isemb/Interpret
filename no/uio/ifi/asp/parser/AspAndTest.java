package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;

import static no.uio.ifi.asp.scanner.TokenKind.*;


// Source: Krogdahl, S., & Langmyhr, D. (2023). "En interpret for Asp - Kompendium for IN2030". IN2030, Department of Informatics, University of Oslo.
// URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/dokumenter/kompendium.pdf
class AspAndTest extends AspSyntax {

    ArrayList<AspNotTest> notTests = new ArrayList<>();


    AspAndTest(int n) {
        super(n);
    }


    static AspAndTest parse(Scanner s) {
        enterParser("and test");
        AspAndTest at = new AspAndTest(s.curLineNum());

        while (true) {
            at.notTests.add(AspNotTest.parse(s));

            if (s.curToken().kind != andToken) {
                break;
            }
            skip(s, andToken);
        }
        leaveParser("and test");
        return at;
    }


    @Override
    void prettyPrint() {
        int nPrinted = 0;

        for (AspNotTest nt : notTests) {
            if (nPrinted > 0) {
                prettyWrite(" and ");
            }
            nt.prettyPrint();
            ++nPrinted;
        }
    }

    // Source: Krogdahl, S., & Langmyhr, D. (2023). "En interpret for Asp - Kompendium for IN2030". Page 46. IN2030, Department of Informatics, University of Oslo.
    // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/dokumenter/kompendium.pdf
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = notTests.get(0).eval(curScope);
        for (int i = 1; i < notTests.size(); ++i) {
            if (! v.getBoolValue("and operand", this)) {
                return v;
            }
            v = notTests.get(i).eval(curScope);
        }
        return v;
    }
}
