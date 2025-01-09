package rabo.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rabo.checklist.domain.ChallengeChecklist;

import java.time.LocalDate;

public interface ChallengeChecklistRepository extends JpaRepository<ChallengeChecklist, Long> {
    ChallengeChecklist findByDate(LocalDate date);
}
