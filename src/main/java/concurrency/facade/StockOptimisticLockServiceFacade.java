package concurrency.facade;

import concurrency.service.StockOptimisticLockService;
import org.springframework.stereotype.Service;

@Service
public class StockOptimisticLockServiceFacade {

    private final StockOptimisticLockService stockOptimisticLockService;

    public StockOptimisticLockServiceFacade(StockOptimisticLockService stockOptimisticLockService) {
        this.stockOptimisticLockService = stockOptimisticLockService;
    }

    public void decrease(Long id) {
        while (true) {
            try {
                stockOptimisticLockService.decrease(id);

                break;
            } catch (Exception e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    // 낙관적 락의 장점
    // 별도의 락을 잡지 않아 비관적 락보다 성능상 이점이 있음

    // 단점으로는 업데이트 실패시 재시도 로직을 개발자가 직접 처리해줘야함
    // 충돌이 빈번하게 날 것이라고 예상된다면 낙관적 락을 추천함

}
