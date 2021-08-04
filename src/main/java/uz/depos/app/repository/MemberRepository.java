package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {}
