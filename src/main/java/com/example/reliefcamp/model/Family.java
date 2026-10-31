package com.example.reliefcamp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "families")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Family head name is required")
    @Column(name = "family_head", nullable = false, length = 100)
    private String familyHead;

    @NotNull(message = "Member count is required")
    @Min(value = 1, message = "Member count must be at least 1")
    @Column(name = "member_count", nullable = false)
    private Integer memberCount;

    @Column(length = 20)
    private String phone;

    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate = LocalDate.now();

    @Column(nullable = false, length = 30)
    private String status = "CHECKED_IN"; // CHECKED_IN, DISCHARGED

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Distribution> distributions = new ArrayList<>();

    public Family() {
    }

    public Family(String familyHead, Integer memberCount, String phone, LocalDate checkInDate, String status, Camp camp) {
        this.familyHead = familyHead;
        this.memberCount = memberCount;
        this.phone = phone;
        this.checkInDate = checkInDate != null ? checkInDate : LocalDate.now();
        this.status = status != null ? status : "CHECKED_IN";
        this.camp = camp;
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

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public List<Distribution> getDistributions() {
        return distributions;
    }

    public void setDistributions(List<Distribution> distributions) {
        this.distributions = distributions;
    }
}
