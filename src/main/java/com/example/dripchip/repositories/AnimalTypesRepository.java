package com.example.dripchip.repositories;

import com.example.dripchip.entities.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalTypesRepository extends JpaRepository<AnimalType, Long> {

    Optional<AnimalType> findByType(String type);

}
