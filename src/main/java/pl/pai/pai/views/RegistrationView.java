package pl.pai.pai.views;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import pl.pai.pai.model.User;
import pl.pai.pai.service.UserService;

@Route("registration")
public class RegistrationView extends VerticalLayout{

	@Autowired
	UserService userService;
	@Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
	public RegistrationView(){
		
	}
	private String inputWidth = "240px";
	@PostConstruct
	void postConstruct() {
		add(new H1("Rejestracja"));
		TextField  name = new TextField ("Imię");
		name.setWidth(inputWidth);
		add(name);
		
		TextField  surname = new TextField ("Nazwisko");
		surname.setWidth(inputWidth);
		add(surname);
		
		EmailField email = new EmailField("Email");
		email.setWidth(inputWidth);
		add(email);
		
		PasswordField  password = new PasswordField("Hasło");
		password.setWidth(inputWidth);
		add(password);
		
		Button register = new Button("Zarejestruj się");
		
		register.addClickListener(x->{
			User newUser = new User(name.getValue(), surname.getValue(), email.getValue(), password.getValue());
			newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
			userService.addUser(newUser);
			getUI().get().getPage().setLocation("/login");
		});
		add(register);
	}
}
