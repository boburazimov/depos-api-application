package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.City;

/**
 * Spring Data JPA repository for the {@link City} entity.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {}
