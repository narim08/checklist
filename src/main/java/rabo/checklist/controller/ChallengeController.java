package rabo.checklist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rabo.checklist.domain.ChallengeChecklist;
import rabo.checklist.service.ChallengeService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @GetMapping("/")
    public String mainPage(Model model) {
        List<ChallengeChecklist> challenges = challengeService.getAllChallenges();
        model.addAttribute("challenges", challenges);
        return "main"; // main.html
    }

    @GetMapping("/date/{date}")
    public String datePage(@PathVariable String date, Model model) {
        LocalDate parsedDate = LocalDate.parse(date);
        ChallengeChecklist checklist = challengeService.getChallengeByDate(parsedDate);
        model.addAttribute("checklist", checklist);
        model.addAttribute("date", date);
        return "date"; // date.html
    }

    @PostMapping("/date/{date}/update")
    public String updateChecklist(@PathVariable String date,
                                  @RequestParam(defaultValue = "false") boolean task1,
                                  @RequestParam(defaultValue = "false") boolean task2,
                                  @RequestParam(defaultValue = "false") boolean task3) {
        LocalDate parsedDate = LocalDate.parse(date);
        challengeService.updateChecklist(parsedDate, task1, task2, task3);
        return "redirect:/date/" + date;
    }
}
