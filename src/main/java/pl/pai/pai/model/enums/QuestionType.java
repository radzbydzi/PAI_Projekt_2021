package pl.pai.pai.model.enums;

public enum QuestionType {
    CLOSED_MULTI("Zamknięte wielkorotnego wyboru"), CLOSED("Zamknięte jednokrotnego wyboru"), OPENED("Otwarte krótkie"), OPENED_LONG("Otwarte długie");
    
    String nazwa="";
	
	QuestionType(String nazwa)
	{
		this.nazwa=nazwa;
	}
}
