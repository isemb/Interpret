package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.*;


// Source: Langmyhr, D., & Runde, R. K. (2023). "uke38-parser-utdeling.pdf". IN2030, Department of Informatics, University of Oslo.
// URL: https://www.uio.no/studier/emner/matnat/ifi/IN2030/h23/timeplan/uke38-parser-utdeling.pdf, slide 8.
abstract class AspAtom extends AspSyntax {

    AspAtom(int n) {
        super(n);
    }

    
    static AspAtom parse(Scanner s) {
        enterParser("atom");
        AspAtom a = null;
        
        switch (s.curToken().kind) {
            case falseToken:
            case trueToken:
                a = AspBooleanLiteral.parse(s); break;
            case floatToken:
                a = AspFloatLiteral.parse(s); break;
            case integerToken:
                a = AspIntegerLiteral.parse(s); break;
            case leftBraceToken:
                a = AspDictDisplay.parse(s); break;
            case leftBracketToken:
                a = AspListDisplay.parse(s); break;
            case leftParToken:
                a = AspInnerExpr.parse(s); break;
            case nameToken:
                a = AspName.parse(s); break;
            case noneToken:
                a = AspNoneLiteral.parse(s); break;
            case stringToken:
                a = AspStringLiteral.parse(s); break;
            default:
                parserError("Expected an expression atom but found a " +
                s.curToken().kind + "!", s.curLineNum());
            }
        leaveParser("atom");
        return a;
    }
}
