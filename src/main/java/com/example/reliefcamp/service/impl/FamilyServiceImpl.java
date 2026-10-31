package com.example.reliefcamp.service.impl;

import com.example.reliefcamp.dto.FamilyRequest;
import com.example.reliefcamp.dto.FamilyResponse;
import com.example.reliefcamp.exception.CampCapacityExceededException;
import com.example.reliefcamp.exception.InvalidOperationException;
import com.example.reliefcamp.exception.ResourceNotFoundException;
import com.example.reliefcamp.model.Camp;
import com.example.reliefcamp.model.Family;
import com.example.reliefcamp.repository.CampRepository;
import com.example.reliefcamp.repository.FamilyRepository;
import com.example.reliefcamp.service.CampService;
import com.example.reliefcamp.service.FamilyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FamilyServiceImpl implements FamilyService {

    private final FamilyRepository familyRepository;
    private final CampRepository campRepository;
    private final CampService campService;

    public FamilyServiceImpl(FamilyRepository familyRepository,
                             CampRepository campRepository,
                             CampService campService) {
        this.familyRepository = familyRepository;
        this.campRepository = campRepository;
        this.campService = campService;
    }

    @Override
    public FamilyResponse createFamily(FamilyRequest request) {
        if (request.getMemberCount() == null || request.getMemberCount() <= 0) {
            throw new InvalidOperationException("Member count must be at least 1");
        }

        Camp camp = campService.getCampEntityById(request.getCampId());

        // Capacity Rule Check:
        int currentOccupancy = camp.getCurrentOccupancy() != null ? camp.getCurrentOccupancy() : 0;
        int requestedMembers = request.getMemberCount();

        if (currentOccupancy + requestedMembers > camp.getCapacity()) {
            throw new CampCapacityExceededException("Camp capacity exceeded. Current occupancy: " +
                    currentOccupancy + "/" + camp.getCapacity() +
                    ". Cannot accommodate " + requestedMembers + " additional members.");
        }

        // Update camp occupancy and status
        int newOccupancy = currentOccupancy + requestedMembers;
        camp.setCurrentOccupancy(newOccupancy);
        if (newOccupancy >= camp.getCapacity()) {
            camp.setStatus("FULL");
        }
        campRepository.save(camp);

        // Create Family entity
        Family family = new Family();
        family.setFamilyHead(request.getFamilyHead().trim());
        family.setMemberCount(requestedMembers);
        family.setPhone(request.getPhone() != null ? request.getPhone().trim() : null);
        family.setCheckInDate(request.getCheckInDate() != null ? request.getCheckInDate() : LocalDate.now());
        family.setStatus(request.getStatus() != null && !request.getStatus().isBlank() ? request.getStatus().trim() : "CHECKED_IN");
        family.setCamp(camp);

        Family savedFamily = familyRepository.save(family);
        return FamilyResponse.fromEntity(savedFamily);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FamilyResponse> getAllFamilies() {
        return familyRepository.findAll().stream()
                .map(FamilyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FamilyResponse> getFamiliesByCamp(Long campId) {
        campService.getCampEntityById(campId); // check exists
        return familyRepository.findByCampId(campId).stream()
                .map(FamilyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FamilyResponse getFamilyById(Long id) {
        Family family = getFamilyEntityById(id);
        return FamilyResponse.fromEntity(family);
    }

    @Override
    public FamilyResponse updateFamily(Long id, FamilyRequest request) {
        Family family = getFamilyEntityById(id);

        if (request.getMemberCount() == null || request.getMemberCount() <= 0) {
            throw new InvalidOperationException("Member count must be at least 1");
        }

        Camp originalCamp = family.getCamp();
        Camp targetCamp = originalCamp;

        if (request.getCampId() != null && !request.getCampId().equals(originalCamp.getId())) {
            targetCamp = campService.getCampEntityById(request.getCampId());

            // Remove from original camp
            int origOccupancy = Math.max(0, originalCamp.getCurrentOccupancy() - family.getMemberCount());
            originalCamp.setCurrentOccupancy(origOccupancy);
            if (origOccupancy < originalCamp.getCapacity() && "FULL".equalsIgnoreCase(originalCamp.getStatus())) {
                originalCamp.setStatus("ACTIVE");
            }
            campRepository.save(originalCamp);

            // Add to new camp with capacity check
            int targetOccupancy = targetCamp.getCurrentOccupancy() != null ? targetCamp.getCurrentOccupancy() : 0;
            if (targetOccupancy + request.getMemberCount() > targetCamp.getCapacity()) {
                throw new CampCapacityExceededException("Camp capacity exceeded for destination camp: " +
                        targetCamp.getName() + " (" + targetOccupancy + "/" + targetCamp.getCapacity() + ")");
            }
            int newTargetOccupancy = targetOccupancy + request.getMemberCount();
            targetCamp.setCurrentOccupancy(newTargetOccupancy);
            if (newTargetOccupancy >= targetCamp.getCapacity()) {
                targetCamp.setStatus("FULL");
            }
            campRepository.save(targetCamp);
            family.setCamp(targetCamp);
        } else {
            // Same camp, check member count difference
            int diff = request.getMemberCount() - family.getMemberCount();
            if (diff > 0) {
                int currentOccupancy = originalCamp.getCurrentOccupancy();
                if (currentOccupancy + diff > originalCamp.getCapacity()) {
                    throw new CampCapacityExceededException("Camp capacity exceeded. Cannot increase by " + diff + " members.");
                }
                originalCamp.setCurrentOccupancy(currentOccupancy + diff);
                if (originalCamp.getCurrentOccupancy() >= originalCamp.getCapacity()) {
                    originalCamp.setStatus("FULL");
                }
                campRepository.save(originalCamp);
            } else if (diff < 0) {
                int currentOccupancy = Math.max(0, originalCamp.getCurrentOccupancy() + diff);
                originalCamp.setCurrentOccupancy(currentOccupancy);
                if (currentOccupancy < originalCamp.getCapacity() && "FULL".equalsIgnoreCase(originalCamp.getStatus())) {
                    originalCamp.setStatus("ACTIVE");
                }
                campRepository.save(originalCamp);
            }
        }

        family.setFamilyHead(request.getFamilyHead().trim());
        family.setMemberCount(request.getMemberCount());
        if (request.getPhone() != null) {
            family.setPhone(request.getPhone().trim());
        }
        if (request.getCheckInDate() != null) {
            family.setCheckInDate(request.getCheckInDate());
        }
        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            family.setStatus(request.getStatus().trim());
        }

        Family updatedFamily = familyRepository.save(family);
        return FamilyResponse.fromEntity(updatedFamily);
    }

    @Override
    public void deleteFamily(Long id) {
        Family family = getFamilyEntityById(id);
        Camp camp = family.getCamp();

        if (camp != null && "CHECKED_IN".equalsIgnoreCase(family.getStatus())) {
            int newOccupancy = Math.max(0, camp.getCurrentOccupancy() - family.getMemberCount());
            camp.setCurrentOccupancy(newOccupancy);
            if (newOccupancy < camp.getCapacity() && "FULL".equalsIgnoreCase(camp.getStatus())) {
                camp.setStatus("ACTIVE");
            }
            campRepository.save(camp);
        }

        familyRepository.delete(family);
    }

    @Override
    @Transactional(readOnly = true)
    public Family getFamilyEntityById(Long id) {
        return familyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with ID: " + id));
    }
}
