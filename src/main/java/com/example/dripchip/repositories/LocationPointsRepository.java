package com.example.dripchip.repositories;

import com.example.dripchip.entity.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPointsRepository extends JpaRepository<LocationPoint, Long> {
}