package com.example.dripchip.services;

import com.example.dripchip.entities.Animal;
import com.example.dripchip.entities.AnimalVisitedLocation;
import com.example.dripchip.entities.LocationPoint;
import com.example.dripchip.exceptions.EntityNotFoundException;
import com.example.dripchip.exceptions.InvalidEntityException;
import com.example.dripchip.repositories.AnimalVisitedLocationRepository;
import com.example.dripchip.repositories.AnimalVisitedLocationsCriteriaRepository;
import com.example.dripchip.repositories.AnimalsRepository;
import com.example.dripchip.repositories.LocationPointsRepository;
import com.example.dripchip.searchCriterias.AnimalVisitedLocationSearchCriteria;
import com.example.dripchip.searchCriterias.XPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.dripchip.utils.Constants.DEAD_LIFE_STATUS;

@Service
@Transactional(readOnly = true)
public class AnimalVisitedLocationsService {
    private final AnimalVisitedLocationRepository visitedLocationRepository;
    private final LocationPointsRepository locationPointsRepository;
    private final AnimalsRepository animalsRepository;
    private final AnimalVisitedLocationsCriteriaRepository criteriaRepository;

    @Autowired
    public AnimalVisitedLocationsService(AnimalVisitedLocationRepository repository,
                                         LocationPointsRepository locationPointsRepository, AnimalsRepository animalsRepository, AnimalVisitedLocationsCriteriaRepository criteriaRepository) {
        this.visitedLocationRepository = repository;
        this.locationPointsRepository = locationPointsRepository;
        this.animalsRepository = animalsRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public List<AnimalVisitedLocation> findWithFilters(XPage page,
                                                       AnimalVisitedLocationSearchCriteria searchCriteria,
                                                       Long animalId) {

        if (!visitedLocationRepository.existsById(animalId)) {
            throw new EntityNotFoundException();
        }

        return criteriaRepository.findWithFilters(page, searchCriteria, animalId);
    }

    @Transactional
    public AnimalVisitedLocation save(Long animalId, Long pointId) {
        Optional<Animal> animalContainer = animalsRepository.findById(animalId);
        Animal animal = animalContainer.orElseThrow(EntityNotFoundException::new);

        Optional<LocationPoint> pointContainer = locationPointsRepository.findById(pointId);
        LocationPoint newPoint = pointContainer.orElseThrow(EntityNotFoundException::new);

        List<AnimalVisitedLocation> visitedLocations = animal.getVisitedLocations();
        LocationPoint lastVisitedLocation = getLastVisitedLocation(visitedLocations);

        if (animal.getLifeStatus().equals(DEAD_LIFE_STATUS)
                || animal.getChippingLocation().equals(newPoint)
                || (lastVisitedLocation != null && lastVisitedLocation.equals(newPoint))) {
            throw new InvalidEntityException();
        }

        AnimalVisitedLocation entity = new AnimalVisitedLocation(
                LocalDateTime.now(),
                animal,
                newPoint
        );

        return visitedLocationRepository.save(entity);
    }

    @Transactional
    public AnimalVisitedLocation update(Long animalId, AnimalVisitedLocation updatedEntity) {
        AnimalVisitedLocation savedEntity = visitedLocationRepository.findById(updatedEntity.getId())
                .orElseThrow(EntityNotFoundException::new);

        Animal animal = animalsRepository.findById(animalId)
                .orElseThrow(EntityNotFoundException::new);

        LocationPoint locationPoint = locationPointsRepository.findById(updatedEntity.getLocationPoint().getId())
                .orElseThrow(EntityNotFoundException::new);

        if (!animal.getVisitedLocations().contains(savedEntity)) {
            throw new EntityNotFoundException();
        }
        List<AnimalVisitedLocation> visitedLocations = animal.getVisitedLocations();
        AnimalVisitedLocation firstPoint = visitedLocations.get(0);

        if (firstPoint.getLocationPoint().equals(animal.getChippingLocation())
                || locationPoint.equals(savedEntity.getLocationPoint())
                || pointsNextOrBeforeIsEqual(visitedLocations, updatedEntity)) {
            throw new InvalidEntityException();
        }

        return visitedLocationRepository.save(updatedEntity);
    }

    @Transactional
    public void delete(Long animalId, Long visitedPointId) {
        Animal animal = animalsRepository.findById(animalId)
                .orElseThrow(EntityNotFoundException::new);

        AnimalVisitedLocation entityToDelete = visitedLocationRepository.findById(visitedPointId)
                .orElseThrow(EntityNotFoundException::new);

        List<AnimalVisitedLocation> visitedLocations = animal.getVisitedLocations();

        if (!visitedLocations.contains(entityToDelete)) {
            throw new EntityNotFoundException();
        }

        AnimalVisitedLocation firstPoint = visitedLocations.get(0);
        AnimalVisitedLocation secondPoint = visitedLocations.size() >= 2
                ? visitedLocations.get(1)
                : null;

        if (firstPoint.equals(entityToDelete)
                && secondPoint != null
                && secondPoint.getLocationPoint().equals(animal.getChippingLocation())) {
            visitedLocationRepository.deleteById(secondPoint.getId());
        }

        visitedLocationRepository.deleteById(visitedPointId);
    }

    private LocationPoint getLastVisitedLocation(List<AnimalVisitedLocation> visitedLocations) {
        return visitedLocations.isEmpty() ? null : visitedLocations.get(visitedLocations.size() - 1)
                .getLocationPoint();
    }

    private boolean pointsNextOrBeforeIsEqual(List<AnimalVisitedLocation> visitedLocations, AnimalVisitedLocation location) {
        int index = visitedLocations.indexOf(location);
        LocationPoint next = null;
        LocationPoint previous = null;

        if (visitedLocations.size() > index + 1) next = visitedLocations.get(index + 1).getLocationPoint();
        if (index - 1  >= 0) previous = visitedLocations.get(index - 1).getLocationPoint();

        return (next != null && next.equals(location.getLocationPoint()))
                || (previous != null && previous.equals(location.getLocationPoint()));
    }
}
