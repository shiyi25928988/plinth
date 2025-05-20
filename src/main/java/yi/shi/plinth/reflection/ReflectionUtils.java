package yi.shi.plinth.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import yi.shi.plinth.annotation.cache.ApiCache;
import yi.shi.plinth.utils.MD5Util;

/**
 * @author yshi
 *
 */
@Slf4j
public final class ReflectionUtils {

	private static Cache<Object, @Nullable Object> cache =
			Caffeine.newBuilder().initialCapacity(10).maximumSize(1024).expireAfterWrite(10, TimeUnit.MINUTES).recordStats().build();
		//CacheBuilder.newBuilder().recordStats().maximumSize(1024).expireAfterAccess(10, TimeUnit.MINUTES).build();

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
		if(method.isAnnotationPresent(ApiCache.class)){
			ApiCache controllerCache = method.getAnnotation(ApiCache.class);
			String key = controllerCache.name().concat("#").concat(MD5Util.md5(args));

			if(controllerCache.name().length()>0){
				result = cache.getIfPresent(key);
				if(Objects.isNull(result)){
					try {
						result = method.invoke(obj, args);
						cache.put(key, result);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						log.error(e.getMessage());
						throw new Exception(e);
					}
				}
				return result;
			}
		}else {
			try {
				result = method.invoke(obj, args);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.error(e.getMessage());
				throw new Exception(e);
			}
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
