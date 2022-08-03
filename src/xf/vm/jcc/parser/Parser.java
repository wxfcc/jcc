/*
 *	java c complier
 *	author:	WuXuefei
 */
package	xf.vm.jcc.parser;

import java.util.ArrayList;
import java.util.List;

import xf.vm.base.Base;
import xf.vm.jcc.Jcc;

public class Parser	extends	Base{
	Jcc	jcc;
	String srcPath;
	int	codeIndex =	0, newLineIndex;
	int	lineNum	= 1;
	byte[] code;
	List<Token> tokens = new ArrayList<Token>();
	
	public Parser(Jcc j, String	path) {
		jcc	= j;
		srcPath	= path;
	}
	
	public boolean parse() {
		boolean	ret	= false;
		try	{
			code = readFile(srcPath);
			if(code	== null) {
				pln("read file failed, " + srcPath);
				return false;
			}
			while(codeIndex	< code.length) {
				Token t = nextWord();
				if(t !=	null) {
					tokens.add(t);
				}
			}
			ret	= true;
		} catch	(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	private	Token nextWord()	{
		Token t = new Token();
		StringBuilder sb = new StringBuilder();
		
		skipWhiteChar();
		if(codeIndex >=	code.length) {
			return null;// sb.toString();
		}
	
		byte c = code[codeIndex];
		byte c2	= 0, c3	= 0, c4 = 0;
		boolean	ispunct	= ispunct(c) && !ishyphen(c);
		t.codeIndex	= codeIndex;
		t.lineNum =	lineNum;
		t.lineCodeIndex=newLineIndex;
		t.ispunct =	ispunct;
		if(ispunct)	{
			if(codeIndex + 1 < code.length)c2 =	code[codeIndex+1];
			if(codeIndex + 2 < code.length)c3 =	code[codeIndex+2];
			if(codeIndex + 3 < code.length)c4 =	code[codeIndex+3];
			codeIndex++;
			sb.append((char)c);
			switch(c) {
			case '~':	//not, k=~k
				break;
			case '!':	//not test,	k=!k;  if(!k)
			case '%':	//mod, %=
			case '^':	//xor, ^=
			case '*':	// *=
				if('=' == c2) {
					codeIndex++;
					sb.append((char)c2);
				}
				break;
			case '&':	//and && &=	&&=
			case '|':	//or ||	|= ||=
			case '+':	// ++ += ++=
			case '-':	// -- -= --=
			case '=':	// ==
				if('=' == c2 ||	c == c2) {
					codeIndex++;
					sb.append((char)c2);
				}
				else if('-' == c && '>' == c2) {	// ->
					codeIndex++;
					sb.append((char)c2);					
				}
				break;
			case '#':	//macro	##
				break;
			case '/':
				if('*' == c2) {
					readComment(sb);
					t.type = Token.Type.COMMENT;
				}
				else if('/'	== c2) {
					readLineComment(sb);
					t.type = Token.Type.COMMENT;
				}
				else if('='	== c2) {		// /=
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
				if(c ==	c2)	{	// <<  >>
					codeIndex++;
					sb.append((char)c2);
					if('=' == c3 ) {	// <<=	>>=
						codeIndex++;
						sb.append((char)c3);
					}
				}
				else if('='	== c2) {	//<= >=
					codeIndex++;
					sb.append((char)c2);
				}
				break;
			case '.':	// ...
				if(c ==	c2 && c == c3 && (')' == c4 || !ispunct(c4))){	// ...
					codeIndex += 2;
					sb.append((char)c2);
					sb.append((char)c3);
					break;
				}
				
//				case '@':	//annotation
			default:	//case '`',	'$',  '\\'
			}
		}
		if(!ispunct || '.' == c) {
			while(codeIndex	< code.length )	{
				c =	code[codeIndex++];
				if(isWhiteChar(c)) {
					skipWhiteChar();
					if('.' == c2) {	//last char
						continue;
					}
					c2 = code[codeIndex++];
					if('.' == c2) {
						sb.append((char)c2);
					}
					else {
						codeIndex--;
						break;
					}
				}
				else if(isCrlf(c)) {
//					if('.' == c2) {
//						lineNum ++;
//						continue;
//					}
					break;
				}
				else if(ispunct	== ispunct(c) || ishyphen(c) || '.' == c) {
					c2 = 0;	// reset 0
					sb.append((char)c);
				}
				else {
					codeIndex--;
					break;
				}
			}
		}
		t.name	= sb.toString();
		return t; //sb.toString();
	}

	
	private	void readEscapeChar(StringBuilder sb) {
		while(codeIndex	< code.length) {
			byte c = code[codeIndex++];
			if(c ==	'\\') {
				c =	code[codeIndex++];
				switch(c) {
				case 't':sb.append('\t');break;
				case 'r':sb.append('\r');break;
				case 'n':sb.append('\n');break;
				default:sb.append((char)c);break;	// '\8'
				}
				continue;
			}
			sb.append((char)c);
			if(c ==	'\'') break;
		}		
	}

	private	void readComment(StringBuilder sb) {
		while(codeIndex	< code.length) {
			byte c = code[codeIndex++];
			if(c ==	'*') {
				byte c2 = code[codeIndex];
				if(c2 == '/') {
					codeIndex++;
					sb.append("*/");
					break;
				}
			}
			else if(c == '\n') {
				lineNum++;newLineIndex=codeIndex;
			}
			else if(c == '\r') { //isCrlf(c)){
				if(codeIndex+1 < code.length) {
					byte c2	= code[codeIndex+1];
					if(c2 == '\n') {	//dos format, \r\n
						codeIndex++;	//skip \n
					}
					lineNum++;newLineIndex=codeIndex;
				}
			}
			sb.append((char)c);
		}
		
	}

	private	void readString(StringBuilder sb) {
		while(codeIndex	< code.length) {
			byte c = code[codeIndex++];
			sb.append((char)c);
			if(c ==	'"') 
				break;
		}
//			pln("------------------");
	}

	private	void readLineComment(StringBuilder sb) {
		while(codeIndex	< code.length) {
			byte c = code[codeIndex++];
			if(c ==	'\n') {	//unix format
				break;
			}
			else if(c == '\r') {
				if(codeIndex < code.length)	{
					byte c2	= code[codeIndex];
					if(c2 == '\n') {	//dos format, \r\n
						codeIndex++;
						break;
					}
				}
				break;	//os2 format, \r
			}
			sb.append((char)c);
		}
		lineNum++;newLineIndex=codeIndex;
	}

	private	void skipWhiteChar() {
		while(codeIndex	< code.length) {
			byte c = code[codeIndex];
			if(!isWhiteChar(c))	{
				if(c ==	'\n') {
					lineNum++;codeIndex++;newLineIndex=codeIndex;
					continue;
				}
				else if(c == '\r') { //isCrlf(c)){
					if(codeIndex+1 < code.length) {
						byte c2	= code[codeIndex+1];
						if(c2 == '\n') {	//dos format, \r\n
							codeIndex++;	//skip \n
						}
						lineNum++;newLineIndex=codeIndex;
					}
					codeIndex++;
					continue;
				}
				break;
			}
			codeIndex++;
		}
	}
	
	public String readLine(Token t) {
		String ret;
		int n = t.codeIndex;
		for(;n < code.length;n++) {
			if(isCrlf(code[n]))break;
		}
		if(n >= code.length)n = code.length - 1;
		int len = n - t.lineCodeIndex ;
		ret = new String(code, t.lineCodeIndex, len);
		return ret;
	}

	
	private	boolean	isWhiteChar(byte c)	{
		boolean	ret	= (c ==	' '	|| c ==	'\t');
		return ret;
	}
	private	boolean	isCrlf(byte	c) {
		boolean	ret	= (c ==	'\r' ||	c == '\n');
		return ret;
	}

	private	boolean	ispunct(byte c)	{
		boolean	ret	= 
				(c >= 0x21 && c <= 0x2f	||	// !"#$%&'()*+,-./
				 c >= 0x3a && c <= 0x40	||	// :;<=?@
				 c >= 0x5b && c <= 0x60	||	// [\]^_`
				 c >= 0x7b && c <= 0x7e);	// {|}~
		return ret;
	}
	
	private	boolean	ishyphen(byte c)	{
		boolean ret =  (/*c == '.' ||*/ c == '_' || c == '$');
		return ret;
	}

	private	boolean	isalpha(byte c)	{
		boolean	ret	= 
				(c >= 'a' && c <= 'z'	||	// a-z
				 c >= 'A' && c <= 'Z'	);	// A-Z
		return ret;
	}

	private	boolean	isnumber(byte c)	{
		boolean	ret	= (c >= '0' && c <= '9');	// A-Z
		return ret;
	}
	
}
