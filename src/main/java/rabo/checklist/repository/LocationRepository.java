package rabo.checklist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rabo.checklist.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

}
