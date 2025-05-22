package yi.shi.plinth.utils;

import io.lettuce.core.RedisURI;

import java.time.Duration;

public class RedisUtil {

    public static RedisURI getRedisURI(int database) {
        String host = System.getProperty("redis.host");
        String port = System.getProperty("redis.port", "6379");
        String password = System.getProperty("redis.password", "");
        return getRedisURI(host, Integer.parseInt(port), password, database);
    }

    public static RedisURI getRedisURI(String host, int port, String password, int database){

        if(host == null || host.isEmpty()){
            throw new RuntimeException("redis.host is not set");
        }

        if(password.isEmpty()){
            RedisURI redisUri = RedisURI
                    .Builder
                    .redis(host)
                    .withPort(port)
                    .withDatabase(database)
                    .withTimeout(Duration.ofSeconds(10))
                    .build();
            return redisUri;
        }

        RedisURI redisUri = RedisURI
                .Builder
                .redis(host)
                .withPort(port)
                .withPassword(password)
                .withDatabase(database)
                .withTimeout(Duration.ofSeconds(10))
                .build();
        return redisUri;
    }
}
