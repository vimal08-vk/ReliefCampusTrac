package com.example.reliefcamp.repository;

import com.example.reliefcamp.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    List<Supply> findByCampId(Long campId);

    List<Supply> findByType(String type);

    List<Supply> findByCampIdAndType(Long campId, String type);

    List<Supply> findByQuantityLessThanEqual(Integer threshold);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Supply s")
    long getTotalAvailableStock();

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Supply s WHERE s.camp.id = :campId")
    long getTotalAvailableStockByCamp(@Param("campId") Long campId);

    long countByQuantityLessThanEqual(Integer threshold);
}
