package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.User;
import pl.pai.pai.repo.SurveyRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SurveyService {
    @Autowired
    SurveyRepository surveyRepository;

    @Autowired
    UserService userService;
    public Survey addSurvey(Survey survey)
    {
        return surveyRepository.save(survey);
    }

    public Survey addSurvey(String title, String description, boolean forEveryone, Long authorId, String hashLink)
    {
        Optional<User> user = userService.get(authorId);
        if(user.isPresent())
        {
            return surveyRepository.save(new Survey(title,description,forEveryone, user.get(), hashLink));
        }else{
            return null;
        }
    }

    public boolean delSurvey(Long id)
    {
        try{
            surveyRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }
    public boolean delSurvey(Survey survey)
    {
        try{
            surveyRepository.delete(survey);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<Survey> getAll()
    {
        return surveyRepository.findAll();
    }

    public Optional<Survey> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId() == id).findFirst();
    }
}
