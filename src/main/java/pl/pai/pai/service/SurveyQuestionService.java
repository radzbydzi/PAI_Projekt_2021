package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.SurveyQuestion;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.repo.SurveyQuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyQuestionService {
    @Autowired
    SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    SurveyService surveyService;

    public SurveyQuestion addSurveyQuestion(SurveyQuestion surveyQuestion)
    {
        return surveyQuestionRepository.save(surveyQuestion);
    }

    public SurveyQuestion addSurveyQuestion(String question, Long surveyId, QuestionType type)
    {
        Optional<Survey> survey = surveyService.get(surveyId);
        if(survey.isPresent())
        {
            return surveyQuestionRepository.save(new SurveyQuestion(question, survey.get(), type));
        }else{
            return null;
        }
    }

    public boolean delSurveyQuestion(Long id)
    {
        try{
            surveyQuestionRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }
    public boolean delSurveyQuestion(SurveyQuestion surveyQuestion)
    {
        try{
            surveyQuestionRepository.delete(surveyQuestion);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<SurveyQuestion> getAll()
    {
        return surveyQuestionRepository.findAll();
    }

    public Optional<SurveyQuestion> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId() == id).findFirst();
    }
}
