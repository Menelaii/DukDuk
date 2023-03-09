package com.example.dripchip.services;

import com.example.dripchip.entities.AnimalType;
import com.example.dripchip.exceptions.EntityAlreadyExistsException;
import com.example.dripchip.exceptions.EntityConnectedException;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.exceptions.InvalidEntityException;
import com.example.dripchip.repositories.AnimalTypesRepository;
import com.example.dripchip.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AnimalTypesService {
    private final AnimalTypesRepository repository;

    @Autowired
    public AnimalTypesService(AnimalTypesRepository animalsRepository) {
        this.repository = animalsRepository;
    }

    public AnimalType findOne(long id) {
        Optional<AnimalType> animal = repository.findById(id);

        if(animal.isEmpty()) {
            throw new EntityNotFoundException();
        }

        return animal.get();
    }

    @Transactional
    public AnimalType save(AnimalType animalType){

        throwIfInvalid(animalType.getType());

        throwIfAlreadyExistsByType(animalType);

        return repository.save(animalType);
    }

    @Transactional
    public AnimalType update(Long id, AnimalType animalType) {
        Validator.throwIfInvalidId(id);

        throwIfInvalid(animalType.getType());

        throwIfExists(id);

        throwIfAlreadyExistsByType(animalType);

        animalType.setId(id);
        return repository.save(animalType);
    }

    @Transactional
    public void delete(Long id){
        Validator.throwIfInvalidId(id);

        Optional<AnimalType> animalTypeContainer = repository.findById(id);
        if (animalTypeContainer.isEmpty()){
            throw  new EntityNotFoundException();
        }

        if (!animalTypeContainer.get().getAnimals().isEmpty()) {
            throw new EntityConnectedException();
        }

        repository.deleteById(id);
    }

    private void throwIfExists(Long id) {
        if(repository.existsById(id)){
            throw new EntityNotFoundException();
        }
    }

    private void throwIfAlreadyExistsByType(AnimalType animalType) {
        if (repository.findByType(animalType.getType()).isPresent()){
            throw new EntityAlreadyExistsException();
        }
    }

    private void throwIfInvalid(String type) {
        if(Validator.isBlankOrEmpty(type)){
            throw new InvalidEntityException();
        }
    }
}
