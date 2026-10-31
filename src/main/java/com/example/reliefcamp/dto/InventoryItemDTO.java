package com.example.reliefcamp.dto;

import com.example.reliefcamp.model.Supply;

public class InventoryItemDTO {

    private Long supplyId;
    private String supplyName;
    private String type;
    private Integer availableQuantity;
    private String unit;
    private Long campId;
    private String campName;
    private String status; // AVAILABLE, LOW_STOCK, OUT_OF_STOCK

    public InventoryItemDTO() {
    }

    public static InventoryItemDTO fromEntity(Supply s) {
        InventoryItemDTO dto = new InventoryItemDTO();
        dto.setSupplyId(s.getId());
        dto.setSupplyName(s.getName());
        dto.setType(s.getType());
        dto.setAvailableQuantity(s.getQuantity());
        dto.setUnit(s.getUnit());

        if (s.getCamp() != null) {
            dto.setCampId(s.getCamp().getId());
            dto.setCampName(s.getCamp().getName());
        }

        if (s.getQuantity() <= 0) {
            dto.setStatus("OUT_OF_STOCK");
        } else if (s.getQuantity() < 50) {
            dto.setStatus("LOW_STOCK");
        } else {
            dto.setStatus("AVAILABLE");
        }

        return dto;
    }

    public Long getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Long supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
