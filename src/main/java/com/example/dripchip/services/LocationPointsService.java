package com.example.dripchip.services;

import com.example.dripchip.entities.LocationPoint;
import com.example.dripchip.exceptions.EntityConnectedException;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.exceptions.InvalidEntityException;
import com.example.dripchip.exceptions.EntityAlreadyExistsException;
import com.example.dripchip.repositories.LocationPointsRepository;
import com.example.dripchip.validators.LocationPointValidator;
import com.example.dripchip.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationPointsService {
    private final LocationPointsRepository repository;

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

    @Transactional
    public LocationPoint save(LocationPoint locationPoint){
        if (LocationPointValidator.isInvalid(locationPoint)){
            throw  new InvalidEntityException();
        }

        throwIfAlreadyExists(locationPoint);

        return repository.save(locationPoint);
    }

    public LocationPoint update(Long id, LocationPoint locationPoint){
        Validator.throwIfInvalidId(id);

        if(LocationPointValidator.isInvalid(locationPoint)){
            throw new InvalidEntityException();
        }
        
        if (!repository.existsById(id)){
            throw new EntityNotFoundException();
        }

        throwIfAlreadyExists(locationPoint);

        locationPoint.setId(id);
        return repository.save(locationPoint);
    }

    public void delete(Long id){
        Validator.throwIfInvalidId(id);

        Optional<LocationPoint> locationPointContainer = repository.findById(id);
        if (locationPointContainer.isEmpty()){
            throw  new EntityNotFoundException();
        }

        if (!locationPointContainer.get().getAnimals().isEmpty()) {
            throw new EntityConnectedException();
        }

        repository.deleteById(id);
    }


    private void throwIfAlreadyExists(LocationPoint locationPoint) {
        Optional<LocationPoint> locationPointContainer = repository
                .findByLatitudeAndLongitude(locationPoint.getLatitude(),
                        locationPoint.getLongitude());

        if (locationPointContainer.isPresent()){
            throw new EntityAlreadyExistsException();
        }
    }


}
