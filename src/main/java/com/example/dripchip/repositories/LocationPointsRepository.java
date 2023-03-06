package com.example.dripchip.repositories;

import com.example.dripchip.entities.LocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationPointsRepository extends JpaRepository<LocationPoint, Long> {

    Optional<LocationPoint> findByLatitudeAndLongitude(Double latitude, Double longitude);
}
