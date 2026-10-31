package com.example.reliefcamp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SupplyRequest {

    @NotBlank(message = "Supply name is required")
    private String name;

    @NotBlank(message = "Supply type is required")
    private String type;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotNull(message = "Camp ID is required")
    private Long campId;

    private LocalDate receivedDate;

    public SupplyRequest() {
    }

    public SupplyRequest(String name, String type, Integer quantity, String unit, Long campId, LocalDate receivedDate) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
        this.campId = campId;
        this.receivedDate = receivedDate;
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

    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
    }

    public LocalDate getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDate receivedDate) {
        this.receivedDate = receivedDate;
    }
}
