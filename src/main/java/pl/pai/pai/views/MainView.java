package pl.pai.pai.views;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

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

@Route("/")
public class MainView extends VerticalLayout{
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	SurveyService surveyService;
	
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
	public MainView()
	{
		
	}
	@PostConstruct
	void postConstruct() {
		MenuTemplate.addMenu(this);
		
		HorizontalLayout holder = new HorizontalLayout();
		holder.setSizeFull();
		VerticalLayout quizLayout = new VerticalLayout();
		quizLayout.setWidth("50%");
		
		quizLayout.add(new H2("Ostatnio dodane quizy"));
		List<Quiz> lastAddedQuizes = quizService.getAll().stream().sorted((a,b) -> (int)(b.getId()-a.getId())).filter(x->x.isForEveryone()).limit(10).collect(Collectors.toList());
		Grid<Quiz> lastAddedGrid = new Grid<>();
		lastAddedGrid.removeAllColumns();
		lastAddedGrid.setSelectionMode(SelectionMode.SINGLE);
		lastAddedGrid.setItems(lastAddedQuizes);//na liscie wszystkie wypełnione
		lastAddedGrid.addColumn(Quiz::getTitle).setHeader("Quiz");
		lastAddedGrid.addColumn(Quiz::getDescription).setHeader("Opis");
		lastAddedGrid.addComponentColumn(this::showSolved).setHeader("Wejdź do Quizu");
		quizLayout.add(lastAddedGrid);
		
		quizLayout.add(new H2("Najczęściej rozwiązywane quizy"));
		List<Quiz> mostFrequentQuiz = quizService.getAll().stream().sorted((a,b) -> b.getQuizUsersAnswers().size()-a.getQuizUsersAnswers().size()).filter(x->x.getQuizUsersAnswers().size()>0).filter(x->x.isForEveryone()).limit(10).collect(Collectors.toList());
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
		surveyLayout.add(new H2("Ostatnio dodane ankiety"));
		List<Survey> lastAddedSurveys = surveyService.getAll().stream().sorted((a,b) -> (int)(b.getId()-a.getId())).filter(x->x.isForEveryone()).limit(10).collect(Collectors.toList());
		Grid<Survey> lastAddedSurveyGrid = new Grid<>();
		lastAddedSurveyGrid.removeAllColumns();
		lastAddedSurveyGrid.setSelectionMode(SelectionMode.SINGLE);
		lastAddedSurveyGrid.setItems(lastAddedSurveys);//na liscie wszystkie wypełnione
		lastAddedSurveyGrid.addColumn(Survey::getTitle).setHeader("Ankieta");
		lastAddedSurveyGrid.addColumn(Survey::getDescription).setHeader("Opis");
		lastAddedSurveyGrid.addComponentColumn(this::showSolved).setHeader("Wejdź do ankiety");
		surveyLayout.add(lastAddedSurveyGrid);
		
		surveyLayout.add(new H2("Najczęściej rozwiązywane ankiety"));
		List<Survey> mostFrequentSurveys = surveyService.getAll().stream().sorted((a,b) -> b.getSurveyUsersAnswers().size()-a.getSurveyUsersAnswers().size()).filter(x->x.isForEveryone()).filter(x->x.getSurveyUsersAnswers().size()>0).limit(10).collect(Collectors.toList());
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
