package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.SurveyQuestion;
import pl.pai.pai.model.SurveyUsersAnswers;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.repo.SurveyUsersAnswersRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyUserAnswerService {
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

    public SurveyUsersAnswers addSurveyUsersAnswers(Long userId, Long surveyId)
    {
        Optional<User> user = userService.get(userId);
        Optional<Survey> survey = surveyService.get(surveyId);
        if(survey.isPresent() && user.isPresent())
        {
            return surveyUsersAnswersRepository.save(new SurveyUsersAnswers(user.get(),survey.get()));
        }else{
            return null;
        }
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
    public boolean delSurveyUsersAnswers(SurveyUsersAnswers surveyUsersAnswers)
    {
        try{
            surveyUsersAnswersRepository.delete(surveyUsersAnswers);
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
