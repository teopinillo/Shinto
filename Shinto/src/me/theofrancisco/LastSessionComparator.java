package me.theofrancisco;
import java.util.Comparator;

public class LastSessionComparator implements Comparator<Question> {

		@Override
	public int compare(Question question1, Question question2) {
		return question1.getLastAnswerDate().compareTo(question2.getLastAnswerDate());		
	}

	
}
