/*
java c complier
author:	WuXuefei
*/
package	xf.vm.jcc;

import xf.vm.base.Base;
import xf.vm.jcc.parser.JcFile;
import xf.vm.jcc.parser.Parser;

public class Jcc extends Base{

	public static void main(String[] args) {
		new	Jcc(args).main();
		pln("done");
	}
	public Jcc(String[]	args) {
	}

	private	void main()	{
		String root	= ".";
		String path	= root + "/doc/test/t2.jc";
//		path = root+"/src/xf/vm/jcc/Jcc.java";
		try	{
			JcFile jc = new JcFile(this, path);
			
			jc.parse();
			jc.dumpTokens();
			jc.run();
		} catch	(Exception e) {
			e.printStackTrace();
		}
		
	}
}
