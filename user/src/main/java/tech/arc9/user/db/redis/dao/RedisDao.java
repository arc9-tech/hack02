package tech.arc9.user.db.redis.dao;

import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RedisDao {

    Logger LOG = LoggerFactory.getLogger(RedisDao.class);
    @Autowired private RedissonClient redissonClient;
    private final Integer MAX_TTL_MIN = 30;
    public void set(String key, Object value) {
        try {
            redissonClient.getBucket(key).set(value, MAX_TTL_MIN, TimeUnit.MINUTES);
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO SAVE DATA, {}", ex.getMessage());
        }
    }

    public void set(String key, Object value, Integer ttlMinutes) {
        try {
            redissonClient.getBucket(key).set(value, ttlMinutes, TimeUnit.MINUTES);
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO SAVE DATA, {}", ex.getMessage());
        }
    }

    public void update(String key, Object value) {
        try {
            redissonClient.getBucket(key).set(value, MAX_TTL_MIN, TimeUnit.MINUTES);
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO SAVE DATA, {}", ex.getMessage());
        }
    }

    public void update(String key, Object value, Integer ttlMinutes) {
        try {
            redissonClient.getBucket(key).set(value, ttlMinutes, TimeUnit.MINUTES);
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO SAVE DATA, {}", ex.getMessage());
        }
    }

    public Object get(String key) {
        try {
            return redissonClient.getBucket(key).get();
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO FETCH VALUE, {}", ex.getMessage());
        }
        return null;
    }

    public void remove(String key) {
        try {
            redissonClient.getBucket(key).delete();
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO REMOVE KEY, {}", ex.getMessage());
        }
    }
    public void removeByKeyPrefix(String keyPrefix) {
        try {
            RKeys keys = redissonClient.getKeys();
            keys.deleteByPattern(keyPrefix);
        } catch (Exception ex) {
            LOG.warn("REDIS FAILED TO REMOVE KEY, {}", ex.getMessage());
        }
    }
}
