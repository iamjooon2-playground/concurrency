package concurrency.facade;

import java.util.concurrent.TimeUnit;

import concurrency.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
public class RedisRedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final StockService stockService;

    public RedisRedissonLockStockFacade(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long id) {
        RLock lock = redissonClient.getLock(id.toString());

        try {
            boolean isAvailable = lock.tryLock(10, 1, TimeUnit.SECONDS);
            System.out.println(isAvailable);
            if (!isAvailable) {
                System.out.println("lock 획득 실패");
                return;
            }

            stockService.decrease(id);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    // Redisson을 활용한 pub-sub을 통해 부하를 줄여준다는 장점이 있음
    // 다만 구현이 조금 복잡하다
    // 또한 별도의 라이브러리를 의존성 추가해줘야함
}
