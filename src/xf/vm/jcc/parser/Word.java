package xf.vm.jcc.parser;

public class Word {
	//Parser parser;
	Type type;			//variable, immediate number, operand, class, method, type(int, long, string), struct, enum, ;
	int codeIndex;
	int lineNum;
	int lineCodeIndex;
	boolean ispunct;
	String name;
	String value;
	
	public enum Type{
		NONE,
		COMMENT,
		OPERAND,
		POLICY,	//public/private
		VARIABLE,
		IMM_NUM,
		TYPE,
		
	}
	
}
