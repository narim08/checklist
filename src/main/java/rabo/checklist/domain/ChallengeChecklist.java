package rabo.checklist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Location> locations = new ArrayList<>();

    public void addLocation(Location location) {
        locations.add(location);
        location.setChecklist(this);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
        location.setChecklist(null);
    }
}
