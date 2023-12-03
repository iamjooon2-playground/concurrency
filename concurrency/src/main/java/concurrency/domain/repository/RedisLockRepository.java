package concurrency.domain.repository;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisLockRepository {

    private static final String LOCK = "lock";

    private final RedisTemplate<String, String> redisTemplate;

    public RedisLockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean lock(Long key) {
        return redisTemplate.opsForValue()
                .setIfAbsent(key.toString(), LOCK, Duration.ofSeconds(3));
    }

    public void unLock(Long key) {
        redisTemplate.delete(key.toString());
    }
}
