package uz.depos.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.depos.app.domain.Agenda;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {}
