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
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.security.SecurityUtils;
import pl.pai.pai.service.QuizAnswerService;
import pl.pai.pai.service.QuizQuestionService;
import pl.pai.pai.service.QuizService;
import pl.pai.pai.service.QuizUsersAnswersService;
import pl.pai.pai.service.UserService;

@Route("/addQuiz")
public class AddQuizView extends VerticalLayout{
	@Autowired
    UserService userService;

    @Autowired
    QuizService quizService;

    @Autowired
    QuizQuestionService quizQuestionService;

    @Autowired
    QuizUsersAnswersService quizUsersAnswersService;

    @Autowired
    QuizAnswerService quizAnswerService;
	
	public AddQuizView(){
		
	}
	private String inputWidth = "350px";
	private String inputWidthAnsw = "300px";
	
	@Data
	class QuestionBox{
		TextField question;
		
		TextField answA;
		Checkbox chAnsA;
		TextField answB;
		Checkbox chAnsB;
		TextField answC;
		Checkbox chAnsC;
		TextField answD;
		Checkbox chAnsD;
		
		Select<String> length;
		NumberField pointsAmount;
		
		boolean open=false;
		
		public QuestionBox(TextField question, TextField answA, Checkbox chAnsA, TextField answB, Checkbox chAnsB, TextField answC,
				Checkbox chAnsC, TextField answD, Checkbox chAnsD) {
			super();
			this.question = question;
			this.answA = answA;
			this.chAnsA = chAnsA;
			this.answB = answB;
			this.chAnsB = chAnsB;
			this.answC = answC;
			this.chAnsC = chAnsC;
			this.answD = answD;
			this.chAnsD = chAnsD;
			
			open=false;
		}



		QuestionBox(TextField question, Select<String> length, NumberField pointsAmount){
			this.question = question;
			this.length = length;
			this.pointsAmount = pointsAmount;
			
			open=true;
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
		
		RadioButtonGroup<String> randomOrder = new RadioButtonGroup<>();
		randomOrder.setLabel("Losowa kolejność");
		randomOrder.setItems("Tak", "Nie");
		randomOrder.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		randomOrder.setValue("Tak");
		add(randomOrder);
		
		RadioButtonGroup<String> forEveryone = new RadioButtonGroup<>();
		forEveryone.setLabel("Publiczne");
		forEveryone.setItems("Tak", "Nie");
		forEveryone.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
		forEveryone.setValue("Tak");
		add(forEveryone);
		
		NumberField durationSeconds = new NumberField("Czas trwania w sekundach");
		add(durationSeconds);
		durationSeconds.setWidth("200px");
		
		
		VerticalLayout questionHolder= new VerticalLayout();
		List<QuestionBox> questions = new ArrayList<>();
		
		HorizontalLayout addButtonLayout = new HorizontalLayout();
		Button addClosed = new Button("Dodaj pytanie zamknięte");
		addClosed.addClickListener(x->{
			TextField question = new TextField("Treść pytania");
			question.setWidth(inputWidth);
			
			TextField answA = new TextField("Odpowiedź A");
			Checkbox chAnsA = new Checkbox();
			TextField answB = new TextField("Odpowiedź B");
			Checkbox chAnsB = new Checkbox();
			TextField answC = new TextField("Odpowiedź C");
			Checkbox chAnsC = new Checkbox();
			TextField answD = new TextField("Odpowiedź D");
			Checkbox chAnsD = new Checkbox();
			
			answA.setWidth(inputWidthAnsw);
			answB.setWidth(inputWidthAnsw);
			answC.setWidth(inputWidthAnsw);
			answD.setWidth(inputWidthAnsw);
			
			
			questions.add(new QuestionBox(question, answA, chAnsA, answB, chAnsB, answC, chAnsC, answD, chAnsD));
			questionHolder.add(question, answA, chAnsA, answB, chAnsB, answC, chAnsC, answD, chAnsD);
			questionHolder.add(new Hr());
		});
		addButtonLayout.add(addClosed);
		
		Button addOpened = new Button("Dodaj pytanie otwarte");
		addOpened.addClickListener(x->{
			TextField question = new TextField("Treść pytania");
			question.setWidth(inputWidth);
			
			Select<String> length = new Select<String>();
			length.setItems("Długie", "Krótkie");
			length.setLabel("Długość odpowiedzi");
			//length.setWidth(inputWidth);
			
			NumberField pointsAmount = new NumberField("Punkty za pytanie");
			//pointsAmount.setWidth("inputWidth");
			
			questions.add(new QuestionBox(question, length, pointsAmount));
			questionHolder.add(question, length, pointsAmount);
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
			boolean randomOrderB = false;
			
			if(randomOrder.getValue().contentEquals("Tak"))
				randomOrderB=true;
			
			boolean forEveryoneB = false;
			
			if(forEveryone.getValue().contentEquals("Tak"))
				forEveryoneB=true;
			
			Quiz newQuiz = new Quiz(title.getValue(), description.getValue(), randomOrderB, Math.round(durationSeconds.getValue()), forEveryoneB, user, DigestUtils.sha256Hex(RandomStringUtils.random(30, true, true)));
			
			for(int i=0; i<questions.size(); i++)
			{
				QuestionBox q = questions.get(i);
				QuizQuestion quizQuestion = new QuizQuestion(q.getQuestion().getValue(), newQuiz, QuestionType.CLOSED);
				int points = 0;
				if(q.isOpen())
				{
					quizQuestion.setType(QuestionType.OPENED);		
					
					if(q.getLength().getValue().equals("Długie"))
						quizQuestion.setLong(true);
					else
						quizQuestion.setLong(false);
					
					points=(int) Math.round(q.getPointsAmount().getValue());
				}else {
					quizQuestion.setType(QuestionType.CLOSED);
					QuizAnswer a = new QuizAnswer(q.getAnswA().getValue(), q.getChAnsA().getValue(), quizQuestion);
					QuizAnswer b = new QuizAnswer(q.getAnswB().getValue(), q.getChAnsB().getValue(), quizQuestion);
					QuizAnswer c = new QuizAnswer(q.getAnswC().getValue(), q.getChAnsC().getValue(), quizQuestion);
					QuizAnswer d = new QuizAnswer(q.getAnswD().getValue(), q.getChAnsD().getValue(), quizQuestion);
					
					if(q.getChAnsA().getValue())
						points++;
					if(q.getChAnsB().getValue())
						points++;
					if(q.getChAnsC().getValue())
						points++;
					if(q.getChAnsD().getValue())
						points++;
						
//					quizAnswerService.addQuizAnswer(a);
//					quizAnswerService.addQuizAnswer(b);
//					quizAnswerService.addQuizAnswer(c);
//					quizAnswerService.addQuizAnswer(d);
					
					quizQuestion.getAnswers().add(a);
					quizQuestion.getAnswers().add(b);
					quizQuestion.getAnswers().add(c);
					quizQuestion.getAnswers().add(d);
				}
				quizQuestion.setPoints(points);
				newQuiz.getQuestions().add(quizQuestion);
			}

			quizService.addQuiz(newQuiz);
			
			getUI().get().getPage().setLocation("/");
		});
		add(register);
	}
}
