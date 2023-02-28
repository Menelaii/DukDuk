package com.example.dripchip.services;

import com.example.dripchip.entity.AnimalType;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.AnimalTypesRepositotory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AnimalTypesService {
    private final AnimalTypesRepositotory repository;

    @Autowired
    public AnimalTypesService(AnimalTypesRepositotory animalsRepository) {
        this.repository = animalsRepository;
    }

    public AnimalType findOne(long id) {
        Optional<AnimalType> animal = repository.findById(id);

        if(animal.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return animal.get();
    }
}
