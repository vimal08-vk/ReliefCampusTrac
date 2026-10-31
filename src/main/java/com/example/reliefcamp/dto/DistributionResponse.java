package com.example.reliefcamp.dto;

import com.example.reliefcamp.model.Distribution;
import java.time.LocalDate;

public class DistributionResponse {

    private Long id;
    private Long campId;
    private String campName;
    private Long familyId;
    private String familyHead;
    private Long supplyId;
    private String supplyName;
    private String supplyUnit;
    private Integer quantity;
    private LocalDate distributionDate;
    private String remarks;
    private String status;

    public DistributionResponse() {
    }

    public static DistributionResponse fromEntity(Distribution d) {
        DistributionResponse response = new DistributionResponse();
        response.setId(d.getId());
        response.setQuantity(d.getQuantity());
        response.setDistributionDate(d.getDistributionDate());
        response.setRemarks(d.getRemarks());
        response.setStatus(d.getStatus());

        if (d.getCamp() != null) {
            response.setCampId(d.getCamp().getId());
            response.setCampName(d.getCamp().getName());
        }

        if (d.getFamily() != null) {
            response.setFamilyId(d.getFamily().getId());
            response.setFamilyHead(d.getFamily().getFamilyHead());
        }

        if (d.getSupply() != null) {
            response.setSupplyId(d.getSupply().getId());
            response.setSupplyName(d.getSupply().getName());
            response.setSupplyUnit(d.getSupply().getUnit());
        }

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
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

    public String getSupplyUnit() {
        return supplyUnit;
    }

    public void setSupplyUnit(String supplyUnit) {
        this.supplyUnit = supplyUnit;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getDistributionDate() {
        return distributionDate;
    }

    public void setDistributionDate(LocalDate distributionDate) {
        this.distributionDate = distributionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
