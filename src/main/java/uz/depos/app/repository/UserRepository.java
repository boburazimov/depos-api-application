package uz.depos.app.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.depos.app.domain.User;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    String USERS_BY_PINFL_CACHE = "usersByPinfl";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    Optional<User> findOneByLoginIgnoreCase(String login);

    Optional<User> findOneByInn(String inn);

    Optional<User> findOneByPassportIgnoreCase(String passport);

    Optional<User> findOneByPinfl(String pinfl);

    Optional<User> findOneByPhoneNumber(String number);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_PINFL_CACHE)
    Optional<User> findOneWithAuthoritiesByPinfl(String pinfl);

    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    @Query(
        value = "select * from jhi_user as u where lower(concat(u.full_name, u.pinfl))  like lower(concat('%', :searchValue, '%'))",
        nativeQuery = true
    )
    Optional<List<User>> findByFullNameOrPinfl(@Param("searchValue") String searchValue);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    List<User> findAll(Specification<User> specification);
}
