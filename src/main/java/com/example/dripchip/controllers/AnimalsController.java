package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.dto.AnimalShortDTO;
import com.example.dripchip.dto.AnimalTypeUpdateDTO;
import com.example.dripchip.entities.Animal;
import com.example.dripchip.searchCriterias.AnimalSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.services.AnimalService;
import com.example.dripchip.mappers.AnimalMapper;
import com.example.dripchip.validators.AnimalValidator;
import com.example.dripchip.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/animals")
public class AnimalsController {
    private final AnimalService service;
    private final AnimalMapper mapper;

    @Autowired
    public AnimalsController(AnimalService animalService, AnimalMapper animalMapper) {
        this.service = animalService;
        this.mapper = animalMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> findOne(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(mapper.toDto(service.findOne(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnimalDTO>> search(AnimalSearchCriteria searchCriteria, XPage page) {
        if (page.getFrom() == null) page.setFrom(0);
        if (page.getSize() == null) page.setSize(10);

        if (page.getFrom() < 0
                || page.getSize() <= 0
                || (searchCriteria.getGender() != null && !AnimalValidator.isValidGender(searchCriteria.getGender()))
                || (searchCriteria.getLifeStatus() != null && !AnimalValidator.isValidLifeStatus(searchCriteria.getLifeStatus()))) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        List<Animal> animals = service.findWithFilters(page, searchCriteria);
        List<AnimalDTO> animalDTOS = animals
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(animalDTOS);
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> save(@RequestBody AnimalShortDTO shortDTO) {
        if (!AnimalValidator.isValid(shortDTO)) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        if (Validator.hasDuplicates(shortDTO.getAnimalTypes())) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(409));
        }

        Animal entity = service.save(mapper.toEntity(shortDTO));
        return new ResponseEntity<>(mapper.toDto(entity), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> update(@PathVariable("id") Long id, @RequestBody AnimalShortDTO shortDTO) {
        Validator.throwIfInvalidId(id);

        if (!AnimalValidator.isValid(shortDTO)) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        Animal entity = mapper.toEntity(shortDTO);
        return ResponseEntity.ok(mapper.toDto(service.update(id, entity)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Validator.throwIfInvalidId(id);
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDTO> addType(@PathVariable("animalId") Long animalId,
                                        @PathVariable("typeId") Long typeId) {
        Validator.throwIfInvalidId(animalId);
        Validator.throwIfInvalidId(typeId);

        Animal updated = service.addType(animalId, typeId);
        return new ResponseEntity<>(mapper.toDto(updated), HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{animalId}/types")
    public ResponseEntity<AnimalDTO> updateType(@PathVariable("animalId") Long animalId,
                                             @RequestBody AnimalTypeUpdateDTO dto) {
        Validator.throwIfInvalidId(animalId);
        Validator.throwIfInvalidId(dto.getNewTypeId());
        Validator.throwIfInvalidId(dto.getOldTypeId());

        Animal updated = service.updateType(animalId, dto.getOldTypeId(), dto.getNewTypeId());
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{animalId}/types/{typeId}")
    public ResponseEntity<AnimalDTO> deleteType(@PathVariable("animalId") Long animalId,
                                           @PathVariable("typeId") Long typeId) {
        Validator.throwIfInvalidId(animalId);
        Validator.throwIfInvalidId(typeId);

        Animal updatedEntity = service.deleteType(animalId, typeId);

        return ResponseEntity.ok(mapper.toDto(updatedEntity));
    }
}
