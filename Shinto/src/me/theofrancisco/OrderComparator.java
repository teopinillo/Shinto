package me.theofrancisco;

import java.io.Serializable;
import java.util.Comparator;

public class OrderComparator implements Comparator<Question>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// if a question is answered ok more time the phase is getting higher
	@Override
	public int compare(Question q1, Question q2) {
		// sort then by phase.
		return q1.getOrder() - q2.getOrder();
	}
}
