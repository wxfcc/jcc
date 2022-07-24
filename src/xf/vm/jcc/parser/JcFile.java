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

	int expectStatus;		// expect package, class, extends(super), implements, class code begin, variable/method, "(){};:", while, case, break, #endif 

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
	List<Word> words;
	public void run() {
		words = parser.words;
		readPackageName();
		readImports();
		readClassName();
		pln("package: "+packageName);
		for(String cn:imports)pln("import: "+cn);
		pln(modifier + " class: "+className+", super: "+ superClassName);
		for(String cn:implement)pln("implements: "+cn);
		pln(words.get(wIndex).name);
		
		while(wIndex < words.size()) {
			Word w = words.get(wIndex);
//			pln("line "+ w.lineNum+", "	+(w.codeIndex -	w.lineCodeIndex+1)+": "+ w.name);

			if(w.ispunct) {
				
			}
			else {
				switch(w.name) {
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
		for(int i=wIndex;i<words.size();i++) {
			Word w = words.get(i);
			if(w.type != Word.Type.COMMENT) {
				if("import".equals(w.name)) {
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
		for(int i=n;i<words.size();i++) {
			Word w = words.get(i);
			if("import".equals(w.name)) {
				Word w2 = words.get(i+1);
				Word w3 = words.get(i+2);
				String cn = w2.name;
				if("*".equals(w3.name)) {
					cn += w3.name;
					w3 = words.get(i+3);
					i++;
				}
				if(";".equals(w3.name)) {
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
		for(int i=wIndex;i<words.size();i++) {
			Word w = words.get(i);
			if("package".equals(w.name)) {
				Word w2 = words.get(i+1);
				Word w3 = words.get(i+2);
				if(";".equals(w3.name)) {
					packageName = w2.name;
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
		for(int i=wIndex;i<words.size();i++) {
			Word w = words.get(i);
			if("class".equals(w.name) || "interface".equals(w.name)) {		//public class a extends b implements c,d,e {
				int n = i+1;
				Word w2 = words.get(n++);
				Word w3 = words.get(n++);
				if("extends".equals(w3.name)) {
					Word w4 = words.get(i+3);
					superClassName = w4.name;
					n++;
				}
				Word w5 = words.get(n);
				if("implements".equals(w5.name)) {
					n++;
					while(n < words.size()) {
						w5 = words.get(n);
						if("{".equals(w5.name)) {	//class code begin
							break;
						}
						else if(",".equals(w5.name)) {
							w5 = words.get(++n);
						}
						if(w5.ispunct) {
							pln("error: "+w5.name);
							return;
						}
						implement.add(w5.name);
						n++;
					}
				}
				
				className = w2.name;
				wIndex = n;
				return;
			}
			else if("public".equals(w.name)) {
				modifier = w.name;
				pln(w.name);
			}
			else if("private".equals(w.name)) {
				modifier = w.name;
				pln(w.name);				
			}
			else if("abstract".equals(w.name)) {
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

}
