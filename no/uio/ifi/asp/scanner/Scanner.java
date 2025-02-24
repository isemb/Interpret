// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;
import no.uio.ifi.asp.main.*;

import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;
	private HashMap<String, TokenKind> keyWords = new HashMap<String, TokenKind>();
	private HashMap<String, TokenKind> operatorsAndDelimiters = new HashMap<String, TokenKind>();


    public Scanner(String fileName) {
		// Keywords
		keyWords.put("and", andToken);
		keyWords.put("def", defToken);
		keyWords.put("elif", elifToken);
		keyWords.put("else", elseToken);
		keyWords.put("False", falseToken);
		keyWords.put("for", forToken);
		keyWords.put("global", globalToken);
		keyWords.put("if", ifToken);
		keyWords.put("in", inToken);
		keyWords.put("None", noneToken);
		keyWords.put("not", notToken);
		keyWords.put("or", orToken);
		keyWords.put("pass", passToken);
		keyWords.put("return", returnToken);
		keyWords.put("True", trueToken);
		keyWords.put("while", whileToken);

		// Operators and delimiters
		operatorsAndDelimiters.put("*", astToken);
		operatorsAndDelimiters.put("==", doubleEqualToken);
		operatorsAndDelimiters.put("//", doubleSlashToken);
		operatorsAndDelimiters.put(">", greaterToken);
		operatorsAndDelimiters.put(">=", greaterEqualToken);
		operatorsAndDelimiters.put("<", lessToken);
		operatorsAndDelimiters.put("<=", lessEqualToken);
		operatorsAndDelimiters.put("-", minusToken);
		operatorsAndDelimiters.put("!=", notEqualToken);
		operatorsAndDelimiters.put("%", percentToken);
		operatorsAndDelimiters.put("+", plusToken);
		operatorsAndDelimiters.put("/", slashToken);
		operatorsAndDelimiters.put(":", colonToken);
		operatorsAndDelimiters.put(",", commaToken);
		operatorsAndDelimiters.put("=", equalToken);
		operatorsAndDelimiters.put("{", leftBraceToken);
		operatorsAndDelimiters.put("[", leftBracketToken);
		operatorsAndDelimiters.put("(", leftParToken);
		operatorsAndDelimiters.put("}", rightBraceToken);
		operatorsAndDelimiters.put("]", rightBracketToken);
		operatorsAndDelimiters.put(")", rightParToken);
		operatorsAndDelimiters.put(";", semicolonToken);


		curFileName = fileName;
		indents.push(0);

		try {
	    	sourceFile = new LineNumberReader(
				    new InputStreamReader(
					new FileInputStream(fileName),
					"UTF-8"));
		} catch (IOException e) {
	    	scannerError("Cannot read " + fileName + "!");
		}
    }


    private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
	    	m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
    }


    public Token curToken() {
		while (curLineTokens.isEmpty()) {
	    	readNextLine();
		}
		return curLineTokens.get(0);
    }


    public void readNextToken() {
		if (! curLineTokens.isEmpty())
	    	curLineTokens.remove(0);
    }


    private void readNextLine() {
		curLineTokens.clear();
	
		// Read the next line:
		String line = null;
		try {
		    line = sourceFile.readLine();
		    if (line == null) {
				sourceFile.close();
				sourceFile = null;
				for (int i = 0; i<indents.size(); i++ ) {
					if (indents.get(i) > 0) {
						curLineTokens.add(new Token(dedentToken));
					}
				}
				curLineTokens.add(new Token(eofToken, curLineNum()));
				for (Token t: curLineTokens) {
					Main.log.noteToken(t);
				}
				return;
		    } else {
				Main.log.noteSourceLine(curLineNum(), line);
		    }
		} catch (IOException e) {
		    sourceFile = null;
		    scannerError("Unspecified I/O error!");
		}

		// Handle lines containing only a comment (and whitespaces)
		if (line != null) {
			String trimLine = line.trim();
			// Check first non-blank symbol on line
			if (trimLine.startsWith("#") || trimLine.isBlank()) { 
				return;
			}
		}
		
		String tabsToSpaces = expandLeadingTabs(line);
		int indCount = findIndent(tabsToSpaces);

		if (indCount > indents.peek()) {
			indents.push(indCount);
			curLineTokens.add(new Token(indentToken, curLineNum()));
		} else {
			while (indCount < indents.peek()) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
		} 
		
		int curIndex = indCount;
		String currentToken = "";

		while (curIndex < tabsToSpaces.length()) {
			char currentChar = tabsToSpaces.charAt(curIndex);
			// Handle comments on lines with code
			if(currentChar == '#') {
				break;
			}
			// Handle names, keywords
			if ((isLetterAZ(currentChar) || (currentChar == '_'))) {
                while (curIndex < tabsToSpaces.length()) {
                    currentChar = tabsToSpaces.charAt(curIndex);
                    if (isLetterAZ(currentChar) || (isDigit(currentChar) || (currentChar == '_'))) {
                        currentToken += currentChar;
                        curIndex ++;
                    } else {
                        break;
                    }
                }
                if (keyWords.containsKey(currentToken)) {
                    curLineTokens.add(new Token(keyWords.get(currentToken), curLineNum()));
                } else {
                    Token newNameToken = new Token(nameToken, curLineNum());
                    newNameToken.name = currentToken;
                    curLineTokens.add(newNameToken);
                }
                currentToken = "";
			} 
			// Handle whitespaces 
			else if (currentChar == ' ') {
				curIndex ++;
			} 
			// Handle operators and delimiters
			else if ((operatorsAndDelimiters.containsKey((String.valueOf(currentChar)))) || (currentChar == '!')) {
				while (curIndex < tabsToSpaces.length()) {
					currentChar = tabsToSpaces.charAt(curIndex);
					if ((currentChar == '!') && (curIndex + 1 < tabsToSpaces.length()) && (tabsToSpaces.charAt(curIndex + 1) == '=')) {
						if (!currentToken.isEmpty()) {
							if (operatorsAndDelimiters.containsKey(currentToken)) {
								curLineTokens.add(new Token(operatorsAndDelimiters.get(currentToken), curLineNum()));
							} else {
								// Handle error?
							}
						}
						curLineTokens.add(new Token(notEqualToken, curLineNum()));
						currentToken = "";
						curIndex += 2;
					} else if (operatorsAndDelimiters.containsKey(currentToken + currentChar)) {
						currentToken += currentChar;
						curIndex++;
					} else {
						if (!currentToken.isEmpty()) {
							if (operatorsAndDelimiters.containsKey(currentToken)) {
								curLineTokens.add(new Token(operatorsAndDelimiters.get(currentToken), curLineNum()));
							} else {
								// Handle error?
							}
						}
						currentToken = "";
						break;
					}
				}
				if (!currentToken.isEmpty()) {
					if (operatorsAndDelimiters.containsKey(currentToken)) {
						curLineTokens.add(new Token(operatorsAndDelimiters.get(currentToken), curLineNum()));
					} else {
						// Handle error?
					}
				}
				currentToken = "";
			}	
			// Handle numbers
			else if (isDigit(currentChar)) {
				if (currentChar == '0') {
					currentToken += currentChar;
					if ((curIndex + 1 < tabsToSpaces.length()) && (tabsToSpaces.charAt(curIndex + 1) == '.')) {
						currentChar = tabsToSpaces.charAt(curIndex + 1);
						currentToken += currentChar;
						curIndex ++;
						while ((curIndex + 1 < tabsToSpaces.length()) && (isDigit(tabsToSpaces.charAt(curIndex+1)))) {
							currentChar = tabsToSpaces.charAt(curIndex+1);
							currentToken += currentChar;
							curIndex ++;
						}
						if (isDigit(tabsToSpaces.charAt(curIndex))) {
							Token newTokenFloat = new Token(floatToken, curLineNum());
							newTokenFloat.floatLit = Double.parseDouble(currentToken);
							curLineTokens.add(newTokenFloat);
							currentToken = "";
							curIndex ++;
						}
						else {
							scannerError("Illegal character : '" + tabsToSpaces.charAt(curIndex) + "'!");
						}
						
					} else {
						Token newTokenNum = new Token(integerToken, curLineNum());
						newTokenNum.integerLit = Integer.parseInt(currentToken);
						curLineTokens.add(newTokenNum);
						currentToken = "";
						currentChar = tabsToSpaces.charAt(curIndex);
						curIndex ++;
					}
				} else {
					currentToken += currentChar;
					while ((curIndex + 1 < tabsToSpaces.length()) && (isDigit(tabsToSpaces.charAt(curIndex+1)))) {
						currentChar = tabsToSpaces.charAt(curIndex+1);
						currentToken += currentChar;
						curIndex ++;
					}
					if ((curIndex + 1 < tabsToSpaces.length()) && (tabsToSpaces.charAt(curIndex + 1) == '.')) {
						currentChar = tabsToSpaces.charAt(curIndex + 1);
						currentToken += currentChar;
						curIndex ++;
						while ((curIndex + 1 < tabsToSpaces.length()) && (isDigit(tabsToSpaces.charAt(curIndex+1)))) {
							currentChar = tabsToSpaces.charAt(curIndex+1);
							currentToken += currentChar;
							curIndex ++;
						}
						if (isDigit(tabsToSpaces.charAt(curIndex))) {
							Token newTokenFloat = new Token(floatToken, curLineNum());
							newTokenFloat.floatLit = Double.parseDouble(currentToken);
							curLineTokens.add(newTokenFloat);
							currentToken = "";
							curIndex ++;
						}
						else {
							scannerError("Illegal character : '" + tabsToSpaces.charAt(curIndex) + "'!");
						}
					} else {
						Token newTokenNumber = new Token(integerToken, curLineNum());
						newTokenNumber.integerLit = Integer.parseInt(currentToken);
						curLineTokens.add(newTokenNumber);
						currentToken = "";
						curIndex ++;
					}
				}
			}
			else if (currentChar == '\"' || currentChar == '\'') {
				char quoteType = currentChar;
				while ((curIndex + 1 < tabsToSpaces.length()) && (tabsToSpaces.charAt(curIndex+1) != quoteType)) {
					currentChar = tabsToSpaces.charAt(curIndex+1);
					currentToken += currentChar;
					curIndex ++;
				}
				if ((curIndex + 1 < tabsToSpaces.length()) && (tabsToSpaces.charAt(curIndex+1) == quoteType)) {
					Token newTokenString = new Token(stringToken, curLineNum());
					newTokenString.stringLit = currentToken;
					curLineTokens.add(newTokenString);
					currentToken = "";
					curIndex += 2;
				} else {
					scannerError("String literal not terminated!");
				}
			}
			else {
				scannerError("Illegal  character: '" + tabsToSpaces.charAt(curIndex) + "'!");
				break;
			}
		}

		// Terminate line
		curLineTokens.add(new Token(newLineToken,curLineNum()));

		for (Token t: curLineTokens) 
			Main.log.noteToken(t);
    }


    public int curLineNum() {
		return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

	
    private int findIndent(String s) {
		int indent = 0;

		while (indent<s.length() && s.charAt(indent)==' ') indent++;
		return indent;
    }


	// Change tabs to spaces at start of line
    private String expandLeadingTabs(String s) {
		String newStr = "";
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                newStr += ' ';
                n ++;
            } else if (c == '\t') {
                int spacesCount = 4 - (n % 4);
                for (int j = 0; j < spacesCount; j++) {
                    newStr += ' ';
                    n++;
                }
            } else {
                newStr += s.substring(i);
                break;  
            }
        }
        return newStr;
    }


    private boolean isLetterAZ(char c) {
		return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
		return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		return (k == lessToken ||
				k == doubleEqualToken ||
				k == greaterEqualToken ||
				k == greaterToken || 
				k == lessEqualToken ||
				k == notEqualToken);
    }


    public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		return (k == plusToken || k == minusToken);
    }


    public boolean isFactorOpr() {
		TokenKind k = curToken().kind;
		return (k == astToken ||
				k == slashToken ||
				k == percentToken ||
				k == doubleSlashToken);
    }
	

    public boolean isTermOpr() {
		TokenKind k = curToken().kind;
		return (k == plusToken || k == minusToken);
    }


    public boolean anyEqualToken() {
		for (Token t: curLineTokens) {
	    	if (t.kind == equalToken) return true;
	    	if (t.kind == semicolonToken) return false;
		}
		return false;
    }
}
