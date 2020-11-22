package portfolio.portfoliodemo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import portfolio.portfoliodemo.web.command.CreateProjectCommand;

@Controller
@RequestMapping("/project")
@Slf4j @RequiredArgsConstructor
public class AddNewProjectController {

    @GetMapping
    public String getProjectPage(Model model) {
        model.addAttribute(new CreateProjectCommand());
        return "project/add";
    }
}
