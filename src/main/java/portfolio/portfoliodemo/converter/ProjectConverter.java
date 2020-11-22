package portfolio.portfoliodemo.converter;

import org.springframework.stereotype.Component;
import portfolio.portfoliodemo.domain.model.Project;
import portfolio.portfoliodemo.web.command.CreateProjectCommand;

@Component
public class ProjectConverter {

    public Project from(CreateProjectCommand createProjectCommand) {
        return Project.builder()
                .name(createProjectCommand.getProjectName())
                .url(createProjectCommand.getUrl())
                .description(createProjectCommand.getProjectDescription())
                .build();
    }
}
