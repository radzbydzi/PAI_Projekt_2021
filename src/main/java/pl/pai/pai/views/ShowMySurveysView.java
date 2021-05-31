package pl.pai.pai.views;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.WildcardParameter;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.server.InitialPageSettings;
import com.vaadin.flow.server.PageConfigurator;
import com.vaadin.flow.server.SessionDestroyEvent;
import com.vaadin.flow.server.SessionDestroyListener;

import lombok.Data;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizAnswer;
import pl.pai.pai.model.QuizQuestion;
import pl.pai.pai.model.QuizUserAnswer;
import pl.pai.pai.model.QuizUsersAnswers;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.security.SecurityUtils;
import pl.pai.pai.service.QuizService;
import pl.pai.pai.service.QuizUserAnswerService;
import pl.pai.pai.service.QuizUsersAnswersService;
import pl.pai.pai.service.SurveyService;
import pl.pai.pai.service.UserService;

@Route("showMySurveys")
@Push
public class ShowMySurveysView extends VerticalLayout{
	Quiz quiz;
	
	User user;

	@Autowired
	UserService userService;
	
	@Autowired
	SurveyService surveyService;
	

	public ShowMySurveysView(){
	}

	Button showSolved(Survey q)
	{
		Button b = new Button("Rozwiązania");
		b.addClickListener(x->{
			getUI().get().getPage().setLocation("/showSurvey/"+q.getHashLink());
		});
		return b;
	}
	Button showLink(Survey q)
	{
		Dialog dialog = new Dialog();
		TextField input = new TextField("Ścieżka");
		input.setSizeFull();
		input.setValue("/showSurvey/"+q.getHashLink());				
		dialog.add(input);
		dialog.setWidth("400px");
		dialog.setHeight("150px");
		
		Button button = new Button("Pokaż link");
        button.addClickListener(x->{
            dialog.open();
        }            
        );
		return button;
	}
	@PostConstruct
	void postConstruct() {
		MenuTemplate.addMenu(this);
		user = userService.getAll().stream().filter(y->y.getEmail().equals(SecurityUtils.getLoggedUserName())).findFirst().get();
		
		List<Survey> mySurveys= surveyService.getAll().stream().filter(x->x.getAuthor().getId()==user.getId()).collect(Collectors.toList());
		
		Grid<Survey> questionsGrid = new Grid<>();//taka tabela
		questionsGrid.removeAllColumns();
		questionsGrid.setSelectionMode(SelectionMode.SINGLE);
		questionsGrid.setItems(mySurveys);//na liscie wszystkie wypełnione
		questionsGrid.addColumn(Survey::getTitle).setHeader("Ankieta");
		questionsGrid.addColumn(Survey::getDescription).setHeader("Opis");
		questionsGrid.addComponentColumn(this::showLink).setHeader("Link");
		questionsGrid.addComponentColumn(this::showSolved).setHeader("Pokaż rozwiązania");
		add(questionsGrid);
		
	}
  
	
}
