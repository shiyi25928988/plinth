package yi.shi.plinth.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import yi.shi.plinth.annotation.cache.LocalCache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LocalCacheUtil {
    private static Cache<Object, @Nullable Object> cache =
            Caffeine.newBuilder().initialCapacity(10).maximumSize(1024).expireAfterWrite(10, TimeUnit.MINUTES).recordStats().build();

    public static void put(Object key, Object value) {
        cache.put(key, value);
    }
    public static @Nullable Object get(Object key) {
        return cache.getIfPresent(key);
    }

    public static Object retrive(Object obj, Method method, Object...args){
        Object result = null;
        if(method.isAnnotationPresent(LocalCache.class)){
            LocalCache cache = method.getAnnotation(LocalCache.class);
            String key = cache.name().concat("#").concat(MD5Util.md5(args));
            if(cache.name().length()>0){
                result = get(key);
                if(Objects.isNull(result)){
                    try {
                        result = method.invoke(obj, args);
                        put(key, result);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        return result;
    }
}
