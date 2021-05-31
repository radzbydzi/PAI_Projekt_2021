package pl.pai.pai.views;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.Survey;
import pl.pai.pai.security.SecurityUtils;
import pl.pai.pai.service.QuizService;
import pl.pai.pai.service.QuizUsersAnswersService;
import pl.pai.pai.service.SurveyService;
import pl.pai.pai.service.UserService;

@Route("/myStats")
public class MyStatsView extends VerticalLayout{
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	SurveyService surveyService;
	
	@Autowired
	UserService userService;
	
	Button showSolved(Quiz q)
	{
		Button b = new Button("Wejdź");
		b.addClickListener(x->{
			getUI().get().getPage().setLocation("/showQuiz/"+q.getHashLink());
		});
		return b;
	}
	Button showSolved(Survey q)
	{
		Button b = new Button("Wejdź");
		b.addClickListener(x->{
			getUI().get().getPage().setLocation("/showSurvey/"+q.getHashLink());
		});
		return b;
	}
	public MyStatsView()
	{
		
	}
	@PostConstruct
	void postConstruct() {
		MenuTemplate.addMenu(this);
		pl.pai.pai.model.User user = userService.getAll().stream().filter(y->y.getEmail().equals(SecurityUtils.getLoggedUserName())).findFirst().get();
		

		add(new H2("Stworzyłeś "+quizService.getAll().stream().filter(x->x.getAuthor().getId()==user.getId()).count()+" quizów"));
		add(new H2("Stworzyłeś "+surveyService.getAll().stream().filter(x->x.getAuthor().getId()==user.getId()).count()+" ankiet"));
		
		HorizontalLayout holder = new HorizontalLayout();
		holder.setSizeFull();
		VerticalLayout quizLayout = new VerticalLayout();
		quizLayout.setWidth("50%");
		
		quizLayout.add(new H2("Moje najczęściej rozwiązywane quizy"));
		List<Quiz> mostFrequentQuiz = quizService.getAll().stream().sorted((a,b) -> b.getQuizUsersAnswers().size()-a.getQuizUsersAnswers().size()).filter(x->x.getQuizUsersAnswers().size()>0).filter(x->x.getAuthor().getId()==user.getId()).filter(x->x.isForEveryone()).limit(10).collect(Collectors.toList());
		Grid<Quiz> mostFreqentGrid = new Grid<>();
		mostFreqentGrid.removeAllColumns();
		mostFreqentGrid.setSelectionMode(SelectionMode.SINGLE);
		mostFreqentGrid.setItems(mostFrequentQuiz);//na liscie wszystkie wypełnione
		mostFreqentGrid.addColumn(Quiz::getTitle).setHeader("Quiz");
		mostFreqentGrid.addColumn(Quiz::getDescription).setHeader("Opis");
		mostFreqentGrid.addComponentColumn(this::showSolved).setHeader("Wejdź do Quizu");
		quizLayout.add(mostFreqentGrid);
		
		holder.add(quizLayout);
		
		VerticalLayout surveyLayout = new VerticalLayout();
		surveyLayout.setWidth("50%");
		
		surveyLayout.add(new H2("Moje najczęściej rozwiązywane ankiety"));
		List<Survey> mostFrequentSurveys = surveyService.getAll().stream().sorted((a,b) -> b.getSurveyUsersAnswers().size()-a.getSurveyUsersAnswers().size()).filter(x->x.getSurveyUsersAnswers().size()>0).filter(x->x.getAuthor().getId()==user.getId()).filter(x->x.isForEveryone()).limit(10).collect(Collectors.toList());
		Grid<Survey> mostFreqentSurveyGrid = new Grid<>();
		mostFreqentSurveyGrid.removeAllColumns();
		mostFreqentSurveyGrid.setSelectionMode(SelectionMode.SINGLE);
		mostFreqentSurveyGrid.setItems(mostFrequentSurveys);//na liscie wszystkie wypełnione
		mostFreqentSurveyGrid.addColumn(Survey::getTitle).setHeader("Ankieta");
		mostFreqentSurveyGrid.addColumn(Survey::getDescription).setHeader("Opis");
		mostFreqentSurveyGrid.addComponentColumn(this::showSolved).setHeader("Wejdź do ankiety");
		surveyLayout.add(mostFreqentSurveyGrid);
		holder.add(surveyLayout);
		
		add(holder);
	}
}
