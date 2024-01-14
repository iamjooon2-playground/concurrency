package concurrency.service;

import concurrency.domain.Stock;
import concurrency.domain.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockSynchronizedService {

    private final StockRepository stockRepository;

    public StockSynchronizedService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    // @Transactional 사용시 StockService를 새로 만들어서 사용하기 떄문에, synchronized가 적용되지 않음(대충 AOP 이야기) -> Transaction 사용 못한다!@ㅆㄱ
    public synchronized void decrease(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease();

        stockRepository.save(stock);
    }

    // synchronized의 단점
    // 각 프로세스 안에서만 보장이 됨.
    // 트랜잭션이 보장되지 않음
    // 현업에서는 대부분 WAS가 두 대 이상이기 때문에, synhronized는 거의 사용하지 않음

}
