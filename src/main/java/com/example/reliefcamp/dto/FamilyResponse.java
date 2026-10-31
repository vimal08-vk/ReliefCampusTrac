package com.example.reliefcamp.dto;

import com.example.reliefcamp.model.Family;
import java.time.LocalDate;

public class FamilyResponse {

    private Long id;
    private String familyHead;
    private Integer memberCount;
    private String phone;
    private LocalDate checkInDate;
    private String status;
    private Long campId;
    private String campName;

    public FamilyResponse() {
    }

    public static FamilyResponse fromEntity(Family family) {
        FamilyResponse response = new FamilyResponse();
        response.setId(family.getId());
        response.setFamilyHead(family.getFamilyHead());
        response.setMemberCount(family.getMemberCount());
        response.setPhone(family.getPhone());
        response.setCheckInDate(family.getCheckInDate());
        response.setStatus(family.getStatus());

        if (family.getCamp() != null) {
            response.setCampId(family.getCamp().getId());
            response.setCampName(family.getCamp().getName());
        }

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFamilyHead() {
        return familyHead;
    }

    public void setFamilyHead(String familyHead) {
        this.familyHead = familyHead;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
