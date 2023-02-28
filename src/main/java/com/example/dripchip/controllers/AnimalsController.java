package com.example.dripchip.controllers;

import com.example.dripchip.SearchCriterias.LocationPointSearchCriteria;
import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.dto.AnimalVisitedLocationDTO;
import com.example.dripchip.services.AnimalService;
import com.example.dripchip.services.AnimalVisitedLocationService;
import com.example.dripchip.services.LocationPointsService;
import com.example.dripchip.utils.AnimalMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/animals")
public class AnimalsController {
    private final AnimalService animalService;
    private final LocationPointsService locationPointsService;
    private final AnimalMapper animalMapper;
    private final AnimalVisitedLocationService animalVisitedLocationService;
    private final ModelMapper modelMapper;

    @Autowired
    public AnimalsController(AnimalService animalService,
                             LocationPointsService locationPointsService, AnimalMapper animalMapper,
                             AnimalVisitedLocationService animalVisitedLocationService,
                             ModelMapper modelMapper) {
        this.animalService = animalService;
        this.locationPointsService = locationPointsService;
        this.animalMapper = animalMapper;
        this.animalVisitedLocationService = animalVisitedLocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> findOne(@PathVariable("id") Integer id) {
        //todo 401 неверные авторизационные данные
        if( id == null || id <= 0){
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(animalMapper.toDto(animalService.findOne(id)));
    }

    @GetMapping("/{id}/locations")
    public ResponseEntity<AnimalVisitedLocationDTO> findOne(@PathVariable("id") Long id,
                                                            LocationPointSearchCriteria searchCriteria ){
        //TODO проверка дат на соответствию стандарту
        if (id == null || id <= 0
                || searchCriteria.getFrom() < 0
                || searchCriteria.getSize() <= 0){
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(locationPointsService.findOne(id, searchCriteria), AnimalVisitedLocationDTO.class));
    }
}
