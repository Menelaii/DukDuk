package com.example.dripchip.repositories;

import com.example.dripchip.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalsRepository extends JpaRepository<Animal, Long> {
}
