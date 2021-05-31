package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.SurveyQuestion;
import pl.pai.pai.model.SurveyUserAnswer;
import pl.pai.pai.model.SurveyUsersAnswers;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.repo.SurveyUserAnswerRepository;
import pl.pai.pai.repo.SurveyUsersAnswersRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyUsersAnswersService {
    @Autowired
    SurveyUsersAnswersRepository surveyUsersAnswersRepository;

    @Autowired
    SurveyService surveyService;

    @Autowired
    UserService userService;

    public SurveyUsersAnswers addSurveyUsersAnswers(SurveyUsersAnswers surveyUsersAnswers)
    {
        return surveyUsersAnswersRepository.save(surveyUsersAnswers);
    }

    
    public boolean delSurveyUsersAnswers(Long id)
    {
        try{
        	surveyUsersAnswersRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }
    public boolean delSurveyUsersAnswers(SurveyUsersAnswers surveyUserAnswer)
    {
        try{
            surveyUsersAnswersRepository.delete(surveyUserAnswer);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<SurveyUsersAnswers> getAll()
    {
        return surveyUsersAnswersRepository.findAll();
    }

    public Optional<SurveyUsersAnswers> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId() == id).findFirst();
    }
}
