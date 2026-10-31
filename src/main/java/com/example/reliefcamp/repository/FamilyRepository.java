package com.example.reliefcamp.repository;

import com.example.reliefcamp.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {

    List<Family> findByCampId(Long campId);

    List<Family> findByStatus(String status);

    List<Family> findByCampIdAndStatus(Long campId, String status);

    List<Family> findTop5ByOrderByIdDesc();

    @Query("SELECT COALESCE(SUM(f.memberCount), 0) FROM Family f WHERE f.camp.id = :campId AND f.status = 'CHECKED_IN'")
    long getTotalMembersInCamp(@Param("campId") Long campId);

    @Query("SELECT COALESCE(SUM(f.memberCount), 0) FROM Family f WHERE f.status = 'CHECKED_IN'")
    long getTotalActiveMembers();
}
