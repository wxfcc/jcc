/*
java c complier
author: WuXuefei
*/
package xf.vm.jcc;

import xf.vm.base.Base;

public class Jcc extends Base{

	public static void main(String[] args) {
    	new Jcc(args).main();
    	pln("done");
    }
    public Jcc(String[] args) {
	}

	private void main() {
    	String root = ".";
    	String path = root + "/doc/test/t1.jc";
//    	path = root+"/src/xf/jcc/Jcc.java";
    	try {
    		Parser p = new Parser(this, path);
    		
    		p.parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
