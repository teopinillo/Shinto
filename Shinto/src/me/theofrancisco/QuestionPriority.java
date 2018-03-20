package me.theofrancisco;

import java.io.Serializable;
import java.util.Comparator;


public class QuestionPriority implements Comparator<Question>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	//if a question is answered ok more time the phase is getting higher	
	@Override
	public int compare(Question q1, Question q2) {		
		try{
		//sort then by phase.
		return q1.getPhase() - q2.getPhase();
		}catch (NullPointerException npe){
			System.err.println("getPhase. null pointer exception comparing phases in answers"); 
			System.err.println (q1);
			System.err.println(q2);
			System.exit(-1);
		}
		return 0;
	}
	
	

}
