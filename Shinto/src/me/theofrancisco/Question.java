package me.theofrancisco;
import java.io.Serializable;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
//import java.util.UUID;

public class Question implements Serializable {
	/**
	 * 
	 */
	private String answerPath;  //Answer Path	
	private ArrayList<AnswerResult> results; 
	private int[] phases = {0,1,7,15,30};
	/* phases
	 * Dec 28, 2017. Dont need this anymore, since every right answer is going to count +1 and every wrong answer -1. 
	 * The standar sort will be by the pahase.
	 */
	private int phase;
	private long date;   //date in days of the file when was last modified
	private static final long serialVersionUID = 1L;
	private long questionID;
	private String path;		//Question Path
	;		
	private transient boolean seen;
	private char[] options;
	private transient int order;
	
	private String subject;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path2) {
		this.path = path2;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public String getAnswerPath() {
		return answerPath;
	}

	public void setAnswerPath(String path2) {
		this.answerPath = path2;
	}

	public long getDate() {
		return date;
	}

	public void setDate(FileTime fileTime) {
		this.date = fileTime.to(TimeUnit.DAYS);
	}

	
	public Question (long _questionID){
		questionID = _questionID;
		phase = 0;		
		results = new ArrayList<AnswerResult>();
	}

	public void setSubject(String subject) {
		this.subject = subject;
		
	}

	//return the last day that the questions was Answered
	//if the questions was never answered then returns null
	public Date getLastAnswerDate() {
		//if the results list has values
		if (results.size()>0) {
			return results.get(results.size()-1).getDate();
		}
		return null;		
	}

	public int getPhase() {				
		return phase;
	}

	public Object getNumber() {		
		return questionID;
	}

	public String getSubject() {		
		return subject;
	}
	
	public void setResult (boolean correct){
		//if correct = true then the answer is ok. Then move to the next phase
		AnswerResult ar = new AnswerResult();
		 ar.setCorrect(correct);				//Guarda el resultado de la respuesta		
		  if (correct){
			  phase++;
		  } else {
			  phase--;
		  }
		 results.add(ar);
	}

	public int getTotalCorrect(){
		int t=0;
		for (AnswerResult r: results){
			if (r.isCorrect())  t++;
		}
		return t;
	}
	
	public int getTotalWrong(){
		int t=0;
		for (AnswerResult r: results){
			if (!r.isCorrect())  t++;
		}
		return t;
	}

	public int totalAnswered() {		
		return results.size();
	}

	public void setOrder(int _order) {
		order = _order;		
	}

	public int getOrder() {		
		return order;
	}
	
	@Override
	public String toString (){
		String result="Question: "+ getPath()+"<br>";
		result+="Corrects: "+getTotalCorrect()+"<br>";
		result+="Wrongs: " + getTotalWrong()+"<br>";
		result+="Phase: "+getPhase()+"<br>";
		result+="Last Date Answered: "+getLastAnswerDate()+"<br><br>";
		 for (AnswerResult r: results){
			 result+=r+"<br>";
		 }			
		return result;
	}
}
