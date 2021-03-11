package salon.beauty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import salon.beauty.domain.*;
import salon.beauty.repo.MasterRepo;
import salon.beauty.repo.ServiceRepo;
import salon.beauty.repo.UserRepo;
import salon.beauty.repo.VisitRepo;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@RequestMapping("/master")
@Controller
@PreAuthorize("hasAuthority('MASTER')")
public class MasterController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private MasterRepo masterRepo;

    @GetMapping
    public String schedule(Principal principal, Model model){

        List<Visit> checkVisit = visitRepo.findByStatus(Status.ОЧІКУЄТЬСЯ);
        if (! checkVisit.isEmpty())
        for (int i = 0; i < checkVisit.size();) {
            Visit visit = checkVisit.get(i);
            if (visit.getDate().isBefore(LocalDate.now())){
                visit.setStatus(Status.ВИКОНАНО);
                i++;
            } else checkVisit.remove(visit);
        }
        if (! checkVisit.isEmpty())
            visitRepo.saveAll(checkVisit);

        User user = userRepo.findByUsername(principal.getName());
        List<Master> master = masterRepo.findByUsers(user);
        List<Visit> visits = visitRepo.findByMaster(master);
        model.addAttribute("visit", visits);
        model.addAttribute("schedule", true);
        return "master/schedule";
    }

    @GetMapping("/edit/{id}")
    public String scheduleEdit(@PathVariable Long id, Model model){
        Visit visit = visitRepo.findVisitById(id);
        model.addAttribute("edit", visit);
        model.addAttribute("schedule", true);
        return "master/schedule";
    }

    @PostMapping("/edit/{id}")
    public String editCast(@PathVariable Long id, @RequestParam String date, @RequestParam String time){
        Visit visit = visitRepo.findVisitById(id);
        if (! time.equals("")){
            visit.setTime(time);
        }

        if (! date.equals("")){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            visit.setDate(LocalDate.parse(date, formatter));
        }
        visitRepo.save(visit);
        return "redirect:/master";
    }

    @GetMapping("/delete/{id}")
    public String deleteVisit(@PathVariable Long id){
        Visit visit = visitRepo.findVisitById(id);
        visitRepo.delete(visit);
        return "redirect:/master";
    }

    @GetMapping("/services")
    public String serviceMaster(Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        List<Master> master = masterRepo.findByUsers(user);
        Set<Category> category = masterRepo.findAllCategory(master);
        List<Services> service = serviceRepo.findByCategories(category);
        model.addAttribute("service", service);
        model.addAttribute("services", true);
        return "master/service-master";
    }

    @GetMapping("/account")
    public String account(Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        List<Master> master = masterRepo.findByUsers(user);

        model.addAttribute("user", user);
        model.addAttribute("master", master);
        model.addAttribute("account", true);
        model.addAttribute("master_role", true);
        return "account";
    }

}