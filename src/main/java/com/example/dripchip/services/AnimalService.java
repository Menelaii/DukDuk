package com.example.dripchip.services;

import com.example.dripchip.entity.Animal;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.repositories.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AnimalService {
    private final AnimalsRepository repository;

    @Autowired
    public AnimalService(AnimalsRepository animalsRepository) {
        this.repository = animalsRepository;
    }

    public Animal findOne(long id) {
        Optional<Animal> animal = repository.findById(id);

        if(animal.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return animal.get();
    }
}
