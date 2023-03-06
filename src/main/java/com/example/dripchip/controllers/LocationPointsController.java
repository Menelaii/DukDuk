package com.example.dripchip.controllers;

import com.example.dripchip.dto.LocationPointShortDTO;
import com.example.dripchip.dto.LocationPointDTO;
import com.example.dripchip.entities.LocationPoint;
import com.example.dripchip.services.LocationPointsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
public class LocationPointsController {
    private final LocationPointsService locationPointsService;
    private final ModelMapper modelMapper;


    @Autowired
    public LocationPointsController(LocationPointsService locationPointsService, ModelMapper modelMapper) {
        this.locationPointsService = locationPointsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationPointDTO> findOne(@PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(locationPointsService.findOne(id), LocationPointDTO.class));
    }

    @PostMapping
    public ResponseEntity<LocationPointDTO> create(@RequestBody LocationPointShortDTO locationPointShortDTO) {
        LocationPoint locationPoint = locationPointsService.save(modelMapper.map(locationPointShortDTO,
                LocationPoint.class));
        return new ResponseEntity<>(modelMapper.map(locationPoint, LocationPointDTO.class),
                HttpStatusCode.valueOf(201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationPointDTO> update(@PathVariable("id") Long id, @RequestBody LocationPointShortDTO locationPointShortDTO) {
        LocationPoint locationPoint = locationPointsService.update(id, (modelMapper.map(locationPointShortDTO,
                LocationPoint.class)));

        return new ResponseEntity<>(modelMapper.map(locationPoint, LocationPointDTO.class),
                HttpStatusCode.valueOf(409));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        locationPointsService.delete(id);
        return ResponseEntity.ok().build();
    }

}
