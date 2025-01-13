package rabo.checklist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
@Getter @Setter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double latitude; //위도

    @Column(nullable = false)
    private Double longitude; //경도

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private ChallengeChecklist checklist;
}
