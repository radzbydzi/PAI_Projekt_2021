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
import pl.pai.pai.model.Survey;
import pl.pai.pai.model.SurveyAnswer;
import pl.pai.pai.model.SurveyQuestion;
import pl.pai.pai.model.SurveyUserAnswer;
import pl.pai.pai.model.SurveyUsersAnswers;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.security.SecurityUtils;
import pl.pai.pai.service.QuizService;
import pl.pai.pai.service.QuizUserAnswerService;
import pl.pai.pai.service.QuizUsersAnswersService;
import pl.pai.pai.service.SurveyAnswerService;
import pl.pai.pai.service.SurveyService;
import pl.pai.pai.service.SurveyUserAnswerService;
import pl.pai.pai.service.SurveyUsersAnswersService;
import pl.pai.pai.service.UserService;
import pl.pai.pai.views.ShowSurveyView.SurveyInterior.AnswerElement;

@Route("showSurvey")
@Push
public class ShowSurveyView extends VerticalLayout implements HasUrlParameter<String>{
	String hashLink="";
	private String inputWidth = "240px";
	Survey survey;
	
	User user;
	
	@Autowired
	SurveyService surveyService;
	
	@Autowired
	SurveyAnswerService surveyAnswerService;
	
	@Autowired
	SurveyUsersAnswersService surveyUsersAnswersService;
	
	@Autowired
	SurveyUserAnswerService surveyUserAnswerService;
	
	@Autowired
	UserService userService;
	
	UI ui;
	List<AnswerElement> answers = new ArrayList<>();
	void summaryFunction()
	{
		SurveyUsersAnswers surveyUsersAnswers = new SurveyUsersAnswers(user,survey);
		for(AnswerElement answer:answers)
		{
			SurveyQuestion q = answer.getQuestion();
			if(answer.getQuestionType()==QuestionType.CLOSED)
			{
				SurveyUserAnswer surveyUserAnswer = new SurveyUserAnswer(Long.toString(answer.getRadioGroup().getValue().getId()), surveyUsersAnswers,q);
				surveyUsersAnswers.getUserAnswers().add(surveyUserAnswer);
			}else if(answer.getQuestionType()==QuestionType.CLOSED_MULTI)
			{
				StringJoiner joiner = new StringJoiner(",");
				for(SurveyAnswer surveyAnswer: answer.getCheckboxGroup().getValue())
				{	
					joiner.add(Long.toString(surveyAnswer.getId()));
				}
				
				SurveyUserAnswer surveyUserAnswer = new SurveyUserAnswer(joiner.toString(), surveyUsersAnswers,q);
				surveyUsersAnswers.getUserAnswers().add(surveyUserAnswer);
				
				answer.getCheckboxGroup().getValue();
			}else if(answer.getQuestionType()==QuestionType.OPENED)
			{
				SurveyUserAnswer quizUserAnswer = new SurveyUserAnswer(answer.getTextField().getValue(), surveyUsersAnswers,q);
				surveyUsersAnswers.getUserAnswers().add(quizUserAnswer);						
			}else if(answer.getQuestionType()==QuestionType.OPENED_LONG)
			{
				SurveyUserAnswer quizUserAnswer = new SurveyUserAnswer(answer.getTextArea().getValue(), surveyUsersAnswers,q);
				surveyUsersAnswers.getUserAnswers().add(quizUserAnswer);
			}
		}
		surveyUsersAnswersService.addSurveyUsersAnswers(surveyUsersAnswers);
		
		try {
			ui.access(() -> { 
				this.removeAll();
				
				add(new H1(survey.getTitle()));
				add(new HtmlComponent("br"));
				add(new Hr());
				add(new HtmlComponent("br"));
				
				add(new H2("Dziękujemy za wypełnienie ankiety"));
				
				Button ret = new Button("Powróć do strony głównej");
				ret.addClickListener(z->{
					getUI().get().getPage().setLocation("/");
				});
				add(ret);
			});
			
			
		}catch(Exception e)
		{
			
		}
		
	}
	
