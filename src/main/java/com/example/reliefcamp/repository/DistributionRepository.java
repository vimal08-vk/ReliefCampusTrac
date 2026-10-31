package com.example.reliefcamp.repository;

import com.example.reliefcamp.model.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistributionRepository extends JpaRepository<Distribution, Long> {

    List<Distribution> findByCampId(Long campId);

    List<Distribution> findByFamilyId(Long familyId);

    List<Distribution> findBySupplyId(Long supplyId);

    List<Distribution> findTop5ByOrderByIdDesc();

    @Query("SELECT COALESCE(SUM(d.quantity), 0) FROM Distribution d")
    long getTotalQuantityDistributed();
}
