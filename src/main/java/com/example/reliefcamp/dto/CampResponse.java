package com.example.reliefcamp.dto;

import com.example.reliefcamp.model.Camp;
import java.time.LocalDateTime;

public class CampResponse {

    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private Integer currentOccupancy;
    private Integer availableCapacity;
    private String status;
    private LocalDateTime createdAt;

    public CampResponse() {
    }

    public static CampResponse fromEntity(Camp camp) {
        CampResponse response = new CampResponse();
        response.setId(camp.getId());
        response.setName(camp.getName());
        response.setLocation(camp.getLocation());
        response.setCapacity(camp.getCapacity());
        int occupancy = camp.getCurrentOccupancy() != null ? camp.getCurrentOccupancy() : 0;
        response.setCurrentOccupancy(occupancy);
        response.setAvailableCapacity(Math.max(0, camp.getCapacity() - occupancy));
        response.setStatus(camp.getStatus());
        response.setCreatedAt(camp.getCreatedAt());
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(Integer currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public Integer getAvailableCapacity() {
        return availableCapacity;
    }

    public void setAvailableCapacity(Integer availableCapacity) {
        this.availableCapacity = availableCapacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