	class SurveyInterior extends VerticalLayout{
		
		
		@Data
		class AnswerElement{
			SurveyQuestion question;
			RadioButtonGroup<SurveyAnswer> radioGroup;
			CheckboxGroup<SurveyAnswer> checkboxGroup;
			TextField textField;
			TextArea textArea;
			QuestionType questionType;
			
			public AnswerElement(SurveyQuestion question, RadioButtonGroup<SurveyAnswer> radioGroup)
			{
				questionType=QuestionType.CLOSED;
				this.radioGroup=radioGroup;
				this.question=question;
			}
			public AnswerElement(SurveyQuestion question, CheckboxGroup<SurveyAnswer> checkboxGroup)
			{
				questionType=QuestionType.CLOSED_MULTI;
				this.checkboxGroup=checkboxGroup;
				this.question=question;
			}
			public AnswerElement(SurveyQuestion question, TextField textField)
			{
				questionType=QuestionType.OPENED;
				this.textField=textField;
				this.question=question;
			}
			public AnswerElement(SurveyQuestion question, TextArea textArea)
			{
				questionType=QuestionType.OPENED_LONG;
				this.textArea=textArea;
				this.question=question;
			}
		}


		
		
		
		public SurveyInterior(){
			MenuTemplate.addMenu(this);
			add(new H1(survey.getTitle()));
			add(new HtmlComponent("br"));
			add(new Hr());

		
			
		
			for(SurveyQuestion question:survey.getQuestions())
			{
				add(new H3(question.getQuestion()));
				if(question.getType()==QuestionType.CLOSED || question.getType()==QuestionType.CLOSED_MULTI)
				{
					if(question.getType()==QuestionType.CLOSED)
					{
						RadioButtonGroup<SurveyAnswer> radioGroup = new RadioButtonGroup<>();
						radioGroup.setItems(question.getAnswers());
						radioGroup.setValue(question.getAnswers().get(0));
						radioGroup.setRenderer(new TextRenderer<>(SurveyAnswer::getDescription));
						radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
						add(radioGroup);
						answers.add(new AnswerElement(question, radioGroup));
					}else {
						CheckboxGroup<SurveyAnswer> checkboxGroup = new CheckboxGroup<>();
						checkboxGroup.setItems(question.getAnswers());
						checkboxGroup.setItemLabelGenerator(SurveyAnswer::getDescription);
						checkboxGroup.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
						add(checkboxGroup);
						answers.add(new AnswerElement(question, checkboxGroup));
					}
					
				}else {
					if(question.isLong())
					{
						TextArea textArea = new TextArea();
						textArea.setWidth(inputWidth);
						add(textArea);
						answers.add(new AnswerElement(question, textArea));
						
					}else {
						TextField textField = new TextField();
						textField.setWidth(inputWidth);
						add(textField);
						answers.add(new AnswerElement(question, textField));
					}
				}
			}
			Button send = new Button("Wyślij");
			send.addClickListener(x->{
				summaryFunction();
			});
			add(send);
		}
	}
	
	class SurveyAdminListInterior extends VerticalLayout{
		
		
		Label showUser(SurveyUsersAnswers q)
		{
			
			return new Label(q.getUser().getName()+" "+q.getUser().getSurname());			
		}
		Button showAdminGrading(SurveyUsersAnswers q)
		{
			Button grade = new Button("Zobacz");
			
			grade.addClickListener(x->{
				removeAll();
				add(new SurveyAdminInterior(q));
			});
			
			return grade;
		}
		public SurveyAdminListInterior(){
			MenuTemplate.addMenu(this);
			add(new H1(survey.getTitle()));
			add(new HtmlComponent("br"));
			add(new Hr());
			
			Grid<SurveyUsersAnswers> questionsGrid = new Grid<>();//taka tabela
			questionsGrid.removeAllColumns();
			questionsGrid.setSelectionMode(SelectionMode.SINGLE);
			questionsGrid.setItems(survey.getSurveyUsersAnswers());//na liscie wszystkie wypełnione
			questionsGrid.addComponentColumn(this::showUser).setHeader("Użytkownik");//dodaje kto wypełniał
			questionsGrid.addComponentColumn(this::showAdminGrading).setHeader("Zobacz");
			add(questionsGrid);
		}
	}
	class SurveyAdminInterior extends VerticalLayout{
		
