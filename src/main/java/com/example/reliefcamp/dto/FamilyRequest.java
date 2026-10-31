package com.example.reliefcamp.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class FamilyRequest {

    @NotBlank(message = "Family head name is required")
    private String familyHead;

    @NotNull(message = "Member count is required")
    @Min(value = 1, message = "Member count must be at least 1")
    private Integer memberCount;

    private String phone;

    @NotNull(message = "Camp ID is required")
    private Long campId;

    private LocalDate checkInDate;

    private String status;

    public FamilyRequest() {
    }

    public FamilyRequest(String familyHead, Integer memberCount, String phone, Long campId, LocalDate checkInDate, String status) {
        this.familyHead = familyHead;
        this.memberCount = memberCount;
        this.phone = phone;
        this.campId = campId;
        this.checkInDate = checkInDate;
        this.status = status;
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

    public Long getCampId() {
        return campId;
    }

    public void setCampId(Long campId) {
        this.campId = campId;
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
}
