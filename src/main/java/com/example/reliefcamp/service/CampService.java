package com.example.reliefcamp.service;

import com.example.reliefcamp.dto.CampRequest;
import com.example.reliefcamp.dto.CampResponse;
import com.example.reliefcamp.model.Camp;

import java.util.List;

public interface CampService {

    CampResponse createCamp(CampRequest request);

    List<CampResponse> getAllCamps();

    CampResponse getCampById(Long id);

    CampResponse updateCamp(Long id, CampRequest request);

    void deleteCamp(Long id);

    Camp getCampEntityById(Long id);
}
