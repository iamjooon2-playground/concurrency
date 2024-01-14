package concurrency.facade;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisLettuceLockStockFacadeTest {

    @Autowired
    private RedisLettuceLockStockFacade redisLettuceLockStockFacade;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    redisLettuceLockStockFacade.decrease(1L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Stock stock = stockRepository.findById(1L).orElseThrow();
        assertThat(stock.getQuantity()).isZero();
    }

    // lettuce를 활용한 방식은 구현이 간단하다
    // 하지만 스핀락 방식이므로 레디스에 부하를 줄 수 있음
    // 그래서 파사드에서 작성한 것 처럼, Thread.sleep()을 통해 Lock 획득에 텀을 두어야 함
}