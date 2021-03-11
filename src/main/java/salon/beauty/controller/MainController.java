package salon.beauty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import salon.beauty.domain.Role;
import salon.beauty.domain.User;
import salon.beauty.repo.UserRepo;

import java.security.Principal;

@Controller
public class MainController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String greeting(Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        if (user.getRoles().contains(Role.ADMIN)){
            return "redirect:/admin";
        } else if (user.getRoles().contains(Role.MASTER)){
            return "redirect:/master";
        } else return "redirect:/user";
    }
}