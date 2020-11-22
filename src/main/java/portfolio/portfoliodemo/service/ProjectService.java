package portfolio.portfoliodemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import portfolio.portfoliodemo.converter.ProjectConverter;
import portfolio.portfoliodemo.domain.model.Project;
import portfolio.portfoliodemo.domain.model.User;
import portfolio.portfoliodemo.domain.repository.ProjectRepository;
import portfolio.portfoliodemo.domain.repository.UserRepository;
import portfolio.portfoliodemo.web.command.CreateProjectCommand;

import javax.transaction.Transactional;

@Service @Transactional
@Slf4j @RequiredArgsConstructor
public class ProjectService {

    private final ProjectConverter projectConverter;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public void addProject(CreateProjectCommand createProjectCommand) {
        log.debug("Projekt do zapisania:{}", createProjectCommand);

        Project projectToAdd = projectConverter.from(createProjectCommand);
        updateProjectWithUser(projectToAdd);
        log.debug("Uzyskany projekt do zapisu: {}", projectToAdd);

        projectRepository.save(projectToAdd);
        log.debug("Zapisany projekt: {}", projectToAdd);
    }

    private void updateProjectWithUser(Project projectToAdd) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getAuthenticatedUser(username);
        projectToAdd.setUser(user);
    }
}