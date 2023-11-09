package concurrency.service;

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
class StockServiceTest {

    @Autowired
    private StockService stockService;

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
        stockService.decrease(id, 1L);

        // when
        Stock stock = stockRepository.findById(id).orElseThrow();

        // then
        assertThat(stock.getQuantity()).isEqualTo(99L);
    }

    @AfterEach
    void after() {
        stockRepository.deleteAll();
    }

}
