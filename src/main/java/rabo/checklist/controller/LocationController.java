package rabo.checklist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rabo.checklist.service.ChallengeService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/{date}")
    public ResponseEntity<?> addLocation(@PathVariable String date,
                                         @RequestBody Map<String, Double> location) {
        date = date.replace("\"", "");
        LocalDate parseDate = LocalDate.parse(date);
        challengeService.addLocation(parseDate, location.get("latitude"), location.get("longitude"));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<?> removeLocation(@PathVariable Long locationId) {
        challengeService.removeLocation(locationId);
        return ResponseEntity.ok().build();
    }
}
