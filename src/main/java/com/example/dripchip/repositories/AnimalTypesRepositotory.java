package com.example.dripchip.repositories;

import com.example.dripchip.entities.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalTypesRepositotory extends JpaRepository<AnimalType, Long> {
}
