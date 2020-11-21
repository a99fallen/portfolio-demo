package portfolio.portfoliodemo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.portfoliodemo.web.command.RegisterUserCommand;

@Controller @Slf4j @RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    @GetMapping
    public String getRegister() {
        return "refister/form";
    }

    @PostMapping
    // Do rejestracji potrzebujemy: nazwa użytkownika, hasło użytkownika
    // Można tworzyć obiekty typu:
    // - RegisterUserDTO
    // - RegisterUserForm
    // - RegisterUserRequest
    // - RegisterUserCommand
    private String processRegister(RegisterUserCommand registerUserCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "register/form";
        }
        return "redirect/login";
    }
}
