package com.example.reliefcamp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "distributions")
public class Distribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "family_id", nullable = false)
    private Family family;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supply_id", nullable = false)
    private Supply supply;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "distribution_date", nullable = false)
    private LocalDate distributionDate = LocalDate.now();

    @Column(length = 255)
    private String remarks;

    @Column(nullable = false, length = 30)
    private String status = "COMPLETED";

    public Distribution() {
    }

    public Distribution(Camp camp, Family family, Supply supply, Integer quantity, LocalDate distributionDate, String remarks) {
        this.camp = camp;
        this.family = family;
        this.supply = supply;
        this.quantity = quantity;
        this.distributionDate = distributionDate != null ? distributionDate : LocalDate.now();
        this.remarks = remarks;
        this.status = "COMPLETED";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Camp getCamp() {
        return camp;
    }

    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public Supply getSupply() {
        return supply;
    }

    public void setSupply(Supply supply) {
        this.supply = supply;
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
