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

@Route("showQuiz")
@Push
public class ShowQuizView extends VerticalLayout implements HasUrlParameter<String>{
	String hashLink="";
	private String inputWidth = "240px";
	Quiz quiz;
	
	User user;
	
	@Autowired
	QuizService quizService;
	
	@Autowired
	QuizUsersAnswersService quizUsersAnswersService;
	
	@Autowired
	QuizUserAnswerService quizUserAnswerService;
	
	@Autowired
	UserService userService;
	
	UI ui;
	Thread timerCounter;
	AtomicBoolean running = new AtomicBoolean(true);
	
	class QuizInterior extends VerticalLayout{
		
		
		@Data
		class AnswerElement{
			QuizQuestion question;
			RadioButtonGroup<QuizAnswer> radioGroup;
			CheckboxGroup<QuizAnswer> checkboxGroup;
			TextField textField;
			TextArea textArea;
			QuestionType questionType;
			
			public AnswerElement(QuizQuestion question, RadioButtonGroup<QuizAnswer> radioGroup)
			{
				questionType=QuestionType.CLOSED;
				this.radioGroup=radioGroup;
				this.question=question;
			}
			public AnswerElement(QuizQuestion question, CheckboxGroup<QuizAnswer> checkboxGroup)
			{
				questionType=QuestionType.CLOSED_MULTI;
				this.checkboxGroup=checkboxGroup;
				this.question=question;
			}
			public AnswerElement(QuizQuestion question, TextField textField)
			{
				questionType=QuestionType.OPENED;
				this.textField=textField;
				this.question=question;
			}
			public AnswerElement(QuizQuestion question, TextArea textArea)
			{
				questionType=QuestionType.OPENED_LONG;
				this.textArea=textArea;
				this.question=question;
			}
		}
		String showTime(long seconds)
		{
			long hours = seconds/3600;
			seconds-=hours*3600;
			long minutes = seconds/60;
			seconds-=minutes*60;
			String res = String.format("%02d", hours)+":"+String.format("%02d", minutes)+":"+String.format("%02d", seconds);
			return res;
		}

		
		void summaryFunction(List<AnswerElement> answers)
		{
			QuizUsersAnswers quizUsersAnswers = new QuizUsersAnswers(user,quiz);
			for(AnswerElement answer:answers)
			{
				QuizQuestion q = answer.getQuestion();
				if(answer.getQuestionType()==QuestionType.CLOSED)
				{
					int pts=0;
					if(answer.getRadioGroup().getValue().isCorrect())
						pts=1;
					QuizUserAnswer quizUserAnswer = new QuizUserAnswer(Long.toString(answer.getRadioGroup().getValue().getId()), true, pts,quizUsersAnswers,q);
					quizUsersAnswers.getUserAnswers().add(quizUserAnswer);
				}else if(answer.getQuestionType()==QuestionType.CLOSED_MULTI)
				{
					int pts=0;
					StringJoiner joiner = new StringJoiner(",");
					for(QuizAnswer quizAnswer: answer.getCheckboxGroup().getValue())
					{
						if(quizAnswer.isCorrect())
							pts++;		
						joiner.add(Long.toString(quizAnswer.getId()));
					}
					
					QuizUserAnswer quizUserAnswer = new QuizUserAnswer(joiner.toString(), true, pts,quizUsersAnswers,q);
					quizUsersAnswers.getUserAnswers().add(quizUserAnswer);
					
					answer.getCheckboxGroup().getValue();
				}else if(answer.getQuestionType()==QuestionType.OPENED)
				{
					QuizUserAnswer quizUserAnswer = new QuizUserAnswer(answer.getTextField().getValue(), false, 0,quizUsersAnswers,q);
					quizUsersAnswers.getUserAnswers().add(quizUserAnswer);						
				}else if(answer.getQuestionType()==QuestionType.OPENED_LONG)
				{
					QuizUserAnswer quizUserAnswer = new QuizUserAnswer(answer.getTextArea().getValue(), false, 0,quizUsersAnswers,q);
					quizUsersAnswers.getUserAnswers().add(quizUserAnswer);
				}
			}
			quizUsersAnswersService.addQuizUsersAnswers(quizUsersAnswers);
			
			try {
				ui.access(() -> { 
					this.removeAll();
					
					add(new H1(quiz.getTitle()));
					add(new HtmlComponent("br"));
					add(new Hr());
					add(new HtmlComponent("br"));
					
					add(new H2("Punkty z zadan zamkniętych"));
					long fullPoints = quiz.getQuestions().stream().map(z->z.getPoints()).reduce(0, Integer::sum);
					long fullPointsClosed = quiz.getQuestions().stream().map(z->z.getAnswers().stream().filter(v->v.isCorrect()).count()).reduce((long) 0, Long::sum);
					long fullPointsOpened = fullPoints-fullPointsClosed;
					int gainedPoints = quizUsersAnswers.getUserAnswers().stream().map(z->z.getPoints()).reduce(0, Integer::sum);
					add(new H3("Punkty z zadan zamkniętych"));
					add(new H3(""+gainedPoints+"/"+fullPointsClosed+" punktów"));
					add(new H2("Punkty z zadan otwartych"));
					add(new H3("Do zdobycia "+fullPointsOpened+" punktów"));
					
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
		
		public QuizInterior(){
			MenuTemplate.addMenu(this);
			H3 timer = new H3(showTime(quiz.getDurationInSeconds()));
			add(new H1(quiz.getTitle()));
			add(new HtmlComponent("br"));
			add(new Hr());
			add(new H3("Czas do końca"));
			add(timer);
			add(new Hr());
			List<AnswerElement> answers = new ArrayList<>();

			
			timerCounter = new Thread(new Runnable() {
				int timeMadeSeconds = 0;
				@Override
				public void run() {
					while(running.get() && quiz.getDurationInSeconds()-timeMadeSeconds>0)
					{
						
						ui.access(() -> { 
							timer.setText(showTime(quiz.getDurationInSeconds()-timeMadeSeconds));
						});
						
						timeMadeSeconds++;
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
						}
					}
					summaryFunction(answers);
				}
				
			});
			timerCounter.start();
			
		
			for(QuizQuestion question:quiz.getShuffledQuestions())
			{
				add(new H3(question.getQuestion()));
				if(question.getType()==QuestionType.CLOSED)
				{
					List<QuizAnswer> shuffledAnswers = question.getShuffledAnswers();
					if(question.getGoodAnswerCount()==1)
					{
						RadioButtonGroup<QuizAnswer> radioGroup = new RadioButtonGroup<>();
						radioGroup.setItems(shuffledAnswers);
						radioGroup.setValue(shuffledAnswers.get(0));
						radioGroup.setRenderer(new TextRenderer<>(QuizAnswer::getDescription));
						radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
						add(radioGroup);
						answers.add(new AnswerElement(question, radioGroup));
					}else {
						CheckboxGroup<QuizAnswer> checkboxGroup = new CheckboxGroup<>();
						checkboxGroup.setItems(shuffledAnswers);
						checkboxGroup.setItemLabelGenerator(QuizAnswer::getDescription);
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
				running.set(false);
				timerCounter.interrupt();
				
			});
			add(send);
		}
	}
	
	class QuizAdminListInterior extends VerticalLayout{
		
		
		Label showUser(QuizUsersAnswers q)
		{
			
			return new Label(q.getUser().getName()+" "+q.getUser().getSurname());			
		}
		Label showPoints(QuizUsersAnswers q)
		{
			long fullPoints = q.getUserQuiz().getQuestions().stream().map(z->z.getPoints()).reduce(0, Integer::sum);
			int gainedPoints = q.getUserAnswers().stream().map(z->z.getPoints()).reduce(0, Integer::sum);
			return new Label(gainedPoints+"/"+fullPoints);			
		}
		Button showAdminGrading(QuizUsersAnswers q)
		{
			Button grade = new Button("Oceń");
			
			if(q.getUserAnswers().stream().filter(y->!y.isChecked()).count()>0)//jesli nie wszystkie sa sprawdzone
			{
				grade.addClickListener(x->{
					removeAll();
					add(new QuizAdminInterior(q));
				});
			}else {
				grade.setEnabled(false);
			}
			
			return grade;
		}
		public QuizAdminListInterior(){
			MenuTemplate.addMenu(this);
			add(new H1(quiz.getTitle()));
			add(new HtmlComponent("br"));
			add(new Hr());
			
			Grid<QuizUsersAnswers> questionsGrid = new Grid<>();//taka tabela
			questionsGrid.removeAllColumns();
			questionsGrid.setSelectionMode(SelectionMode.SINGLE);
			questionsGrid.setItems(quiz.getQuizUsersAnswers());//na liscie wszystkie wypełnione
			questionsGrid.addComponentColumn(this::showUser).setHeader("Użytkownik");//dodaje kto wypełniał
			questionsGrid.addComponentColumn(this::showPoints).setHeader("Punkty");
			questionsGrid.addComponentColumn(this::showAdminGrading).setHeader("Oceń");
			add(questionsGrid);
		}
	}
	class QuizAdminInterior extends VerticalLayout{
		
		void comeBack() {
			removeAll();
			add(new QuizAdminListInterior());
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
		public QuizAdminInterior(QuizUsersAnswers q){
			MenuTemplate.addMenu(this);
			add(new H1(quiz.getTitle()));
			add(new HtmlComponent("br"));
			add(new Hr());
			add(new H2(q.getUser().getName()+" "+q.getUser().getSurname()));
			add(new HtmlComponent("br"));
			add(new Hr());
			add(new HtmlComponent("br"));
			
			List<PointsForAnswer> points = new ArrayList<>();
			for(QuizUserAnswer quizUserAnswer : q.getUserAnswers().stream().filter(z->z.getQuizQuestion().getType()==QuestionType.OPENED || z.getQuizQuestion().getType()==QuestionType.OPENED_LONG).collect(Collectors.toList()))
			{
				add(new H3(quizUserAnswer.getQuizQuestion().getQuestion()));
				add(new H4(quizUserAnswer.getAnswer()));
				add(new H5("Maksymalna ilość punktów: "+quizUserAnswer.getQuizQuestion().getPoints()));
				NumberField numberField = new NumberField("Punkty");
				numberField.setValue(0d);
				numberField.setMin(0d);
				numberField.setMax(quizUserAnswer.getQuizQuestion().getPoints());
				points.add(new PointsForAnswer(numberField, quizUserAnswer));
				add(numberField);
				add(new Hr());
			}
			Button g = new Button("Zatwierdź");
			
			g.addClickListener(x->{
				for(PointsForAnswer point : points)
				{
					int pts = (int) Math.round(point.getNumberField().getValue());
					QuizUserAnswer qua = point.getQuizUserAnswer();
					qua.setPoints(pts);
					qua.setChecked(true);
					quizUserAnswerService.addQuizUsersAnswers(qua);
				}
				removeAll();
				add(new QuizAdminListInterior());
			});
			add(g);
		}
	}
	public ShowQuizView(){
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
				Optional<Quiz> quizOpt = quizService.getAll().stream().filter(x->x.getHashLink().equals(hashLink)).findFirst();
				
				if(quizOpt.isPresent())
				{
					quiz = quizOpt.get();
					if(quiz.getAuthor().getId()==user.getId()) {//tutaj administratorka
						removeAll();
						add(new QuizAdminListInterior());
					}else {
						
						if(quizUsersAnswersService.getAll().stream().filter(x->x.getUserQuiz().getId()==quiz.getId()).filter(x->x.getUser().getId()==user.getId()).findFirst().isPresent()) {
							add(new Label("Quiz został już wypełniony"));
						}else {
							add(new H1(quiz.getTitle()));
							add(new H3(quiz.getDescription()));
							add(new HtmlComponent("br"));
							
							Button start = new Button("Zacznij quiz");
							start.addClickListener(x->{
								this.removeAll();
								add(new QuizInterior());
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
		if(timerCounter!=null && timerCounter.isAlive())
		{
			running.set(false);
			timerCounter.interrupt();
		}
	}
	
}
