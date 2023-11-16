package concurrency.service;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockPessimisticLockService {

    private final StockRepository stockRepository;

    public StockPessimisticLockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Transactional
    public void decrease(Long id) {
        Stock stock = stockRepository.findByIdWithPessimisticLock(id);
        stock.decrease();

        stockRepository.save(stock);
    }
    // pessimisticLock(비관적 락)의 경우 충돌이 빈번하다면 optimisticLock(낙관적 락)보다 더 좋을 수 있음
    // 데이터 정합성이 완전히 보장됨
    // 허나 데드락의 위험성이 존재함
}
