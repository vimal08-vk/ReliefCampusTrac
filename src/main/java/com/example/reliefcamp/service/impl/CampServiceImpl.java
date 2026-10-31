package com.example.reliefcamp.service.impl;

import com.example.reliefcamp.dto.CampRequest;
import com.example.reliefcamp.dto.CampResponse;
import com.example.reliefcamp.exception.InvalidOperationException;
import com.example.reliefcamp.exception.ResourceNotFoundException;
import com.example.reliefcamp.model.Camp;
import com.example.reliefcamp.repository.CampRepository;
import com.example.reliefcamp.service.CampService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CampServiceImpl implements CampService {

    private final CampRepository campRepository;

    public CampServiceImpl(CampRepository campRepository) {
        this.campRepository = campRepository;
    }

    @Override
    public CampResponse createCamp(CampRequest request) {
        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new InvalidOperationException("Camp capacity must be a positive number greater than 0");
        }

        Camp camp = new Camp();
        camp.setName(request.getName().trim());
        camp.setLocation(request.getLocation().trim());
        camp.setCapacity(request.getCapacity());
        camp.setCurrentOccupancy(0);
        camp.setStatus(request.getStatus() != null && !request.getStatus().isBlank() ? request.getStatus() : "ACTIVE");

        Camp savedCamp = campRepository.save(camp);
        return CampResponse.fromEntity(savedCamp);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CampResponse> getAllCamps() {
        return campRepository.findAll().stream()
                .map(CampResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CampResponse getCampById(Long id) {
        Camp camp = getCampEntityById(id);
        return CampResponse.fromEntity(camp);
    }

    @Override
    public CampResponse updateCamp(Long id, CampRequest request) {
        Camp camp = getCampEntityById(id);

        if (request.getCapacity() == null || request.getCapacity() <= 0) {
            throw new InvalidOperationException("Camp capacity must be a positive number greater than 0");
        }

        if (request.getCapacity() < camp.getCurrentOccupancy()) {
            throw new InvalidOperationException("New capacity (" + request.getCapacity() +
                    ") cannot be less than current occupancy (" + camp.getCurrentOccupancy() + ")");
        }

        camp.setName(request.getName().trim());
        camp.setLocation(request.getLocation().trim());
        camp.setCapacity(request.getCapacity());

        if (request.getStatus() != null && !request.getStatus().isBlank()) {
            camp.setStatus(request.getStatus().trim());
        } else {
            camp.setStatus(camp.getCurrentOccupancy() >= camp.getCapacity() ? "FULL" : "ACTIVE");
        }

        Camp updatedCamp = campRepository.save(camp);
        return CampResponse.fromEntity(updatedCamp);
    }

    @Override
    public void deleteCamp(Long id) {
        Camp camp = getCampEntityById(id);
        if (camp.getCurrentOccupancy() > 0) {
            throw new InvalidOperationException("Cannot delete camp with active occupancy (" +
                    camp.getCurrentOccupancy() + " members registered). Discharge families first.");
        }
        campRepository.delete(camp);
    }

    @Override
    @Transactional(readOnly = true)
    public Camp getCampEntityById(Long id) {
        return campRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Camp not found with ID: " + id));
    }
}
