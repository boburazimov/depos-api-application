package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.City;

public interface CityRepository extends JpaRepository<City, Integer> {}
