package uz.depos.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Page<Company> findAllByOrderByCreatedDateDesc(Pageable pageable);

    Page<Company> findAllByOrderByCreatedDateAsc(Pageable pageable);
}
