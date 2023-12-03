package concurrency.facade;

import concurrency.domain.repository.RedisLockRepository;
import concurrency.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class RedisLettuceLockStockFacade {

    private final RedisLockRepository redisLockRepository;
    private final StockService stockService;

    public RedisLettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long id) throws InterruptedException {
        while (!redisLockRepository.lock(id)) {
            Thread.sleep(100);
        }
        try {
            stockService.decrease(id);
        } finally {
            redisLockRepository.unLock(id);
        }
    }
}
