package xf.vm.test;

import java.lang.reflect.Field;

import xf.vm.base.Base;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class t2 extends Base{

	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	static @interface Tag{
		String alias() default "";
		int groupFilter() default 0;
		int value();
	}

	@Tag(alias="abc", value = 0)
	int id;
	
	void t(String ... a) {
		Field f;
		try {
			f = t2.class.getDeclaredField("id");
			Tag tag = f.getAnnotation(Tag.class);
			pln("tag="+tag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) {
		t2 t2 = new t2();
		t2.t();
	}
	

}
