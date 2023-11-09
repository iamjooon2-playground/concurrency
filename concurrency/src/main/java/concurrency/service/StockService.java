package concurrency.service;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // @Transactional 사용시 StockService를 새로 만들어서 사용하기 떄문에, synchronized가 적용되지 않음 -> Transaction 사용 못한다!
    // 그리고 synchronized는
    public synchronized void decrease(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease();

        stockRepository.save(stock);
    }

    // synchronized의 단점이 있음
    // 각 프로세스 안에서만 보장이 되어서, 여러 스레드에서 동시에 접근할 수 있음
    // 실서비스는 WAS가 두 대 이상이기 때문에 synhronized는 거의 사용하지 않음
}
