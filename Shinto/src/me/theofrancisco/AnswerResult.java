package me.theofrancisco;
import java.io.Serializable;
import java.util.Date;

public class AnswerResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Date date;
	private boolean correct;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
		this.date = new Date();
	}
	
	@Override
	public String toString(){
		String show;
		if (isCorrect()){
			show = String.format("%1$tD%2$10S", getDate(),"right");
		}else {
			show = String.format("%1$tD%2$10S", getDate(),"wrong");
		}			
		return show;
	}

}
