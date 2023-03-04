package com.example.dripchip.controllers;

import com.example.dripchip.dto.LocationPointDTO;
import com.example.dripchip.services.LocationPointsService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
public class LocationPointsController {

    private final LocationPointsService locationPointsService;
    private final ModelMapper modelMapper;

    public LocationPointsController(LocationPointsService locationPointsService, ModelMapper modelMapper) {
        this.locationPointsService = locationPointsService;
        this.modelMapper = modelMapper;
    }




    @GetMapping("/{id}")
    public ResponseEntity<LocationPointDTO> findOne(@PathVariable("id") Long id) {
        if( id == null || id <= 0){
            return  new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }

        return ResponseEntity.ok(modelMapper.map(locationPointsService.findOne(id), LocationPointDTO.class));
    }
}
