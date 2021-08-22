package uz.depos.app.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Company;

/**
 * Spring Data JPA repository for the {@link Company} entity.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    String COMPANIES_BY_NAME_CACHE = "companiesByName";

    String COMPANIES_BY_EMAIL_CACHE = "companiesByEmail";

    Page<Company> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<Company> findAllByOrderByCreatedDateAsc(Pageable pageable);

    //    @Cacheable(cacheNames = COMPANIES_BY_NAME_CACHE)
    Optional<Company> findOneByName(String name);

    //    @Cacheable(cacheNames = COMPANIES_BY_EMAIL_CACHE)
    Optional<Company> findOneByEmailIgnoreCase(String email);

    Optional<Company> findOneByInn(String inn);
}
