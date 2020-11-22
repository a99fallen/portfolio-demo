package portfolio.portfoliodemo.web.controller;

import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.portfoliodemo.exception.UserAlreadyExistsException;
import portfolio.portfoliodemo.service.UserService;
import portfolio.portfoliodemo.web.command.RegisterUserCommand;


import javax.validation.Valid;

@Controller @RequestMapping("/register")
@Slf4j @RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String getRegister(Model model) {
        model.addAttribute(new RegisterUserCommand());
        return "register/form";
    }

    @PostMapping
    // Do rejestracji potrzebujemy: nazwa użytkownika, hasło użytkownika
    // Można tworzyć obiekty typu:
    // - RegisterUserDTO
    // - RegisterUserForm
    // - RegisterUserRequest
    // - RegisterUserCommand
    public String processRegister(@Valid RegisterUserCommand registerUserCommand, BindingResult bindingResult) {
        log.debug("Dane do utworzenia użytkownika: {}", registerUserCommand);
        if (bindingResult.hasErrors()) {
            log.debug("Błędne dane: {}", bindingResult.getAllErrors());
            return "register/form";
        }

        try {
            Long id = userService.create(registerUserCommand);
            log.debug("Utworzono użytkownika o id = {}", id);
            return "redirect:/login";
        } catch (UserAlreadyExistsException uaee) {
            bindingResult.rejectValue("username", null, "Użytkownik o podanej nazwie już istnieje");
            return "register/form";
        } catch (RuntimeException re) {
            bindingResult.rejectValue(null, null, "Wystąpił błąd");
            return "register/form";
        }
    }
}