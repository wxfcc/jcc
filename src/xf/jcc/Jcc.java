/*
java c complier
author: WuXuefei
*/
package xf.jcc;


public class Jcc extends Base{
	int codeIndex;
	byte[] code;
    public static void main(String[] args) {
    	String path = "/Users/xuefeiwu/eclipse-workspace/jcc/doc/test/t1.java";
    	try {
    		Jcc p = new Jcc(path);
    		
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	pln("done");
    }
	
    public Jcc(String path) {
    	try {
    		code = readFile(path);
    		parse();
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	private void parse() {
		while(codeIndex < code.length) {
			String v = nextWord();
			if(v.length() > 0)
			pln(v);
		}
		
	}
	
	private String nextWord() {
		StringBuilder sb = new StringBuilder();
		
		skipWhiteChar();
		byte lc = 0;
		while(codeIndex < code.length) {
			byte c = code[codeIndex++];
			if(isWhite(c))
				break;
			if(c == '+') {
				if(lc == '+') {
					sb.append((char)c);
					break;
				}				
			}
			if(c == '-') {
				if(lc == '-') {
					sb.append((char)c);
					break;
				}				
			}
			if(c == '*') {
				if(lc == '/') {
					sb.append((char)c);
					readComment(sb);
					break;
				}				
			}
			if(c == '/') {
				if(lc == '/') {
					sb.append((char)c);
					readLineComment(sb);
					break;
				}
			}
			if(c == '=') {
				if(lc == '+' || lc == '-' || lc == '*' || lc == '/') {
					sb.append((char)c);
					break;
				}				
			}
			if(c == '\'') {
				//
			}
			if(c == '|') {
				if(lc == '|') {
					sb.append((char)c);
					break;
				}				
			}
			if(c == '"') {
				readString(sb);
				break;
			}
			if(c == '{' || c == '}' || c == '(' || c == ')' || c == ';' || c == '#' 
					|| c == '<' || c == '>' || c == '=' 
					|| c == '.' || c == '?' || c == '!' || c == '[' || c == ']' || c == ',') {
				if(sb.length() > 0)
					codeIndex--;
				else
					sb.append((char)c);
				break;
			}
			sb.append((char)c);
			lc = c;
		}
		
		return sb.toString();
	}
	
	private void readComment(StringBuilder sb) {
		while(codeIndex < code.length) {
			byte c = code[codeIndex++];
			if(c == '*') {
				c = code[codeIndex++];
				if(c == '/') {
					sb.append("*/");
					break;
				}
			}
			sb.append((char)c);
		}
		
	}

	private void readString(StringBuilder sb) {
		while(codeIndex < code.length) {
			byte c = code[codeIndex++];
			if(c == '"') {
				break;
			}
			sb.append((char)c);
		}
	}

	private void readLineComment(StringBuilder sb) {
		while(codeIndex < code.length) {
			byte c = code[codeIndex++];
			if(c == '\r' || c == '\n') {
				break;
			}
			sb.append((char)c);
		}
	}

	private void skipWhiteChar() {
		while(codeIndex < code.length && isWhite(code[codeIndex])) {
			codeIndex++;
		}
	}
	
	private boolean isWhite(byte c) {
		if(c == ' ' || c == '\t' || c == '\r' || c == '\n')
			return true;
		return false;
	}
    
}
