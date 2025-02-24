package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;


class AspPassStmt extends AspSmallStmt {

    AspPassStmt(int n) {
        super(n);
    }


    static AspPassStmt parse(Scanner s){
        enterParser("pass stmt");
        AspPassStmt ps = new AspPassStmt(s.curLineNum());

        skip(s, passToken);

        leaveParser("pass stmt");
        return ps;
    }

    
    @Override
    void prettyPrint() {
        prettyWrite(passToken.toString());
    }


    // Source: Langmyhr, D., & Runde, R. K. (2023). "uke-44-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
    // URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke-44-utdeling.pdf, slide 4.
    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("pass");
        return null;
    }
}
