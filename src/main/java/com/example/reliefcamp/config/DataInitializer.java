package com.example.reliefcamp.config;

import com.example.reliefcamp.model.Camp;
import com.example.reliefcamp.model.Distribution;
import com.example.reliefcamp.model.Family;
import com.example.reliefcamp.model.Supply;
import com.example.reliefcamp.repository.CampRepository;
import com.example.reliefcamp.repository.DistributionRepository;
import com.example.reliefcamp.repository.FamilyRepository;
import com.example.reliefcamp.repository.SupplyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CampRepository campRepository;
    private final SupplyRepository supplyRepository;
    private final FamilyRepository familyRepository;
    private final DistributionRepository distributionRepository;

    public DataInitializer(CampRepository campRepository,
                           SupplyRepository supplyRepository,
                           FamilyRepository familyRepository,
                           DistributionRepository distributionRepository) {
        this.campRepository = campRepository;
        this.supplyRepository = supplyRepository;
        this.familyRepository = familyRepository;
        this.distributionRepository = distributionRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        try {
            if (campRepository.count() > 0) {
                return;
            }

            // 1. Initial Relief Camps
        Camp camp1 = new Camp("Pollachi Relief Camp", "Pollachi, Coimbatore", 100, "ACTIVE");
        Camp camp2 = new Camp("Valparai Hill Camp", "Valparai, Anamalai", 60, "ACTIVE");
        Camp camp3 = new Camp("Mettupalayam Center", "Mettupalayam Town Hall", 80, "ACTIVE");

        campRepository.save(camp1);
        campRepository.save(camp2);
        campRepository.save(camp3);

        // 2. Incoming Relief Supplies
        Supply s1 = new Supply("Food Packets", "Food", 500, "Packets", LocalDate.now(), camp1);
        Supply s2 = new Supply("Mineral Water Bottles", "Water", 1000, "Litres", LocalDate.now(), camp1);
        Supply s3 = new Supply("Thermal Blankets", "Blankets", 200, "Pieces", LocalDate.now(), camp1);
        Supply s4 = new Supply("First Aid & Paracetamol Kits", "Medicines", 75, "Boxes", LocalDate.now(), camp1);

        Supply s5 = new Supply("Food Packets", "Food", 300, "Packets", LocalDate.now(), camp2);
        Supply s6 = new Supply("Warm Blankets", "Blankets", 150, "Pieces", LocalDate.now(), camp2);

        supplyRepository.save(s1);
        supplyRepository.save(s2);
        supplyRepository.save(s3);
        supplyRepository.save(s4);
        supplyRepository.save(s5);
        supplyRepository.save(s6);

        // 3. Registered Affected Families
        Family f1 = new Family("Kumar", 5, "9876543210", LocalDate.now(), "CHECKED_IN", camp1);
        Family f2 = new Family("Priya", 4, "9876543211", LocalDate.now(), "CHECKED_IN", camp1);
        Family f3 = new Family("Ramesh", 3, "9876543212", LocalDate.now(), "CHECKED_IN", camp2);

        familyRepository.save(f1);
        familyRepository.save(f2);
        familyRepository.save(f3);

        // Update Camp Occupancy
        camp1.setCurrentOccupancy(9); // 5 + 4
        camp2.setCurrentOccupancy(3);
        campRepository.save(camp1);
        campRepository.save(camp2);

        // 4. Initial Distributions (matches specification example: 500 - 20 = 480)
        s1.setQuantity(s1.getQuantity() - 20);
        supplyRepository.save(s1);
        Distribution d1 = new Distribution(camp1, f1, s1, 20, LocalDate.now(), "Initial emergency meal kit");
        distributionRepository.save(d1);

        s2.setQuantity(s2.getQuantity() - 10);
        supplyRepository.save(s2);
        Distribution d2 = new Distribution(camp1, f1, s2, 10, LocalDate.now(), "Daily drinking water ration");
        distributionRepository.save(d2);

        s3.setQuantity(s3.getQuantity() - 4);
        supplyRepository.save(s3);
        Distribution d3 = new Distribution(camp1, f2, s3, 4, LocalDate.now(), "Warm bedding assistance");
        distributionRepository.save(d3);
        } catch (Exception e) {
            System.err.println("Notice: DataInitializer skipped or encountered existing data: " + e.getMessage());
        }
    }
}
