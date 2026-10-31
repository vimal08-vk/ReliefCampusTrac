package com.example.reliefcamp.service;

import com.example.reliefcamp.dto.FamilyRequest;
import com.example.reliefcamp.dto.FamilyResponse;
import com.example.reliefcamp.model.Family;

import java.util.List;

public interface FamilyService {

    FamilyResponse createFamily(FamilyRequest request);

    List<FamilyResponse> getAllFamilies();

    List<FamilyResponse> getFamiliesByCamp(Long campId);

    FamilyResponse getFamilyById(Long id);

    FamilyResponse updateFamily(Long id, FamilyRequest request);

    void deleteFamily(Long id);

    Family getFamilyEntityById(Long id);
}
