package me.theofrancisco;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Card implements Serializable {

	/**
	 * 
	 */

	private transient JFrame frame;
	private transient int questionIndex;
	public enum RetrieveQuestionMode  {STANDARD, NON_ANSWERED_FIRST, RANDOM};
	private RetrieveQuestionMode sortMode;

	private static final long serialVersionUID = 1L;
	private String setName; // Name of the root Directory
	private List<Question> questions;
	private int execIndex; // Which execution of the card set is.
	private File sourceDir = null;
	private String mainSubject;
	private Date lastSession;
	private File myLocation;
	private transient int todayCards;
	private transient int todayOkAnswers;
	private transient int todayWrongAnswers;

	public boolean hasNext() {
		if (questions.size() == 0)
			return false;
		return (questionIndex <= (questions.size() - 1));
	}

	public Question getNext() {	
		int i;
		Question question=null;
		switch (sortMode) {
		case RANDOM:
		case  STANDARD : {
			if (questionIndex < questions.size()) {				
				question = questions.get(questionIndex++);
			}
			break;
		}
		case NON_ANSWERED_FIRST: {
			if ( (i = getNextUnanswered() ) !=-1){
				question =  questions.get(i);
			}	
			break;
		}		
		default:
			break;		
		}
		if (question!=null){
			question.setSeen(true);
			todayCards++;
		}
		return question;
	}
	
	//Give every question a random number to sort
	private void shuffle(){
		for (Question q: questions){
			q.setOrder ((int) (questions.size()*Math.random()));
		}
	}
	//return numbers of questions no answered.
	private int unSeeQuestions() {
		int t=0;
		for (Question q: questions){
			if (!q.isSeen())t++;
		}
		return t;
	}

	public int getTodayCards() {
		return todayCards;
	}

	public int getTodayOkAnswers() {
		return todayOkAnswers;
	}

	public int getTodayWrongAnswers() {
		return todayWrongAnswers;
	}

	// Create a new set.
	// all files starting with Q are questions
	// all files starting with A and send ending as Q are answers
	// example Q01.txt
	// the answer must be A01. The extension does not matter.
	// The root Directory is the name of the Main Topic,
	// For Example JAVA
	// The other are subject for example:
	// Variables
	public boolean createSet(File file) {
		try {
			sourceDir = file;
			questions = new LinkedList<>();
			Files.walkFileTree(Paths.get(file.getPath()), new MyFileVisitor(questions, frame, mainSubject));
			setName = file.getName();			
			init();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, e.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	public void init() {		
			questionIndex = 0;
			todayCards=0;
			todayOkAnswers = 0;
			todayWrongAnswers = 0;
			for (Question q : questions){
				q.setSeen(false);
			}
			addExecIndex();		
	}

	public Card(JFrame frame) {
		sortMode = RetrieveQuestionMode.STANDARD;
		this.frame = frame;
	}

	public int size() {
		return questions.size();
	}

	public String getSetName() {
		return setName;
	}

	public void setFrame(JFrame _frame) {
		this.frame = _frame;

	}

	public void defaultSortCards() {		
		questionIndex=0;
		sortMode = RetrieveQuestionMode.STANDARD;
		questions.sort(new QuestionPriority());
	}
	
	public void randomSortCard (){
		shuffle();
		questions.sort( new OrderComparator());
	}

	public void print() {
		for (Question q : questions) {
			System.out.println(
					"\nSubject: " + q.getSubject() + "\nPath: " + q.getPath() + "\nAnswer" + q.getAnswerPath());
		}

	}
	
	public void restart(){
		questionIndex=0;
	}

	// TODO:
	/*
	 * <h1><strong>TEMA 1</strong></h1> <p><br /> Q1: <span
	 * style="background-color: #008000; color: #ffffff;">O</span> <span
	 * style="background-color: #ff0000; color: #ffffff;">X</span> <br />Q2:
	 * <span style="background-color: #ff0000; color: #ffffff;">X</span> <span
	 * style="color: #ffffff; background-color: #008000;">X</span> </p>
	 * <p>&nbsp;</p>
	 * 
	 */
	public String estadisticas() {
		String r = "<HTML>";
		r += "Questions in this session: " + questionIndex;
		return r += "</HTML>";

	}

	public String getSubject() {
		return mainSubject;
	}

	public void addExecIndex() {
		execIndex++;
		lastSession = new Date();
	}

	public int getExecIndex() {
		return execIndex;
	}

	//Where is the card set located. The Root directory for all the cards
	public File getSourceDir() {		
	  return sourceDir;
	}

	public void addQuestion(Question question) {		
		questions.add(question);	
	}

	public boolean Contains(Question question) {
		for (Question q:questions){
			if (q.getPath().equals(question.getPath() )) {
				return true;
			}
		}
		//JOptionPane.showConfirmDialog(frame, "Question: "+question.getPath().toString() + " is new?");
		return false;
	}

	public int noAnsweredYet() {
		int t=0;
		for (Question q:questions){
			if (q.getLastAnswerDate()==null) t++;
		}
		return t;
	}

	public String lastSessionRightAnswers() {
		
		return  "por implementar. comprobar la variable lastSession";
	}

	public String lastSessionWrongAnswers() {
		
		return  "por implementar. comprobar la variable lastSession";
	}
	
	public File getPath() {
		return myLocation;
	}
	
	public void setPath(File location){
		myLocation = location;
	}

	public int getQuestionCounter() {
		return this.questionIndex;
	}
	
	public int getNextUnanswered() {
		Question q;
		if (questionIndex < questions.size() ) {
		   q = questions.get(questionIndex);
		    while (q.getLastAnswerDate()!=null){
		    	if (questionIndex == questions.size()-1) {
		    		return -1;
		    	} else {		    		
		    		 q = questions.get(++questionIndex);
		    	}
		    }
		    return questionIndex;
		} else return -1;
	}
	
	

	public void setSortMode(RetrieveQuestionMode mode) {		
			sortMode = mode;
	}

	public void remove(Question question) {
		questions.remove(question);
		
	}

	public void setTodayWrongAnswers() {
		todayWrongAnswers++;		
	}

	public void setTodayOKAnswers() {
		todayOkAnswers++;		
	}
	
	public void setLastSession (Date date){
		lastSession = date;
	}
	
}
