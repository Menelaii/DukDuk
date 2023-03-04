package com.example.dripchip.controllers;

import com.example.dripchip.SearchCriterias.AnimalSearchCriteria;
import com.example.dripchip.SearchCriterias.LocationPointSearchCriteria;
import com.example.dripchip.SearchCriterias.XPage;
import com.example.dripchip.dto.AnimalDTO;
import com.example.dripchip.dto.AnimalVisitedLocationDTO;
import com.example.dripchip.entities.Animal;
import com.example.dripchip.services.AnimalService;
import com.example.dripchip.services.AnimalVisitedLocationService;
import com.example.dripchip.services.LocationPointsService;
import com.example.dripchip.utils.AnimalMapper;
import com.example.dripchip.utils.AnimalValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<AnimalDTO> findOne(@PathVariable("id") Long id) {
        if( id == null || id <= 0){
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(animalMapper.toDto(animalService.findOne(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnimalDTO>> search(AnimalSearchCriteria searchCriteria, XPage page) {
        //todo check dates format iso-8601?
        if (page.getFrom() == null || page.getFrom() < 0
                || page.getSize() == null || page.getSize() <= 0
                || !AnimalValidator.isValidGender(searchCriteria.getGender())
                || !AnimalValidator.isValidLifeStatus(searchCriteria.getLifeStatus())) {
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        List<Animal> animals = animalService.findWithFilters(page, searchCriteria);
        List<AnimalDTO> animalDTOS = animals
                .stream()
                .map(animalMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(animalDTOS);
    }



    @GetMapping("/{id}/locations") //просмотр тчока лок посещенная животным
    public ResponseEntity<AnimalVisitedLocationDTO> findOne(@PathVariable("id") Long id,
                                                            LocationPointSearchCriteria searchCriteria ){
        //TODO проверка дат на соответствию стандарту
        if (id == null || id <= 0 || searchCriteria.getFrom() < 0
                || searchCriteria.getSize() <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(locationPointsService.findOne(id, searchCriteria), AnimalVisitedLocationDTO.class));
    }
}
