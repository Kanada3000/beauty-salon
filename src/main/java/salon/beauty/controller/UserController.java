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
import java.util.Map;
import java.util.Set;

@RequestMapping("/user")
@Controller
@PreAuthorize("hasAuthority('USER')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private VisitRepo visitRepo;

    @Autowired
    private MasterRepo masterRepo;

    @GetMapping
    public String greeting(Principal principal, Model model){
        String name = userRepo.findByUsername(principal.getName()).getName();
        model.addAttribute("name", name);
        model.addAttribute("main", true);
        return "main";
    }

    @GetMapping("/masters")
    public String masters(Model model){
        model.addAttribute("masters", true);
        return "masters";
    }


    @GetMapping("/services")
    public String services(Map<String, Object> model, Model model2){
        Iterable<Services> nails = serviceRepo.findByCategory(Category.Манікюр);
        Iterable<Services> hair = serviceRepo.findByCategory(Category.Зачіски);
        Iterable<Services> cosm = serviceRepo.findByCategory(Category.Косметологія);
        Iterable<Services> other = serviceRepo.findByCategory(Category.Інше);
        model.put("nails", nails);
        model.put("hair", hair);
        model.put("cosm", cosm);
        model.put("other", other);
        model2.addAttribute("services", true);
        Visit visit;
        if (visitRepo.findFirst1ByOrderByIdDesc() != null)
            if ((visitRepo.findFirst1ByOrderByIdDesc().getDate() != null) && (visitRepo
                    .findFirst1ByOrderByIdDesc()
                    .getTime() == null)){
                visit = visitRepo.findFirst1ByOrderByIdDesc();
                List<Master> vs = masterRepo.findMasterByCategory(visit.getServices().getCategory());
                if(visit.getStatus() == Status.ПЕРЕВІРЯЄТЬСЯ)
                for (int i = 0; i<vs.size(); ){
                    Master master = vs.get(i);
                    if (visitRepo.findTime(visit.getDate(), master.getTime(), Status.ОЧІКУЄТЬСЯ).isEmpty()){
                        visit.setStatus(Status.ОБРОБЛЮЄТЬСЯ);
                        i++;
                    } else{
                        vs.remove(master);
                    }
                } model.put("vs", vs);
            } model2.addAttribute("min", LocalDate.now().plusDays(1));
        model2.addAttribute("max", LocalDate.now().plusMonths(2));
        model2.addAttribute("mins", true);
        return "services";
    }

    @GetMapping("/services/close")
    public String close(){
        if ((visitRepo.findFirst1ByOrderByIdDesc().getStatus() == Status.ОБРОБЛЮЄТЬСЯ) || (visitRepo
                .findFirst1ByOrderByIdDesc()
                .getStatus() == Status.ПЕРЕВІРЯЄТЬСЯ)){
            Visit visit = visitRepo.findFirst1ByOrderByIdDesc();
            visitRepo.delete(visit);
        }
        return "redirect:/user/services";
    }

    @GetMapping("/services/add/{id}")
    public String addNewVisit(@PathVariable(value = "id") Long serv_id, Principal principal){
        Visit visit = new Visit();
        Services services = serviceRepo.findServicesById(serv_id);
        User user = userRepo.findByUsername(principal.getName());
        visit.setUser(user);
        visit.setServices(services);
        visit.setStatus(Status.ОБРОБЛЮЄТЬСЯ);
        visitRepo.save(visit);
        return "redirect:/user/services#win1";
    }

    @PostMapping("/services/new-visiting")
    public String addDate(@RequestParam String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Visit visit = visitRepo.findFirst1ByOrderByIdDesc();
        visit.setDate(LocalDate.parse(date, formatter));
        Set<LocalDate> checkDate = visitRepo.findAllDate();
        if (checkDate.contains(visit.getDate()))
            visit.setStatus(Status.ПЕРЕВІРЯЄТЬСЯ);
        visitRepo.save(visit);
        return "redirect:/user/services#win4";
    }

    @GetMapping("/services/add-time/{id}/{time}")
    public String addTime(@PathVariable(value = "id") Long id, @PathVariable(value = "time") String time){
        Visit visit = visitRepo.findFirst1ByOrderByIdDesc();
        Master master = masterRepo.findByUserId(id, visit.getServices().getCategory(), time);
        visit.setTime(time);
        visit.setMaster(master);
        visit.setStatus(Status.ОЧІКУЄТЬСЯ);
        visitRepo.save(visit);
        return "redirect:/user/services";
    }


    @GetMapping("/account")
    public String account(Principal principal, Model model){

        List<Visit> checkVisit = visitRepo.findByStatus(Status.ОЧІКУЄТЬСЯ);
        if (! checkVisit.isEmpty())
            for (int i = 0; i<checkVisit.size(); ){
                Visit visit = checkVisit.get(i);
                if (visit.getDate().isBefore(LocalDate.now())){
                    visit.setStatus(Status.ВИКОНАНО);
                    i++;
                } else
                    checkVisit.remove(visit);
            }
        if (! checkVisit.isEmpty())
            visitRepo.saveAll(checkVisit);

        User user = userRepo.findByUsername(principal.getName());
        List<Visit> visit = visitRepo.findByUsers(user);
        model.addAttribute("user", user);
        if (visit != null)
            model.addAttribute("visit", true);
        model.addAttribute("visits", visit);
        model.addAttribute("account", true);
        model.addAttribute("user_role", true);
        return "account";
    }
}