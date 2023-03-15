package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalTypeDTO;
import com.example.dripchip.dto.AnimalTypeShortDTO;
import com.example.dripchip.entities.AnimalType;
import com.example.dripchip.services.AnimalTypesService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals/types")
public class AnimalTypesController {
    private final AnimalTypesService service;
    private final ModelMapper modelMapper;

    @Autowired
    public AnimalTypesController(AnimalTypesService service,
                                 ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalTypeDTO> findOne(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(service.findOne(id), AnimalTypeDTO.class));
    }

    @PostMapping
    public ResponseEntity<AnimalTypeDTO> save(@RequestBody AnimalTypeShortDTO animalTypeShortDTO){
        AnimalType animalType = service.save(modelMapper.map(animalTypeShortDTO,
                AnimalType.class));

        return new ResponseEntity<>(modelMapper.map(animalType, AnimalTypeDTO.class),
                HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalTypeDTO> update(@PathVariable("id") Long id,
                                   @RequestBody AnimalTypeShortDTO shortDTO) {
        System.out.println("id : " + id);
        AnimalType animalType = service.update(id, modelMapper.map(shortDTO,
                AnimalType.class));

        return ResponseEntity.ok(modelMapper.map(animalType, AnimalTypeDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
