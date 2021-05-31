package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.SurveyAnswer;
import pl.pai.pai.model.SurveyQuestion;
import pl.pai.pai.model.User;
import pl.pai.pai.repo.SurveyAnswerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyAnswerService {
    @Autowired
    SurveyAnswerRepository surveyAnswerRepository;

    @Autowired
    SurveyQuestionService surveyQuestionService;

    public SurveyAnswer addSurveyAnswer(SurveyAnswer surveyAnswer)
    {
        return surveyAnswerRepository.save(surveyAnswer);
    }

    public SurveyAnswer addSurveyAnswer(String description, Long surveyQuestionId)
    {
        Optional<SurveyQuestion> surveyQuestion = surveyQuestionService.get(surveyQuestionId);
        if(surveyQuestion.isPresent())
        {
            return surveyAnswerRepository.save(new SurveyAnswer(description, surveyQuestion.get()));
        }else{
            return null;
        }
    }

    public boolean delSurveyAnswer(Long id)
    {
        try{
            surveyAnswerRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }
    public boolean delSurveyAnswer(SurveyAnswer surveyAnswer)
    {
        try{
            surveyAnswerRepository.delete(surveyAnswer);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<SurveyAnswer> getAll()
    {
        return surveyAnswerRepository.findAll();
    }

    public Optional<SurveyAnswer> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId() == id).findFirst();
    }
}
