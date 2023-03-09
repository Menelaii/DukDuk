package com.example.dripchip.controllers;

import com.example.dripchip.dto.AnimalVisitedLocationDTO;
import com.example.dripchip.dto.AnimalVisitedLocationShortDTO;
import com.example.dripchip.entities.AnimalVisitedLocation;
import com.example.dripchip.searchCriterias.AnimalVisitedLocationSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import com.example.dripchip.services.AnimalVisitedLocationsService;
import com.example.dripchip.mappers.AnimalVisitedLocationsMapper;
import com.example.dripchip.validators.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/animals/{animalId}/locations")
public class AnimalVisitedLocationsController {
    private final AnimalVisitedLocationsMapper visitedLocationsMapper;
    private final AnimalVisitedLocationsService service;

    @Autowired
    public AnimalVisitedLocationsController(AnimalVisitedLocationsMapper visitedLocationsMapper, AnimalVisitedLocationsService animalVisitedLocationsService) {
        this.visitedLocationsMapper = visitedLocationsMapper;
        this.service = animalVisitedLocationsService;
    }

    @GetMapping
    public ResponseEntity<List<AnimalVisitedLocationDTO>> findOne(@PathVariable("animalId") Long animalId,
                                                                  AnimalVisitedLocationSearchCriteria searchCriteria,
                                                                  XPage page) {
        //TODO проверка дат на соответствию стандарту
        if (animalId == null || animalId <= 0 || page.getFrom() < 0 || page.getSize() <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        page.setSortBy("dateTimeOfVisitLocationPoint");

        List<AnimalVisitedLocation> visitedLocations =
                service.findWithFilters(page, searchCriteria, animalId);
        List<AnimalVisitedLocationDTO> dtos = visitedLocations
                .stream()
                .map(visitedLocationsMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{pointId}")
    public ResponseEntity<AnimalVisitedLocationDTO> save(@PathVariable("animalId") Long animalId,
                                 @PathVariable("pointId") Long pointId) {
        Validator.throwIfInvalidId(animalId);
        Validator.throwIfInvalidId(pointId);

        AnimalVisitedLocation entity = service.save(animalId, pointId);
        return ResponseEntity.ok(visitedLocationsMapper.toDto(entity));
    }

    @PutMapping
    public ResponseEntity<AnimalVisitedLocationDTO> update(@PathVariable("animalId") Long animalId,
                                                                 @RequestBody AnimalVisitedLocationShortDTO shortDTO) {
        Validator.throwIfInvalidId(animalId);
        Validator.throwIfInvalidId(shortDTO.getId());
        Validator.throwIfInvalidId(shortDTO.getLocationPointId());

        AnimalVisitedLocation updated = service.update(animalId, visitedLocationsMapper.toEntity(shortDTO));
        return ResponseEntity.ok(visitedLocationsMapper.toDto(updated));
    }


    @DeleteMapping("/{visitedPointId}")
    public ResponseEntity<Void> delete(@PathVariable("animalId") Long animalId,
                                       @PathVariable("visitedPointId") Long visitedPointId) {
        Validator.throwIfInvalidId(animalId);
        Validator.throwIfInvalidId(visitedPointId);

        service.delete(animalId, visitedPointId);

        return ResponseEntity.ok().build();
    }
}
