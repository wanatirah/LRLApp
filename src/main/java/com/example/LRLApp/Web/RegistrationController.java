package com.example.LRLApp.Web;

import com.example.LRLApp.Service.UsersService;
import com.example.LRLApp.Dto.UserRegistrationDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private UsersService usersService;

    @ModelAttribute("users")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("users") UserRegistrationDto registrationDto) {
        usersService.save(registrationDto);
        return "redirect:/registration?success";
    }
}
