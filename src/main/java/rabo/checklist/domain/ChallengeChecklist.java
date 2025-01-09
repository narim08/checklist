package rabo.checklist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "challenge_checklist")
@Getter @Setter
public class ChallengeChecklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean task1Completed = false;

    @Column(nullable = false)
    private boolean task2Completed = false;

    @Column(nullable = false)
    private boolean task3Completed = false;

    @Column(nullable = false)
    private boolean allCompleted = false;
}
