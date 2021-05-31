package pl.pai.pai.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pai.pai.model.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