		void comeBack() {
			removeAll();
			add(new SurveyAdminListInterior());
		}
		
		@Data
		class PointsForAnswer{
			NumberField numberField;
			QuizUserAnswer quizUserAnswer;
			public PointsForAnswer(NumberField numberField, QuizUserAnswer quizUserAnswer) {
				super();
				this.numberField = numberField;
				this.quizUserAnswer = quizUserAnswer;
			}
			
		}
		public SurveyAdminInterior(SurveyUsersAnswers q){
			MenuTemplate.addMenu(this);
			add(new H1(survey.getTitle()));
			add(new HtmlComponent("br"));
			add(new Hr());
			add(new H2(q.getUser().getName()+" "+q.getUser().getSurname()));
			add(new HtmlComponent("br"));
			add(new Hr());
			add(new HtmlComponent("br"));
			
			for(SurveyUserAnswer surveyUserAnswer : q.getUserAnswers())
			{
				add(new H3(surveyUserAnswer.getSurveyQuestion().getQuestion()));
				
				if(surveyUserAnswer.getSurveyQuestion().getType()==QuestionType.OPENED || surveyUserAnswer.getSurveyQuestion().getType()==QuestionType.OPENED_LONG)
				{

					add(new H4(surveyUserAnswer.getAnswer()));
				}else {
					String[] survAns = surveyUserAnswer.getAnswer().split(",");
					
					
					for(String s:survAns)
					{
						Optional<SurveyAnswer> a = surveyAnswerService.get(Long.parseLong(s));
						
						if(a.isPresent())
						{
							add(new H4(a.get().getDescription()));
						}else {
							add(new H4("[Brak odpowiedzi w bazie]"));
						}
					}
				}
				
				
				
				
				add(new Hr());
			}
			Button g = new Button("Powrót");
			
			g.addClickListener(x->{
				removeAll();
				add(new SurveyAdminListInterior());
			});
			add(g);
		}
	}
	public ShowSurveyView(){
		this.addDetachListener(x -> {
			System.out.println("Zdetaczowany");
			});
	}

	@PostConstruct
	void postConstruct() {

	}
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        ui = attachEvent.getUI();
        ui.getPage().executeJs("function closeListener() { $0.$server.windowClosed(); } " +
                "window.addEventListener('beforeunload', closeListener); " +
                "window.addEventListener('unload', closeListener);",getElement());
    }
	@Override
	public void setParameter(BeforeEvent event, @WildcardParameter String parameter) {
		MenuTemplate.addMenu(this);
		if(!parameter.isEmpty())
		{
			hashLink=parameter;
			user = userService.getAll().stream().filter(y->y.getEmail().equals(SecurityUtils.getLoggedUserName())).findFirst().get();
			if(!hashLink.isBlank())
			{
				Optional<Survey> surveyOpt = surveyService.getAll().stream().filter(x->x.getHashLink().equals(hashLink)).findFirst();
				
				if(surveyOpt.isPresent())
				{
					survey = surveyOpt.get();
					if(survey.getAuthor().getId()==user.getId()) {//tutaj administratorka
						removeAll();
						add(new SurveyAdminListInterior());
					}else {
						
						if(surveyUsersAnswersService.getAll().stream().filter(x->x.getUserSurvey().getId()==survey.getId()).filter(x->x.getUser().getId()==user.getId()).findFirst().isPresent()) {
							add(new Label("Ankieta została już wypełniony"));
						}else {
							add(new H1(survey.getTitle()));
							add(new H3(survey.getDescription()));
							add(new HtmlComponent("br"));
							
							Button start = new Button("Zacznij ankiete");
							start.addClickListener(x->{
								this.removeAll();
								add(new SurveyInterior());
							});
							add(start);
						}
						
					}
					
					
					
				}else {
					add(new Label("Brak quizu o podanym numerze"));
				}
			}
		}else {
			add(new Label("Brak numeru quizu"));
		}
	}

	@ClientCallable
	public void windowClosed() {
		//summaryFunction();
	}
	
}
