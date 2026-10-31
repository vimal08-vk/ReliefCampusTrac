package com.example.reliefcamp;

import com.example.reliefcamp.dto.*;
import com.example.reliefcamp.exception.CampCapacityExceededException;
import com.example.reliefcamp.exception.InsufficientInventoryException;
import com.example.reliefcamp.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReliefCampApplicationTests {

    @Autowired
    private CampService campService;

    @Autowired
    private SupplyService supplyService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private DistributionService distributionService;

    @Autowired
    private DashboardService dashboardService;

    @Test
    @DisplayName("Context loads and initial data is seeded")
    void contextLoads() {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        assertNotNull(summary);
        assertTrue(summary.getTotalCamps() >= 3, "Initial camps should be seeded");
        assertTrue(summary.getTotalStockQuantity() > 0, "Initial supplies should be seeded");
    }

    @Test
    @DisplayName("Rule 2: Camp capacity limit must be enforced")
    void testCampCapacityRule() {
        // Create a small test camp with capacity = 10
        CampRequest campReq = new CampRequest("Test Overflow Camp", "Test Location", 10, "ACTIVE");
        CampResponse camp = campService.createCamp(campReq);

        // Register a family of 8 members (valid)
        FamilyRequest f1 = new FamilyRequest("Rao", 8, "9998887771", camp.getId(), null, "CHECKED_IN");
        FamilyResponse savedF1 = familyService.createFamily(f1);
        assertNotNull(savedF1);

        // Try to register another family with 4 members (8 + 4 = 12 > 10 capacity)
        FamilyRequest f2 = new FamilyRequest("Sharma", 4, "9998887772", camp.getId(), null, "CHECKED_IN");
        assertThrows(CampCapacityExceededException.class, () -> familyService.createFamily(f2),
                "Should reject registration when camp capacity is exceeded");
    }

    @Test
    @DisplayName("Rule 1: Distribution must reduce stock and reject over-distribution")
    void testInventoryAndDistributionRules() {
        // Create a camp
        CampRequest campReq = new CampRequest("Inventory Test Camp", "Zone B", 50, "ACTIVE");
        CampResponse camp = campService.createCamp(campReq);

        // Add 50 units of supply
        SupplyRequest supplyReq = new SupplyRequest("Blankets", "Blankets", 50, "Pieces", camp.getId(), null);
        SupplyResponse supply = supplyService.createSupply(supplyReq);
        assertEquals(50, supply.getQuantity());

        // Register a family
        FamilyRequest famReq = new FamilyRequest("Patel", 3, "9998887773", camp.getId(), null, "CHECKED_IN");
        FamilyResponse family = familyService.createFamily(famReq);

        // Distribute 20 units (50 - 20 = 30)
        DistributionRequest distReq = new DistributionRequest(camp.getId(), family.getId(), supply.getId(), 20, "Test ration");
        DistributionResponse dist = distributionService.createDistribution(distReq);
        assertNotNull(dist);

        // Check updated stock
        SupplyResponse updatedSupply = supplyService.getSupplyById(supply.getId());
        assertEquals(30, updatedSupply.getQuantity(), "Inventory stock should be reduced by 20");

        // Attempt to distribute 40 units (which exceeds available stock of 30)
        DistributionRequest invalidDistReq = new DistributionRequest(camp.getId(), family.getId(), supply.getId(), 40, "Excess ration");
        assertThrows(InsufficientInventoryException.class, () -> distributionService.createDistribution(invalidDistReq),
                "Should reject distribution when stock is insufficient");
    }
}
