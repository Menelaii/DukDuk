package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalTypeDTO;
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
    public AnimalTypesController(AnimalTypesService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalTypeDTO> findOne(@PathVariable("id") Integer id) {
        if( id == null || id <= 0){
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(service.findOne(id), AnimalTypeDTO.class));
    }
}
