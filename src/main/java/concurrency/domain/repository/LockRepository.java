package concurrency.domain.repository;

import concurrency.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<Stock, Long> {

    // 락 획득
    @Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
    void getLock(String key);

    // 락 해제
    @Query(value = "select release_lock(:key)", nativeQuery = true)
    void releaseLock(String key);

}
