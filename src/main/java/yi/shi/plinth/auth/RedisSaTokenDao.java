package yi.shi.plinth.auth;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.util.SaFoxUtil;
import tools.jackson.core.JacksonException;
import yi.shi.plinth.utils.JsonUtils;
import yi.shi.plinth.utils.SatokenRedisUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

public class RedisSaTokenDao implements SaTokenDao {
    @Override
    public String get(String key) {
        return SatokenRedisUtil.get(key);
    }

    @Override
    public void set(String key, String value, long timeout) {
        if(timeout == -1){
            SatokenRedisUtil.set(key, value);
            return;
        }else if(timeout == 0 || timeout < -2){
            return;
        }
        SatokenRedisUtil.set(key, value, Duration.ofSeconds(timeout));
    }

    @Override
    public void update(String key, String value) {
        this.set(key, value, -1);
    }

    @Override
    public void delete(String key) {
        SatokenRedisUtil.del(key);
    }

    @Override
    public long getTimeout(String key) {
        Long expiration = SatokenRedisUtil.getExpiration(key);
        if(expiration == -1) {
            return Long.MAX_VALUE;
        }else if(expiration == -2) {
            return 0;
        }
        return expiration;
    }

    @Override
    public void updateTimeout(String key, long timeout) {
        SatokenRedisUtil.updateExpiration(key, Duration.ofSeconds(timeout));
    }

    @Override
    public Object getObject(String key) {
        if(!SatokenRedisUtil.exists(key)) {
            return null;
        }
        return SatokenRedisUtil.get(key);
    }

    @Override
    public <T> T getObject(String key, Class<T> classType) {
        if(!SatokenRedisUtil.exists(key)) {
            return null;
        }
        String value = SatokenRedisUtil.get(key);
        try {
            return JsonUtils.fromJson(value.getBytes(StandardCharsets.UTF_8), classType);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setObject(String key, Object object, long timeout) {
        SatokenRedisUtil.set(key, JsonUtils.toJson(object), Duration.ofSeconds(timeout));
    }

    @Override
    public void updateObject(String key, Object object) {
        Long expire = SatokenRedisUtil.getExpiration(key);
        if(expire > 0 ){
            SatokenRedisUtil.update(key, JsonUtils.toJson(object), Duration.ofSeconds(expire));
        }else if(expire == -1){
            SatokenRedisUtil.set(key, JsonUtils.toJson(object));
        }
    }

    @Override
    public void deleteObject(String key) {
        SatokenRedisUtil.del(key);
    }

    @Override
    public long getObjectTimeout(String key) {
        Long expire = SatokenRedisUtil.getExpiration(key);
        if(expire == -1) {
            return Long.MAX_VALUE;
        }else if(expire == -2) {
            return 0;
        }else{
            return expire;
        }
    }

    @Override
    public void updateObjectTimeout(String key, long timeout) {
        SatokenRedisUtil.updateExpiration(key, Duration.ofSeconds(timeout));
    }

    @Override
    public SaSession getSession(String sessionId) {
        return null;
    }

    @Override
    public void setSession(SaSession session, long timeout) {
        SatokenRedisUtil.set(session.getId(), JsonUtils.toJson(session), Duration.ofSeconds(timeout));
    }

    @Override
    public void updateSession(SaSession session) {
        if(SatokenRedisUtil.exists(session.getId())){
            Long expire = SatokenRedisUtil.getExpiration(session.getId());
            SatokenRedisUtil.update(session.getId(), JsonUtils.toJson(session), Duration.ofSeconds(expire));
        }
    }

    @Override
    public void deleteSession(String sessionId) {
        SatokenRedisUtil.del(sessionId);
    }

    @Override
    public long getSessionTimeout(String sessionId) {
        Long  expire = SatokenRedisUtil.getExpiration(sessionId);
        if(expire == -1) {
            return Long.MAX_VALUE;
        }else if(expire == -2) {
            return 0;
        }else{
            return expire;
        }
    }

    @Override
    public void updateSessionTimeout(String sessionId, long timeout) {
        SatokenRedisUtil.updateExpiration(sessionId, Duration.ofSeconds(timeout));
    }

    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        return SaFoxUtil.searchList(SatokenRedisUtil.allKeys(), prefix, keyword, start, size, sortType);
    }
}
