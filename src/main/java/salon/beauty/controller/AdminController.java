package salon.beauty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RequestMapping("admin")
@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MasterRepo masterRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ServiceRepo serviceRepo;

    @Autowired
    private VisitRepo visitRepo;

    @GetMapping
    public String users(Map<String, Object> model, Model model2){
        Iterable<User> users = userRepo.findAll(Sort.by(Sort.Direction.ASC, "roles"));
        model.put("user", users);
        model2.addAttribute("users", true);
        return "admin/users";
    }

    @GetMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id){
        User user = userRepo.findUserById(id);
        if (user.getRoles().equals(Collections.singleton(Role.MASTER))){
            List<Master> masters = masterRepo.findByUsers(user);
            masterRepo.deleteAll(masters);
        }
        List<Visit> visits = visitRepo.findByUsers(user);
        visitRepo.deleteAll(visits);
        userRepo.delete(user);
        return "redirect:/";
    }

    @GetMapping("/user/{id}/edit")
    public String editGetUser(@PathVariable Long id, Model model){
        User user = userRepo.findUserById(id);
        model.addAttribute("us", user);
        model.addAttribute("users", true);
        return "admin/users";
    }

    @PostMapping("/user/{id}/edit")
    public String editUser(User userTemp){
        User user = userRepo.findUserById(userTemp.getId());
        if (! userTemp.getName().isEmpty()){
            user.setName(userTemp.getName());
        }
        if (! userTemp.getPhone().isEmpty()){
            user.setPhone(userTemp.getPhone());
        }
        if (! userTemp.getUsername().isEmpty()){
            user.setUsername(userTemp.getUsername());
        }
        if (! userTemp.getRoles().equals(user.getRoles())){
            user.setRoles(userTemp.getRoles());
            if (user.getRoles().equals(Collections.singleton(Role.MASTER))){
                Master master = new Master();
                List<Visit> visits = visitRepo.findByUsers(user);
                visitRepo.deleteAll(visits);
                master.setCategory(Category.Манікюр);
                master.setUser(user);
                master.setTime("09:00");
                masterRepo.save(master);
            } else{
                List<Master> master = masterRepo.findByUsers(user);
                masterRepo.deleteAll(master);
            }
        }
        userRepo.save(user);
        return "redirect:/";
    }

    @GetMapping("/user/add")
    public String addGetUser(Model model){
        model.addAttribute("add", true);
        model.addAttribute("users", true);
        return "admin/users";
    }

    @PostMapping("/user/add")
    public String addUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        if (user.getRoles().equals(Collections.singleton(Role.MASTER))){
            Master master = new Master();
            master.setCategory(Category.Манікюр);
            master.setUser(user);
            master.setTime("09:00");
            masterRepo.save(master);
        }
        return "redirect:/";
    }


    @GetMapping("/account")
    public String account(Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        model.addAttribute("account", true);
        model.addAttribute("admin_role", true);
        return "account";
    }


    @GetMapping("/cast")
    public String cast(Map<String, Object> model, Model model2){
        Iterable<Master> masters = masterRepo.findAll(Sort.by(Sort.Direction.DESC, "users"));
        model.put("master", masters);
        model2.addAttribute("masters", true);
        return "admin/users";
    }

    @GetMapping("/cast/add")
    public String addGetCast(Model model){
        model.addAttribute("ms", userRepo.findAllByRoleUser(Collections.singleton(Role.MASTER)));
        model.addAttribute("add", true);
        model.addAttribute("masters", true);
        return "admin/users";
    }

    @PostMapping("/cast/add")
    public String addCast(@RequestParam Long id, @RequestParam Category category, @RequestParam String time){
        User user = userRepo.findUserById(id);
        Master master = new Master();
        master.setCategory(category);
        master.setUser(user);
        master.setTime(time);
        masterRepo.save(master);
        return "redirect:/admin/cast";
    }

    @GetMapping("/cast/{id}/edit")
    public String editGetCast(@PathVariable Long id, Model model){
        Master master = masterRepo.findMasterById(id);
        model.addAttribute("edit", master);
        model.addAttribute("masters", true);
        return "admin/users";
    }

    @PostMapping("/cast/{id}/edit")
    public String editCast(Master masterTemp){
        Master master = masterRepo.findMasterById(masterTemp.getId());

        if (! masterTemp.getTime().isEmpty()){
            master.setTime(masterTemp.getTime());
        }
        masterRepo.save(master);
        return "redirect:/admin/cast";
    }

    @GetMapping("/cast/{id}/delete")
    public String deleteCast(@PathVariable Long id){
        Master master = masterRepo.findMasterById(id);
        masterRepo.delete(master);
        return "redirect:/admin/cast";
    }


    @GetMapping("/services")
    public String serviceList(Map<String, Object> model, Model model2){
        Iterable<Services> services = serviceRepo.findAll();
        model.put("service", services);
        model2.addAttribute("services", true);
        return "admin/service-list";
    }

    @GetMapping("/services/add")
    public String addGetService(Model model){
        model.addAttribute("add", true);
        return "admin/service-list";
    }

    @PostMapping("/services/add")
    public String addService(Services service){
        serviceRepo.save(service);
        return "redirect:/admin/services";
    }

    @GetMapping("/services/{id}/edit")
    public String editGetService(@PathVariable Long id, Model model){
        Services service = serviceRepo.findServicesById(id);
        model.addAttribute("serv", service);
        return "admin/service-list";
    }

    @PostMapping("/services/{id}/edit")
    public String editService(Services serviceTemp){
        Services service = serviceRepo.findServicesById(serviceTemp.getId());

        if (! serviceTemp.getName().isEmpty()){
            service.setName(serviceTemp.getName());
        }
        if (! serviceTemp.getCategory().equals(service.getCategory())){
            service.setCategory(serviceTemp.getCategory());
        }
        serviceRepo.save(service);
        return "redirect:/admin/services";
    }

    @GetMapping("/services/{id}/delete")
    public String deleteService(@PathVariable Long id){
        Services services = serviceRepo.findServicesById(id);
        serviceRepo.delete(services);
        return "redirect:/admin/services";
    }


    @GetMapping("/visit")
    public String visitList(Map<String, Object> model, Model model2){

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

        Iterable<Visit> visits = visitRepo.findAll();
        model.put("visits", visits);
        model2.addAttribute("visit", true);
        return "admin/visit-list";
    }
}