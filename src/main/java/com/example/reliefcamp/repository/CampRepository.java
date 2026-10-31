package com.example.reliefcamp.repository;

import com.example.reliefcamp.model.Camp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampRepository extends JpaRepository<Camp, Long> {

    List<Camp> findByStatus(String status);

    List<Camp> findTop5ByOrderByIdDesc();

    @Query("SELECT COALESCE(SUM(c.currentOccupancy), 0) FROM Camp c")
    long getTotalCurrentOccupancy();
}
