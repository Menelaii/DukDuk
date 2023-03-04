package com.example.dripchip.services;

import com.example.dripchip.SearchCriterias.LocationPointSearchCriteria;
import com.example.dripchip.entities.LocationPoint;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.LocationPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationPointsService {
    private final LocationPointsRepository repository;
    private final LocationPointsCriteriaRepository criteriaRepository;

    @Autowired
    public LocationPointsService(LocationPointsRepository repository) {
        this.repository = repository;
    }


    public LocationPoint findOne(long id){
        Optional<LocationPoint> locationPoint = repository.findById(id);

        if (locationPoint.isEmpty()){
            throw new EntityNotFoundException();
        }

        return locationPoint.get();
    }


    public Object findOne(Long id, LocationPointSearchCriteria searchCriteria) {
        Optional<LocationPoint> locationPoint = repository.findById(id);

        if (locationPoint.isEmpty()){
            throw new EntityNotFoundException();
        }

        return locationPoint.get();
    }
}
