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
@Table(name = "supplies")
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Supply name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Supply type is required")
    @Column(nullable = false, length = 50)
    private String type; // Food, Water, Blankets, Medicines, Clothing, Other

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(nullable = false)
    private Integer quantity; // Current available inventory stock

    @NotBlank(message = "Unit is required")
    @Column(nullable = false, length = 30)
    private String unit; // Packets, Litres, Pieces, Boxes, Kgs

    @Column(name = "received_date", nullable = false)
    private LocalDate receivedDate = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;

    @OneToMany(mappedBy = "supply", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Distribution> distributions = new ArrayList<>();

    public Supply() {
    }

    public Supply(String name, String type, Integer quantity, String unit, LocalDate receivedDate, Camp camp) {
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
        this.receivedDate = receivedDate != null ? receivedDate : LocalDate.now();
        this.camp = camp;
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
