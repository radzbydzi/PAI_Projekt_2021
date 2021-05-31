package pl.pai.pai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.pai.pai.model.*;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.service.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class QuizRestController {

    @Autowired
    UserService userService;
    @Autowired
    QuizService quizService;
    @Autowired
    QuizQuestionService quizQuestionService;
    @Autowired
    QuizAnswerService quizAnswerService;
    @Autowired
    QuizUsersAnswersService quizUsersAnswersService;
    @Autowired
    SurveyService surveyService;
    @Autowired
    SurveyQuestionService surveyQuestionService;
    @Autowired
    SurveyAnswerService surveyAnswerService;
    @Autowired
    SurveyUsersAnswersService surveyUsersAnswersService;


    //--------- USER ACTIONS -----------//
    @GetMapping(value = "/user/getAll")
    public List<User> showUser() {
        return userService.getAll();
    }

    @PostMapping(value = "/user/add")
    public User addUser(@RequestParam String name, @RequestParam String surname, @RequestParam String email, @RequestParam String password)
    {
        return userService.addUser(name,surname,email,password);
    }

    @PostMapping(value = "/user/del")
    public boolean delUser(@RequestParam Long id)
    {
        return userService.delUser(id);
    }

    //--------- QUIZ ACTIONS -----------//
    @GetMapping(value = "/quiz/getAll")
    public List<Quiz> showQuiz(){
        return quizService.getAll();
    }

    @PostMapping(value =  "/quiz/add")
    public Quiz addQuiz(@RequestParam String title, @RequestParam String description, @RequestParam boolean randomOrder, @RequestParam Long durationInSeconds, @RequestParam boolean forEveryone, @RequestParam Long authorId, @RequestParam String hashLink)
    {
        return quizService.addQuiz(title, description, randomOrder, durationInSeconds, forEveryone, authorId, hashLink);
    }

    @PostMapping(value =  "/quiz/del")
    public boolean delQuiz(@RequestParam Long id)
    {
        return quizService.delQuiz(id);
    }

    //--------- QUIZ ANSWER ACTIONS -----------//
    @GetMapping(value = "/quizanswer/getAll")
    public List<QuizAnswer> showQuizAnswer(){
        return quizAnswerService.getAll();
    }

    @PostMapping(value =  "/quizanswer/add")
    public QuizAnswer addQuizAnswer(@RequestParam String description, @RequestParam boolean isCorrect, @RequestParam Long quizQuestionId)
    {
        return quizAnswerService.addQuizAnswer(description, isCorrect, quizQuestionId);
    }

    @PostMapping(value =  "/quizanswer/del")
    public boolean delQuizAnswer(@RequestParam Long id)
    {
        return quizAnswerService.delQuizAnswer(id);
    }

    //--------- QUIZ QUESTION ACTIONS -----------//
    @GetMapping(value = "/quizquestion/getAll")
    public List<QuizQuestion> showQuizQuestion(){
        return quizQuestionService.getAll();
    }

    @PostMapping(value =  "/quizquestion/add")
    public QuizQuestion addQuizAnswer(@RequestParam String question, @RequestParam Long quizId, @RequestParam QuestionType type)
    {
        return quizQuestionService.addQuizQuestion(question, quizId, type);
    }

    @PostMapping(value =  "/quizquestion/del")
    public boolean delQuizQuestion(@RequestParam Long id)
    {
        return quizQuestionService.delQuizQuestion(id);
    }

    //--------- QUIZ USERS ANSWERS ACTIONS -----------//
    @GetMapping(value = "/quizusersanswers/getAll")
    public List<QuizUsersAnswers> showQuizUsersAnswers(){
        return quizUsersAnswersService.getAll();
    }

    @PostMapping(value =  "/quizusersanswers/add")
    public QuizUsersAnswers addQuizUsersAnswers(@RequestParam Long userId, @RequestParam Long quizId)
    {
        return quizUsersAnswersService.addQuizUsersAnswers(userId, quizId);
    }

    @PostMapping(value =  "/quizusersanswers/del")
    public boolean delQuizUsersAnswers(@RequestParam Long id)
    {
        return quizQuestionService.delQuizQuestion(id);
    }

    //--------- SURVEY ACTIONS -----------//
    @GetMapping(value = "/survey/getAll")
    public List<Survey> showSurvey(){
        return surveyService.getAll();
    }

    @PostMapping(value =  "/survey/add")
    public Survey addSurvey(@RequestParam String title, @RequestParam String description, @RequestParam boolean forEveryone, @RequestParam Long authorId, @RequestParam String hashLink)
    {
        return surveyService.addSurvey(title, description, forEveryone, authorId, hashLink);
    }

    @PostMapping(value =  "/survey/del")
    public boolean delSurvey(@RequestParam Long id)
    {
        return surveyService.delSurvey(id);
    }

    //--------- SURVEY ANSWER ACTIONS -----------//
    @GetMapping(value = "/surveyanswer/getAll")
    public List<SurveyAnswer> showSurveyAnswer(){
        return surveyAnswerService.getAll();
    }

    @PostMapping(value =  "/surveyanswer/add")
    public SurveyAnswer addSurveyAnswer(@RequestParam String description, @RequestParam Long surveyQuestionId)
    {
        return surveyAnswerService.addSurveyAnswer(description, surveyQuestionId);
    }

    @PostMapping(value =  "/surveyanswer/del")
    public boolean delSurveyAnswer(@RequestParam Long id)
    {
        return surveyAnswerService.delSurveyAnswer(id);
    }

    //--------- SURVEY QUESTION ACTIONS -----------//
    @GetMapping(value = "/surveyquestion/getAll")
    public List<SurveyQuestion> showSurveyQuestion(){
        return surveyQuestionService.getAll();
    }

    @PostMapping(value =  "/surveyquestion/add")
    public SurveyQuestion addSurveyQuestion(@RequestParam String question, @RequestParam Long surveyId, @RequestParam QuestionType type)
    {
        return surveyQuestionService.addSurveyQuestion(question, surveyId, type);
    }

    @PostMapping(value =  "/surveyquestion/del")
    public boolean delSurveyQuestion(@RequestParam Long id)
    {
        return surveyAnswerService.delSurveyAnswer(id);
    }

    //--------- SURVEY USERS ANSWERS ACTIONS -----------//
    @GetMapping(value = "/surveyusersanswers/getAll")
    public List<SurveyUsersAnswers> showSurveyUsersAnswers(){
        return surveyUsersAnswersService.getAll();
    }

//    @PostMapping(value =  "/surveyusersanswers/add")
//    public SurveyUsersAnswers addSurveyUsersAnswers(@RequestParam Long userId, @RequestParam Long surveyId)
//    {
//        return surveyUsersAnswersService.addSurveyUsersAnswers(userId, surveyId);
//    }

    @PostMapping(value =  "/surveyusersanswers/del")
    public boolean delSurveyUsersAnswers(@RequestParam Long id)
    {
        return surveyUsersAnswersService.delSurveyUsersAnswers(id);
    }
}
