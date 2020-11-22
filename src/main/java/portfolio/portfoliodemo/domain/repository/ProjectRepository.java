package portfolio.portfoliodemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.portfoliodemo.domain.model.Project;

public interface ProjectRepository extends JpaRepository <Project, Long>{

}
