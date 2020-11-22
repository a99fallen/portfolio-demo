package portfolio.portfoliodemo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.portfoliodemo.data.user.UserSummary;
import portfolio.portfoliodemo.exception.UserAlreadyExistsException;
import portfolio.portfoliodemo.service.UserService;
import portfolio.portfoliodemo.web.command.EditUserCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile")
@Slf4j
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @ModelAttribute("userSummary")
    public UserSummary userSummary() {
        return userService.getCurrentUserSummary();
    }

    @GetMapping
    public String getProfilePage(Model model) {
        UserSummary summary = userService.getCurrentUserSummary();
        EditUserCommand editUserCommand = createEditUserCommand(summary);
        model.addAttribute("userSummary", new UserSummary());
        model.addAttribute("editUserCommand", editUserCommand);
        return "user/profile";
    }

    private EditUserCommand createEditUserCommand(UserSummary summary) {
        return EditUserCommand.builder()
                .firstName(summary.getFirstName())
                .lastName(summary.getLastName())
                .birthDate(summary.getBirthDate())
                .build();
    }

    @PostMapping("/edit")
    public String editUserProfile(@Valid EditUserCommand editUserCommand, BindingResult bindingResult) {
        log.debug("Dane do edycji użytkownika: {}", editUserCommand);
        if (bindingResult.hasErrors()) {
            log.debug("Błędne dane: {}", bindingResult.getAllErrors());
            return "user/profile";
        }
        try {
            boolean success = userService.edit(editUserCommand);
            log.debug("Edytowano użytkownika o id = {}", success);
            return "redirect:/profile";
        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Błąd przy edycji danych", re);
            bindingResult.rejectValue(null, null, "Wystąpił błąd");
            return "rediret:/profile";
        }
    }
}
