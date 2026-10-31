package com.example.reliefcamp.dto;

import com.example.reliefcamp.model.Supply;
import java.time.LocalDate;

public class SupplyResponse {

    private Long id;
    private String name;
    private String type;
    private Integer quantity;
    private String unit;
    private LocalDate receivedDate;
    private Long campId;
    private String campName;
    private String stockStatus; // OUT_OF_STOCK, LOW_STOCK, AVAILABLE

    public SupplyResponse() {
    }

    public static SupplyResponse fromEntity(Supply supply) {
        SupplyResponse response = new SupplyResponse();
        response.setId(supply.getId());
        response.setName(supply.getName());
        response.setType(supply.getType());
        response.setQuantity(supply.getQuantity());
        response.setUnit(supply.getUnit());
        response.setReceivedDate(supply.getReceivedDate());

        if (supply.getCamp() != null) {
            response.setCampId(supply.getCamp().getId());
            response.setCampName(supply.getCamp().getName());
        }

        if (supply.getQuantity() <= 0) {
            response.setStockStatus("OUT_OF_STOCK");
        } else if (supply.getQuantity() < 50) {
            response.setStockStatus("LOW_STOCK");
        } else {
            response.setStockStatus("AVAILABLE");
        }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
    }

    public String getCampName() {
        return campName;
    }

    public void setCampName(String campName) {
        this.campName = campName;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }
}
