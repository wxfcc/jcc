package xf.vm.jcc.parser;
//package xf.vm.jcc.parser;

import java.util.ArrayList;
import java.util.List;

import xf.vm.base.Base;
import xf.vm.jcc.Jcc;

public class JcFile extends Base{	//Illegal modifier for the class JcFile; only public, abstract & final are permitted
	Parser parser;
	String packageName;
	String superClassName;
	String className;
	int classModifier;
	List<String>imports = new ArrayList<String>();
	List<String>implement = new ArrayList<String>();

	int tokenIndex = 0;
	List<Token> tokens;
	ExpectStatus expectStatus = ExpectStatus.PACKAGE; 
	int modifier;	// for field,method
	
	public JcFile(Jcc j, String path) {
		parser = new Parser(j, path);
	}
	public void parse() {
		try	{
			parser.parse();
			tokens = parser.tokens;
		} catch	(Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		tokenIndex = 0;
		while(tokenIndex < tokens.size()) {
			Token t = tokens.get(tokenIndex++);
			eval(t);
		}
		pln("package: "+packageName);
		for(String cn:imports)pln("import: "+cn);
		pln(Modifier.toString(classModifier) + " class: "+className+", super: "+ superClassName);
		for(String cn:implement)pln("implements: "+cn);
		
	}
	
	public void dumpTokens() {
		for(Token t : tokens) 
			pln("line "+ t.lineNum+", "	+(t.codeIndex -	t.lineCodeIndex+1)+": "+ t.name);
	}
	
	Token nextToken() {
		Token t = tokens.get(tokenIndex);
		tokenIndex++;
		return t;
	}
	void eval(Token t) {
		if(t.ispunct) {
			
		}
		else {
			int i = tokenIndex;
			switch(t.name) {
			case "package":
				if(expectStatus != ExpectStatus.PACKAGE) {
//					error(t, "import expected");
					error(t, "class, interface, or enum expected");
					break;
				}
					Token t2 = tokens.get(i);
					Token t3 = tokens.get(i+1);
					if(";".equals(t3.name)) {
						packageName = t2.name;
						tokenIndex = i + 2;
					}
					else {
//						error
					}
					expectStatus = ExpectStatus.IMPORT;
				break;
			case "import":
				if(expectStatus != ExpectStatus.IMPORT) {
					error(t, "illegal start of type");// interface expected after this token
					break;
				}
				break;
			case "public":
				classModifier |= Modifier.PUBLIC;	// static public, public static
				break;
			case "private":
				classModifier |= Modifier.PRIVATE;
				break;
			case "protect":
				classModifier |= Modifier.PROTECTED;
				break;
			case "abstract":
				classModifier |= Modifier.ABSTRACT;
				break;
			case "final":
				classModifier |= Modifier.FINAL;
				break;
			case "static":
				classModifier |= Modifier.STATIC;
				break;
			case "extends":
			case "{":	//code block
				break;
			case "class":
				expectStatus = ExpectStatus.SUPER_CLASS;
				break;
			case "print":
//				if(read(i,"(")) {
//					
//				}
			default:	//
				
			}
		}
		
	}
	
	private void error(Token t, String msg) {
		String  l = parser.readLine(t);
//		msg = (msg == null)?"":", "+msg;
//		pln("Syntax error on token \""+t.name+"\"" + msg +
//				", line "+ t.lineNum+":" + (t.codeIndex - t.lineCodeIndex+1)+": "+ l);
		
		if(msg == null)msg = "";
		pln(parser.srcPath+":"+ t.lineNum+": error: " + msg + "\n" + l);
		StringBuilder sb = new StringBuilder();
		for(int i=1;i<t.codeIndex - t.lineCodeIndex;i++)
			sb.append(' ');
		sb.append('^');
		sb.append("\n1 error");
		pln(sb.toString());
		System.exit(0);
	}
	
//	public void run0() {
//		readPackageName();
//		readImports();
//		readClassName();
//		pln("package: "+packageName);
//		for(String cn:imports)pln("import: "+cn);
//		pln(modifier + " class: "+className+", super: "+ superClassName);
//		for(String cn:implement)pln("implements: "+cn);
//		pln(tokens.get(tokenIndex).name);
//		
//		while(tokenIndex < tokens.size()) {
//			Token t = tokens.get(tokenIndex);
////			pln("line "+ t.lineNum+", "	+(t.codeIndex -	t.lineCodeIndex+1)+": "+ t.name);
//
//			if(t.ispunct) {
//				
//			}
//			else {
//				switch(t.name) {
//				case "import":
//				case "public":
//				case "private":
//				case "extends":
//				case "{":	//code block
//					break;
//				case "print":
////					if(read(i,"(")) {
////						
////					}
//				default:	//
//				}
//			}
//			tokenIndex++;
//		}
//	}	

	private void readImports() {
		int n=0;
		for(int i=tokenIndex;i<tokens.size();i++) {
			Token t = tokens.get(i);
			if(t.type != Token.Type.COMMENT) {
				if("import".equals(t.name)) {
					n=i;
					break;
				}
				else {
					tokenIndex = i;
					return;
				}
			}
		}
		//import
		for(int i=n;i<tokens.size();i++) {
			Token t = tokens.get(i);
			if("import".equals(t.name)) {
				Token t2 = tokens.get(i+1);
				Token t3 = tokens.get(i+2);
				String cn = t2.name;
				if("*".equals(t3.name)) {
					cn += t3.name;
					t3 = tokens.get(i+3);
					i++;
				}
				if(";".equals(t3.name)) {
					imports.add(cn);
					i += 2;
				}
				else {
//					error
					return;
				}
			}
			else {
				tokenIndex = i;
				return;
			}
		}		
	}
	private void readPackageName() {
		for(int i=tokenIndex;i<tokens.size();i++) {
			Token t = tokens.get(i);
			if("package".equals(t.name)) {
				Token t2 = tokens.get(i+1);
				Token t3 = tokens.get(i+2);
				if(";".equals(t3.name)) {
					packageName = t2.name;
					tokenIndex = i + 3;
				}
				else {
//					error
				}
				return;
			}
		}
		//note found
		packageName = "default";
		
	}
//	private void readClassName() {
//		classModifier = 0;
//		for(int i=tokenIndex;i<tokens.size();i++) {
//			Token t = tokens.get(i);
//			if("public".equals(t.name)) {
//				//classModifier = t.name;
//				classModifier |= Modifier.PUBLIC;	// static public, public static
//				pln(t.name);
//			}
//			else if("private".equals(t.name)) {
//				classModifier |= Modifier.PRIVATE;
//				pln(t.name);				
//			}
//			else if("protect".equals(t.name)) {
//				classModifier |= Modifier.PROTECTED;
//				pln(t.name);				
//			}
//			else if("abstract".equals(t.name)) {
//				classModifier |= Modifier.ABSTRACT;
//			}
//			else if("final".equals(t.name)) {
//				classModifier |= Modifier.FINAL;
//			}
//			else if("static".equals(t.name)) {
//				classModifier |= Modifier.STATIC;
//			}
//			//public class a extends b implements c,d,e {
//			else if("class".equals(t.name) || "interface".equals(t.name)|| "enum".equals(t.name)) {	
//				int n = i+1;
//				Token t2 = tokens.get(n++);
//				Token t3 = tokens.get(n++);
//				if("extends".equals(t3.name)) {
//					Token t4 = tokens.get(i+3);
//					superClassName = t4.name;
//					n++;
//				}
//				Token t5 = tokens.get(n);
//				if("implements".equals(t5.name)) {
//					n++;
//					while(n < tokens.size()) {
//						t5 = tokens.get(n);
//						if("{".equals(t5.name)) {	//class code begin
//							break;
//						}
//						else if(",".equals(t5.name)) {
//							t5 = tokens.get(++n);
//						}
//						if(t5.ispunct) {
//							pln("error: "+t5.name);
//							return;
//						}
//						implement.add(t5.name);
//						n++;
//					}
//				}
//				
//				className = t2.name;
//				tokenIndex = n;
//				return;
//			}
//			else {
//				pln("unkown error");
//				return;
//			}
//		}
//		//note found
//		String path = parser.srcPath.replace('\'', '/');
//		int l = path.lastIndexOf('/');
//		int r = path.lastIndexOf('.');
//		if(l <= 0)l = 0; else l++;
//		if(r <= l) r = path.length();
//		className = path.substring(l, r);
//	}
	
	private boolean read(String name) {
		return false;
	}

	enum ExpectStatus{
		NONE,
		PACKAGE, 
		IMPORT,
		CLASS, 
		SUPER_CLASS, 
		IMPLEMENTS, 
		CLASS_CODE_BEGIN, 
		VARIABLE_NAME,
		METHOD__NAME, 
		//"(){};:", 
		WHILE, 
		CASE, 
		BREAK, 
		MACRO_END	//#endf 
		
	}
}
