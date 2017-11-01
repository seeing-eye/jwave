package org.jwave.wtf;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * @author e.petrov. Created 11 - 2017.
 */
public class FieldUtil {

	public static Predicate<Field> serializeable() {
		return f -> Modifier.isStatic(f.getModifiers());
	}



	private FieldUtil() {
	}

}
