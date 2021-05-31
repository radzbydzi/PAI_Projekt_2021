package pl.pai.pai.views;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import lombok.Data;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizAnswer;
import pl.pai.pai.model.QuizQuestion;
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.SurveyAnswer;
import pl.pai.pai.model.SurveyQuestion;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.security.SecurityUtils;
import pl.pai.pai.service.QuizAnswerService;
import pl.pai.pai.service.QuizQuestionService;
import pl.pai.pai.service.QuizService;
import pl.pai.pai.service.QuizUsersAnswersService;
import pl.pai.pai.service.SurveyAnswerService;
import pl.pai.pai.service.SurveyQuestionService;
import pl.pai.pai.service.SurveyService;
import pl.pai.pai.service.SurveyUsersAnswersService;
import pl.pai.pai.service.UserService;

@Route("/addSurvey")
public class AddSurveyView extends VerticalLayout{
	@Autowired
    UserService userService;

    @Autowired
    SurveyService surveyService;

    @Autowired
    SurveyQuestionService surveyQuestionService;

    @Autowired
    SurveyUsersAnswersService surveyUsersAnswersService;

    @Autowired
    SurveyAnswerService surveyAnswerService;
	
	public AddSurveyView(){
		
	}
	private String inputWidth = "350px";
	private String inputWidthAnsw = "300px";
	
	@Data
	class QuestionBox{
		TextField question;
		
		TextField answA;
		TextField answB;
		TextField answC;
		TextField answD;
		Select<QuestionType> questionType;
		
		boolean open=false;
		
		QuestionBox(TextField question,Select<QuestionType> questionType){
			this.question = question;
			this.questionType=questionType;
			
			open=true;
		}



		public QuestionBox(TextField question,Select<QuestionType> questionType, TextField answA, TextField answB, TextField answC, TextField answD) {
			super();
			this.question = question;
			this.answA = answA;
			this.answB = answB;
			this.answC = answC;
			this.answD = answD;
			this.questionType=questionType;
			
			open=false;
		}
	}
	@PostConstruct
	void postConstruct() {
		MenuTemplate.addMenu(this);
		TextField  title = new TextField ("Tytuł");
		title.setWidth(inputWidth);
		add(title);
		
		TextArea  description = new TextArea("Opis");
		description.setWidth(inputWidth);
		description.setHeight("200px");
		add(description);
		
		RadioButtonGroup<String> forEveryone = new RadioButtonGroup<>();
		forEveryone.setLabel("Publiczne");
		forEveryone.setItems("Tak", "Nie");
		forEveryone.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		forEveryone.setValue("Tak");
		add(forEveryone);
		
		
		VerticalLayout questionHolder= new VerticalLayout();
		List<QuestionBox> questions = new ArrayList<>();
		
		HorizontalLayout addButtonLayout = new HorizontalLayout();
		Button addClosed = new Button("Dodaj pytanie zamknięte");
		addClosed.addClickListener(x->{
			TextField question = new TextField("Treść pytania");
			question.setWidth(inputWidth);
			
			TextField answA = new TextField("Odpowiedź A");
			TextField answB = new TextField("Odpowiedź B");
			TextField answC = new TextField("Odpowiedź C");
			TextField answD = new TextField("Odpowiedź D");
			
			answA.setWidth(inputWidthAnsw);
			answB.setWidth(inputWidthAnsw);
			answC.setWidth(inputWidthAnsw);
			answD.setWidth(inputWidthAnsw);
			
			Select<QuestionType> questionType = new Select<QuestionType>();
			questionType.setItems(QuestionType.CLOSED,QuestionType.CLOSED_MULTI);
			questionType.setLabel("Długość odpowiedzi");
			
			questions.add(new QuestionBox(question, questionType, answA, answB,  answC,  answD));
			questionHolder.add(question, questionType, answA, answB, answC, answD);
			questionHolder.add(new Hr());
		});
		addButtonLayout.add(addClosed);
		
		Button addOpened = new Button("Dodaj pytanie otwarte");
		addOpened.addClickListener(x->{
			TextField question = new TextField("Treść pytania");
			question.setWidth(inputWidth);
			
			Select<QuestionType> questionType = new Select<QuestionType>();
			questionType.setItems(QuestionType.OPENED,QuestionType.OPENED_LONG);
			questionType.setLabel("Długość odpowiedzi");
			
			questions.add(new QuestionBox(question,questionType));
			questionHolder.add(question,questionType);
			questionHolder.add(new Hr());
		});
		addButtonLayout.add(addOpened);
		
		add(addButtonLayout);
		H1 pytania = new H1("Pytania");
		add(pytania);
		
		add(questionHolder);
		
		Button register = new Button("Dodaj");
		
		register.addClickListener(x->{
			User user = userService.getAll().stream().filter(y->y.getEmail().equals(SecurityUtils.getLoggedUserName())).findFirst().get();
			
			boolean forEveryoneB = false;
			
			if(forEveryone.getValue().contentEquals("Tak"))
				forEveryoneB=true;
			
			Survey newSurvey = new Survey(title.getValue(), description.getValue(),  forEveryoneB, user, DigestUtils.sha256Hex(RandomStringUtils.random(30, true, true)));
			
			for(int i=0; i<questions.size(); i++)
			{
				QuestionBox q = questions.get(i);
				SurveyQuestion surveyQuestion = new SurveyQuestion(q.getQuestion().getValue(), newSurvey, QuestionType.CLOSED);
				int points = 0;
				if(q.isOpen())
				{
					surveyQuestion.setType(q.getQuestionType().getValue());		
					
					if(q.getQuestionType().getValue()==QuestionType.OPENED_LONG)
						surveyQuestion.setLong(true);
					else
						surveyQuestion.setLong(false);
				}else {
					surveyQuestion.setType(q.getQuestionType().getValue());
					SurveyAnswer a = new SurveyAnswer(q.getAnswA().getValue(), surveyQuestion);
					SurveyAnswer b = new SurveyAnswer(q.getAnswB().getValue(), surveyQuestion);
					SurveyAnswer c = new SurveyAnswer(q.getAnswC().getValue(), surveyQuestion);
					SurveyAnswer d = new SurveyAnswer(q.getAnswD().getValue(), surveyQuestion);
					
					surveyQuestion.getAnswers().add(a);
					surveyQuestion.getAnswers().add(b);
					surveyQuestion.getAnswers().add(c);
					surveyQuestion.getAnswers().add(d);
				}
				newSurvey.getQuestions().add(surveyQuestion);
			}

			surveyService.addSurvey(newSurvey);
			
			getUI().get().getPage().setLocation("/");
		});
		add(register);
	}
}
