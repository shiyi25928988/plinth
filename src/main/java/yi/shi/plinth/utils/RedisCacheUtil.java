package yi.shi.plinth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;
import yi.shi.plinth.annotation.cache.RedisCache;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Objects;

@Slf4j
public class RedisCacheUtil {

    public static Object retrive(Object obj, Method method, Object...args){
        Object result = null;
        if(method.isAnnotationPresent(RedisCache.class)){
            RedisCache cache = method.getAnnotation(RedisCache.class);
            String key = cache.name().concat("#").concat(MD5Util.md5(args));
            Duration expiration = Duration.of(cache.expire(), cache.timeUnit());
            if(cache.name().length()>0){
                result = get(key, method.getReturnType());
                if(Objects.isNull(result)){
                    try {
                        result = method.invoke(obj, args);
                        set(key, result, expiration);
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                       log.error(e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    public static void set(String key, Object value, Duration expiration){
        try {
            getRedisCommands().set(key, JsonUtils.toJson(value), SetArgs.Builder.ex(expiration));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object get(String key, Class<T> classType){
        String _value = getRedisCommands().get(key);
        try {
            return JsonUtils.fromJson(_value.getBytes(StandardCharsets.UTF_8), classType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void del(String key){
        getRedisCommands().del(key);
    }

    private static RedisCommands<String, String> getRedisCommands(){
        return getConnection().sync();
    }

    private static StatefulRedisConnection getConnection(){
        return getRedisClient().connect();
    }

    private static RedisClient getRedisClient(){
        return RedisClient.create(RedisUtil.getRedisURI(0));
    }

}
