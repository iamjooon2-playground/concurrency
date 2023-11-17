package concurrency.service;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockOptimisticLockService {

    private final StockRepository stockRepository;

    public StockOptimisticLockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id) {
        Stock stock = stockRepository.findByIdWithOptimisticLock(id);
        stock.decrease();

        stockRepository.save(stock);
    }

}
