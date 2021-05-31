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
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.security.SecurityUtils;
import pl.pai.pai.service.QuizService;
import pl.pai.pai.service.QuizUserAnswerService;
import pl.pai.pai.service.QuizUsersAnswersService;
import pl.pai.pai.service.UserService;

@Route("showMyFilledQuizes")
@Push
public class ShowMyFilledQuizesView extends VerticalLayout{
	Quiz quiz;
	
	User user;

	@Autowired
	UserService userService;
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	QuizUsersAnswersService quizUsersAnswersService;

	public ShowMyFilledQuizesView(){
	}

	Label showTitle(QuizUsersAnswers q)
	{
		Label res = new Label(q.getUserQuiz().getTitle());
		return res;
	}
	Label showScore(QuizUsersAnswers q)
	{
		Quiz quiz = q.getUserQuiz();
		long fullPoints = quiz.getQuestions().stream().map(z->z.getPoints()).reduce(0, Integer::sum);
		int gainedPoints = q.getUserAnswers().stream().map(z->z.getPoints()).reduce(0, Integer::sum);
		Label res = new Label(gainedPoints+"/"+fullPoints);
		return res;
	}
	Label showFullCheck(QuizUsersAnswers q)
	{
		String fullCheck = "NIE";
		if(q.getUserAnswers().stream().filter(x->x.isChecked()).count()==q.getUserAnswers().size())
		{
			fullCheck="TAK";
		}
		Label res = new Label(fullCheck);
		return res;
	}
	@PostConstruct
	void postConstruct() {
		MenuTemplate.addMenu(this);
		user = userService.getAll().stream().filter(y->y.getEmail().equals(SecurityUtils.getLoggedUserName())).findFirst().get();
		
		List<QuizUsersAnswers> myQuizes = quizUsersAnswersService.getAll().stream().filter(x->x.getUser().getId()==user.getId()).collect(Collectors.toList());
		
		Grid<QuizUsersAnswers> questionsGrid = new Grid<>();//taka tabela
		questionsGrid.removeAllColumns();
		questionsGrid.setSelectionMode(SelectionMode.SINGLE);
		questionsGrid.setItems(myQuizes);//na liscie wszystkie wypełnione
		questionsGrid.addComponentColumn(this::showTitle).setHeader("Tytuł");
		questionsGrid.addComponentColumn(this::showScore).setHeader("Wynik");
		questionsGrid.addComponentColumn(this::showFullCheck).setHeader("W pełni sprawdzony");
		add(questionsGrid);
		
	}
  
	
}
