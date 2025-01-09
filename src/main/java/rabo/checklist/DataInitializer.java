package rabo.checklist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import rabo.checklist.domain.ChallengeChecklist;
import rabo.checklist.repository.ChallengeChecklistRepository;

import java.time.LocalDate;
import java.util.stream.IntStream;

@Component
public class DataInitializer {
    private final ChallengeChecklistRepository repository;

    public DataInitializer(ChallengeChecklistRepository repository) {
        this.repository = repository;
    }

    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            // 데이터가 이미 있을 경우 초기화를 건너뜁니다.
            if (repository.count() == 0) {
                LocalDate startDate = LocalDate.of(2025, 1, 1);

                IntStream.range(0, 100).forEach(i -> {
                    ChallengeChecklist checklist = new ChallengeChecklist();
                    checklist.setDate(startDate.plusDays(i));
                    repository.save(checklist);
                });

                System.out.println("100일 챌린지가 초기화되었습니다!");
            }
        };
    }
}
