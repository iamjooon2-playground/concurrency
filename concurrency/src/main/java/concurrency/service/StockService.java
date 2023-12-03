package concurrency.service;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease();

        stockRepository.saveAndFlush(stock);
    }
}
