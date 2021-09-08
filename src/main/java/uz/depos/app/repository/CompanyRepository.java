package uz.depos.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.Company;
import uz.depos.app.service.dto.CompanyByUserDTO;

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

    Optional<List<Company>> findByNameIgnoreCaseContaining(String name);

    Page<Company> findAllByChairmanId(Long chairman_id, Pageable pageable);

    Page<Company> findAllBySecretaryId(Long secretary_id, Pageable pageable);

    @Query(
        value = "select distinct c.* from company as c inner join member as m on m.company_id=c.id where m.user_id=:user_id",
        nativeQuery = true
    )
    List<Company> findCompanyByUser(@Param("user_id") Long user_id);

    //    @Query(value = "select c.id, c.name from Company c where c.chairman.id=:chairman_id or c.secretary.id=:secretary_id")//jpa
    //    List<Company> findAllByChairmanIdOrSecretaryId(@Param("chairman_id") Long chairman_id, @Param("secretary_id") Long secretary_id);

    List<Company> findAllByChairmanIdOrSecretaryId(Long chairman_id, Long secretary_id);
}
