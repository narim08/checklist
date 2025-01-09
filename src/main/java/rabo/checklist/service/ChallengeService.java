package rabo.checklist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rabo.checklist.domain.ChallengeChecklist;
import rabo.checklist.repository.ChallengeChecklistRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChallengeService {
    @Autowired
    private ChallengeChecklistRepository repository;

    public List<ChallengeChecklist> getAllChallenges() {
        return repository.findAll();
    }

    public ChallengeChecklist getChallengeByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    @Transactional
    public void updateChecklist(LocalDate date, boolean task1, boolean task2, boolean task3) {
        ChallengeChecklist checklist = repository.findByDate(date);
        if (checklist != null) {
            checklist.setTask1Completed(task1);
            checklist.setTask2Completed(task2);
            checklist.setTask3Completed(task3);
            checklist.setAllCompleted(task1 && task2 && task3);
            repository.save(checklist);
        }
    }
}
