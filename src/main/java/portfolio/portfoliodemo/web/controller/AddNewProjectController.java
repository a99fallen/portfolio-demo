package portfolio.portfoliodemo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.portfoliodemo.service.ProjectService;
import portfolio.portfoliodemo.web.command.CreateProjectCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/projects")
@Slf4j @RequiredArgsConstructor
public class AddNewProjectController {

    private final ProjectService projectService;

    @GetMapping("/add")
    public String getProjectPage(Model model) {
        model.addAttribute(new CreateProjectCommand());
        return "project/add";
    }

    @PostMapping("/add")
    public String processAddProject(@Valid CreateProjectCommand createProjectCommand, BindingResult bindings) {
        log.debug("Dane dla dodawania projetu: {}", createProjectCommand);
        if (bindings.hasErrors()) {
            log.debug("Dane zawierajá bøédy: {}", bindings.getAllErrors());
            return "project/add";
        }

        try {
            projectService.addProject(createProjectCommand);
            log.debug("Poprawne dodanie projektu");
            return "redirect:/projects";
        } catch (RuntimeException re) {
            log.warn(re.getLocalizedMessage());
            log.debug("Błąd podczas tworzenia danych", re);
            bindings.rejectValue(null, null, "Wystąpił błąd");
        }
        return "project/add";
    }
}
