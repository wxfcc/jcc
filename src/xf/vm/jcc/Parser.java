/*
java c complier
author: WuXuefei
 */
package xf.jcc;

public class Parser extends Base{
	Jcc jcc;
	String srcPath;
	int codeIndex;
	byte[] code;
	
    public Parser(Jcc j, String path) {
    	jcc = j;
    	srcPath = path;
    }
    
    public boolean parse() {
    	boolean ret = false;
    	try {
    		code = readFile(srcPath);
    		if(code == null) {
    			pln("read file failed, " + srcPath);
    			return false;
    		}
			while(codeIndex < code.length) {
				String v = nextWord();
				if(v.length() > 0)
				pln(v);
			}
			ret = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private String nextWord() {
		StringBuilder sb = new StringBuilder();
		
		skipWhiteChar();
		if(codeIndex >= code.length) {
			return sb.toString();
		}
		byte c = code[codeIndex];
		boolean ispunct = ispunct(c);
		if(ispunct) {
			byte c2 = 0, c3 = 0;
			if(codeIndex + 1 < code.length)c2 = code[codeIndex+1];
			if(codeIndex + 2 < code.length)c3 = code[codeIndex+2];
			codeIndex++;
			sb.append((char)c);
			switch(c) {
			case '~':	//not, k=~k
				break;
			case '!':	//not test, k=!k;  if(!k)
			case '%':	//mod, %=
			case '^':	//xor, ^=
			case '*':	// *=
				if('=' == c2) {
					codeIndex++;
					sb.append((char)c2);
				}
				break;
			case '&':	//and && &= &&=
			case '|':	//or || |= ||=
			case '+':	// ++ += ++=
			case '-':	// -- -= --=
			case '=':	// ==
				if('=' == c2 || c == c2) {
					codeIndex++;
					sb.append((char)c2);
				}
				break;
//					case '@':
			case '#':	//macro ##
				break;
			case '/':
				if('*' == c2) {
					readComment(sb);
				}
				else if('/' == c2)
					readLineComment(sb);
				else if('=' == c2) {		// /=
					codeIndex++;
					sb.append((char)c2);
				}
				break;
			case '(':
			case ')':
			case '{':
			case '}':
			case '[':
			case ']':
			case ':':
			case ';':
			case ',':
			case '?':
				break;
			case '"':
				//sb.append((char)c);
				readString(sb);
				break;
			case '\'':
				readEscapeChar(sb);
				break;
			case '<':
			case '>':
				if(c == c2) {	// <<  >>
					codeIndex++;
					sb.append((char)c2);
					if('=' == c3 ) {	// <<=  >>=
						codeIndex++;
						sb.append((char)c3);
					}
				}
				else if('=' == c2) {	//<= >=
					codeIndex++;
					sb.append((char)c2);
				}
				break;
			default:	//case '`', '$', '@', '.', '\\'
			}
		}
		else {
			while(codeIndex < code.length ) {
				c = code[codeIndex++];
				if(isWhiteChar(c)) {
					break;
				}
				else if(isCrlf(c)) {
					break;
				}
				else if(ispunct == ispunct(c))
					sb.append((char)c);
				else {
					codeIndex --;
					break;
				}
			}
		}
		return sb.toString();
	}

	private String nextWord0() {
		StringBuilder sb = new StringBuilder();
		
		skipWhiteChar();
		if(codeIndex >= code.length) {
			return sb.toString();
		}
		byte lc = 0, c = code[codeIndex];
		if(c == '"') {
			codeIndex++;
			//sb.append((char)c);
			readString(sb);
		}
		else {
			boolean ispunct = ispunct(c);
			while(codeIndex < code.length ) {
				c = code[codeIndex++];
				if(isWhiteChar(c)) {
					break;
				}
				else if(isCrlf(c)) {
					break;
				}
				else if(ispunct == ispunct(c))
					sb.append((char)c);
				else {
					codeIndex --;
					break;
				}
			}
			if(sb.length() >= 2) {
				char c1 = sb.charAt(0), c2 = sb.charAt(1);
				if('/' == c1) {
					if('*' == c2) {
						if(c != c1 && c != c2)
							sb.append((char)c);
						readComment(sb);
					}
					else if('/' == c2)
						readLineComment(sb);
				}
			}
		}
		return sb.toString();
	}
	
	byte[] signChar = "`~!@%^&*()-=+[]{}\\|;:'\",<>/?".getBytes(); //#$.
	private boolean ispunct(byte c) {
		boolean ret = 
				(c >= 0x21 && c <= 0x2f ||	// !"#$%&'()*+,-./
				 c >= 0x3a && c <= 0x40 ||	// :;<=?@
				 c >= 0x5b && c <= 0x60 || 	// [\]^_`
				 c >= 0x7b && c <= 0x7e);	// {|}~

		if(ret && (c == '.' || c == '_' || c == '$'))
			ret = false;
		
		return ret;
	}

	private void readEscapeChar(StringBuilder sb) {
		while(codeIndex < code.length) {
			byte c = code[codeIndex++];
			if(c == '\\') {
				c = code[codeIndex++];
				switch(c) {
				case 't':sb.append('\t');break;
				case 'r':sb.append('\r');break;
				case 'n':sb.append('\n');break;
				default:sb.append((char)c);break;	// '\8'
				}
				continue;
			}
			sb.append((char)c);
			if(c == '\'') break;
		}		
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
			sb.append((char)c);
			if(c == '"') 
				break;
		}
//			pln("------------------");
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
		while(codeIndex < code.length&& (isWhiteChar(code[codeIndex]) || isCrlf(code[codeIndex]))) {
			codeIndex++;
		}
	}
	
	private boolean isWhiteChar(byte c) {
		if(c == ' ' || c == '\t')
			return true;
		return false;
	}
	private boolean isCrlf(byte c) {
		if(c == '\r' || c == '\n')
			return true;
		return false;
	}
    
}
