package concurrency.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class StockSynchronizedServiceTest {

    @Autowired
    private StockSynchronizedService stockSynchronizedService;

    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void before() {
        stockRepository.saveAndFlush(new Stock(1L, 100L));
    }

    @Test
    void 재고_감소() {
        // given
        Long id = 1L;
        stockSynchronizedService.decrease(id);

        // when
        Stock stock = stockRepository.findById(id).orElseThrow();

        // then
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @Test
    void 동시에_100개의_요청을_받는다() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount); // 다른 쓰레드에서 작업이 완료될 때까지 기다릴 수 있게 해주는 클래스

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockSynchronizedService.decrease(1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // then
        Long quantity = stockRepository.findById(1L).orElseThrow().getQuantity();
        assertThat(quantity).isZero();
    }

}
