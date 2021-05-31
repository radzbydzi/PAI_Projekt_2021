package pl.pai.pai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quizes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String description;

    @OneToMany(
            mappedBy = "quiz",
            cascade = CascadeType.ALL
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<QuizQuestion> questions = new ArrayList<>();

    @OneToMany(
            mappedBy = "userQuiz",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    List<QuizUsersAnswers> quizUsersAnswers = new ArrayList<>();

    boolean randomOrder = false;


    Long durationInSeconds;

    boolean forEveryone = true;


    @ManyToOne(fetch = FetchType.EAGER)
    User author;

    String hashLink;

    public List<QuizQuestion> getShuffledQuestions()
    {
    	List<QuizQuestion> res = new ArrayList<>();
    	res.addAll(questions);
    	if(randomOrder)
    	{
    		Collections.shuffle(res);
    	}
    	return res;
    }
    
    public Quiz(String title, String description, boolean randomOrder, Long durationInSeconds, boolean forEveryone, User author, String hashLink) {
        this.title = title;
        this.description = description;
        this.randomOrder = randomOrder;
        this.durationInSeconds = durationInSeconds;
        this.forEveryone = forEveryone;
        this.author = author;
        this.hashLink = hashLink;
    }
}
