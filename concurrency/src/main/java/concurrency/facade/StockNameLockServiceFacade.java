package concurrency.facade;

import concurrency.domain.repository.LockRepository;
import concurrency.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockNameLockServiceFacade {

    private final LockRepository lockRepository;
    private final StockService stockService;

    public StockNameLockServiceFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id) {
        try {
            lockRepository.getLock(id.toString());
            stockService.decrease(id);
        } finally {
            lockRepository.releaseLock(id.toString());
        }
    }

    // 네임드 락은 주로 분산락을 구현할 때 사용한다
    // 비관적 락은 timeout을 구현하기 힘들지만, 네임드 락은 쉽게 구현할 수 있음
    // 이외에도 데이터 삽입시 정합성 맞출 때도 사용 가능

    // 하지만 트랜잭션 종료시 락 해제(세션관리)를 잘 해줘야하기 때문에, 주의해서 사용해야 함 -> 실제로 사용할 떄는 구현방법 어려움
    // MySQL에서는 GET LOCK으로 락 획득 후, release lock 통해 lock 해제 가능
}
