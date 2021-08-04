package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {}
