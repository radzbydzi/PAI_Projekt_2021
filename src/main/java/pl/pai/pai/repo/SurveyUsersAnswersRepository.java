package pl.pai.pai.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pai.pai.model.SurveyUsersAnswers;

@Repository
public interface SurveyUsersAnswersRepository extends JpaRepository<SurveyUsersAnswers, Long> {
}
