package com.example.reliefcamp.dto;

import java.util.ArrayList;
import java.util.List;

public class DashboardSummaryDTO {

    private long totalCamps;
    private long totalFamilies;
    private long totalPersonsAccommodated;
    private long totalInventoryItems;
    private long totalStockQuantity;
    private long totalDistributions;
    private long lowStockCount;

    private List<CampResponse> recentCamps = new ArrayList<>();
    private List<FamilyResponse> recentFamilies = new ArrayList<>();
    private List<DistributionResponse> recentDistributions = new ArrayList<>();

    public DashboardSummaryDTO() {
    }

    public long getTotalCamps() {
        return totalCamps;
    }

    public void setTotalCamps(long totalCamps) {
        this.totalCamps = totalCamps;
    }

    public long getTotalFamilies() {
        return totalFamilies;
    }

    public void setTotalFamilies(long totalFamilies) {
        this.totalFamilies = totalFamilies;
    }

    public long getTotalPersonsAccommodated() {
        return totalPersonsAccommodated;
    }

    public void setTotalPersonsAccommodated(long totalPersonsAccommodated) {
        this.totalPersonsAccommodated = totalPersonsAccommodated;
    }

    public long getTotalInventoryItems() {
        return totalInventoryItems;
    }

    public void setTotalInventoryItems(long totalInventoryItems) {
        this.totalInventoryItems = totalInventoryItems;
    }

    public long getTotalStockQuantity() {
        return totalStockQuantity;
    }

    public void setTotalStockQuantity(long totalStockQuantity) {
        this.totalStockQuantity = totalStockQuantity;
    }

    public long getTotalDistributions() {
        return totalDistributions;
    }

    public void setTotalDistributions(long totalDistributions) {
        this.totalDistributions = totalDistributions;
    }

    public long getLowStockCount() {
        return lowStockCount;
    }

    public void setLowStockCount(long lowStockCount) {
        this.lowStockCount = lowStockCount;
    }

    public List<CampResponse> getRecentCamps() {
        return recentCamps;
    }

    public void setRecentCamps(List<CampResponse> recentCamps) {
        this.recentCamps = recentCamps;
    }

    public List<FamilyResponse> getRecentFamilies() {
        return recentFamilies;
    }

    public void setRecentFamilies(List<FamilyResponse> recentFamilies) {
        this.recentFamilies = recentFamilies;
    }

    public List<DistributionResponse> getRecentDistributions() {
        return recentDistributions;
    }

    public void setRecentDistributions(List<DistributionResponse> recentDistributions) {
        this.recentDistributions = recentDistributions;
    }
}
