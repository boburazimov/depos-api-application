package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    //    @Transactional
    //    @Modifying
    //    @Query(value = "delete from attachment where id in (select logo_id from company where id=:companyId)", nativeQuery = true)
    //    void deleteByCompany(@Param("companyId") UUID companyId);
}
