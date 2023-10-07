package yi.shi.plinth.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

/**
 * @author yshi
 *
 */
@Slf4j
public final class ReflectionUtils {

	/**
	 * @param clazz
	 * @return
	 * @throws Exception 
	 */
	public static Object newInstance(Class<?> clazz) throws Exception {
		Object instance = null;
		Constructor<?> constructor = clazz.getConstructor();
		try {
			instance = constructor.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error(e.toString());
			throw new Exception(e);
		}
		return instance;
	}
	
	/**
	 * @param obj
	 * @param method
	 * @param args
	 * @return
	 * @throws Exception 
	 */
	public static Object invokeMethod(Object obj, Method method, Object...args) throws Exception {
		Object result = null;
		method.setAccessible(true);
		try {
			result = method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.error(e.getMessage());
			throw new Exception(e);
		}
		return result;
	}
	
	/**
	 * Used to inject the target Object to the field.
	 * @param obj
	 * @param field
	 * @param value
	 * @throws Exception 
	 */
	public static void setField(Object obj, Field field, Object value) throws Exception {
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e.getMessage());
			throw new Exception(e);
		}
	}
}
