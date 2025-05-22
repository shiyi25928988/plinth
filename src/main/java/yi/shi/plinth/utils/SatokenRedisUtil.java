package yi.shi.plinth.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.time.Duration;
import java.util.List;

public class SatokenRedisUtil {

    public static List<String> allKeys() {
        return getRedisCommands().keys("*");
    }

    public static Long getExpiration(String key){
        return getRedisCommands().ttl(key);
    }

    public static void set(String key, String value, Duration expiration){
        getRedisCommands().set(key, value, SetArgs.Builder.ex(expiration));
    }

    public static void set(String key, String value){
        getRedisCommands().set(key, value);
    }

    public static String get(String key){
        return getRedisCommands().get(key);
    }

    public static void del(String key){
        getRedisCommands().del(key);
    }

    public static void updateExpiration(String key, Duration expiration){
        getRedisCommands().expire(key, expiration);
    }

    public static boolean exists(String key){
        return getRedisCommands().exists(key) == 1;
    }

    public static void update(String key, String value, Duration expiration){
        getRedisCommands().set(key, value, SetArgs.Builder.ex(expiration));
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
