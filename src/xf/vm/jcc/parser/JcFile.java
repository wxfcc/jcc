package xf.vm.jcc.parser;

import java.util.ArrayList;
import java.util.List;

import xf.vm.base.Base;
import xf.vm.jcc.Jcc;

public class JcFile extends Base{
	Parser parser;
	String packageName;
	String superClassName;
	String className;
	String modifier;
	boolean abstractClass;
	List<String>imports = new ArrayList<String>();
	List<String>implement = new ArrayList<String>();

	ExpectStatus expectStatus; 

	public JcFile(Jcc j, String path) {
		parser = new Parser(j, path);
	}
	public void parse() {
		try	{
			parser.parse();
		} catch	(Exception e) {
			e.printStackTrace();
		}
	}

	int wIndex = 0;
	List<Token> tokens;
	public void run() {
		tokens = parser.tokens;
		readPackageName();
		readImports();
		readClassName();
		pln("package: "+packageName);
		for(String cn:imports)pln("import: "+cn);
		pln(modifier + " class: "+className+", super: "+ superClassName);
		for(String cn:implement)pln("implements: "+cn);
		pln(tokens.get(wIndex).name);
		
		while(wIndex < tokens.size()) {
			Token t = tokens.get(wIndex);
//			pln("line "+ t.lineNum+", "	+(t.codeIndex -	t.lineCodeIndex+1)+": "+ t.name);

			if(t.ispunct) {
				
			}
			else {
				switch(t.name) {
				case "import":
				case "public":
				case "private":
				case "extends":
				case "{":	//code block
					break;
				case "print":
//					if(read(i,"(")) {
//						
//					}
				default:	//
					
				}
			}
			wIndex++;
		}
		
	}
	private void readImports() {
		int n=0;
		for(int i=wIndex;i<tokens.size();i++) {
			Token t = tokens.get(i);
			if(t.type != Token.Type.COMMENT) {
				if("import".equals(t.name)) {
					n=i;
					break;
				}
				else {
					wIndex = i;
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
				wIndex = i;
				return;
			}
		}		
	}
	private void readPackageName() {
		for(int i=wIndex;i<tokens.size();i++) {
			Token t = tokens.get(i);
			if("package".equals(t.name)) {
				Token t2 = tokens.get(i+1);
				Token t3 = tokens.get(i+2);
				if(";".equals(t3.name)) {
					packageName = t2.name;
					wIndex = i + 3;
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
	private void readClassName() {
		modifier = "";
		for(int i=wIndex;i<tokens.size();i++) {
			Token t = tokens.get(i);
			if("class".equals(t.name) || "interface".equals(t.name)) {		//public class a extends b implements c,d,e {
				int n = i+1;
				Token t2 = tokens.get(n++);
				Token t3 = tokens.get(n++);
				if("extends".equals(t3.name)) {
					Token t4 = tokens.get(i+3);
					superClassName = t4.name;
					n++;
				}
				Token t5 = tokens.get(n);
				if("implements".equals(t5.name)) {
					n++;
					while(n < tokens.size()) {
						t5 = tokens.get(n);
						if("{".equals(t5.name)) {	//class code begin
							break;
						}
						else if(",".equals(t5.name)) {
							t5 = tokens.get(++n);
						}
						if(t5.ispunct) {
							pln("error: "+t5.name);
							return;
						}
						implement.add(t5.name);
						n++;
					}
				}
				
				className = t2.name;
				wIndex = n;
				return;
			}
			else if("public".equals(t.name)) {
				modifier = t.name;
				pln(t.name);
			}
			else if("private".equals(t.name)) {
				modifier = t.name;
				pln(t.name);				
			}
			else if("abstract".equals(t.name)) {
				abstractClass = true;
			}
			else {
				pln("unkown error");
				return;
			}
		}
		//note found
		String path = parser.srcPath.replace('\'', '/');
		int l = path.lastIndexOf('/');
		int r = path.lastIndexOf('.');
		if(l <= 0)l = 0; else l++;
		if(r <= l) r = path.length();
		className = path.substring(l, r);
	}
	
	private boolean read(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	enum ExpectStatus{
		NONE,
		PACKAGE_NAME, 
		CLASS_NAME, 
		SUPER_NAME, //extends(super), 
		IMPLEMENTS_NAME, 
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
