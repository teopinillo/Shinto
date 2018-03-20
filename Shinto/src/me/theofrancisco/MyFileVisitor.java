package me.theofrancisco;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyFileVisitor extends SimpleFileVisitor<Path> implements Serializable {

	private transient Queue<String> subjects;
	private JFrame frame;
	private transient long questionCounter;
	
	public MyFileVisitor (List<Question> _questions, JFrame _frame, String mainSubject){
		this.questions = _questions;
		this.frame = _frame;
		subjects = new LinkedList<String>();
		questionCounter = 0;
	}
	
	private List<Question> questions;
	private static final long serialVersionUID = 1L;
	private boolean firstVisit = true;
	

	@Override
	public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes attrs) {
		// when enters in a new directory then adds to the the subjects
		if (firstVisit) {
			firstVisit = false;
		} else {
			subjects.add(path.getFileName().toString());
			
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path path, IOException ioe) {
		// when exists a directory then removes from the the subjects
		subjects.poll();
		if (ioe != null) {
			JOptionPane.showMessageDialog(frame, ioe.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
		// Si el archivo comienza con Q, asume que es una pregunta
		// y comprueba que este la respuesta
		if (isQuestion(path)) {
			Path answerPath = getAnswerPath(path);
			// si esta la respuesta, agrega la tarjeta
			if (answerPath != null) {
				Question question = new Question(questionCounter++);
				question.setPath(path.toString());
				question.setSubject(getSubject());
				question.setAnswerPath(answerPath.toString());
				try {
					question.setDate(Files.getLastModifiedTime(path));
				} catch (IOException e) {
					// if error se the current time
					JOptionPane.showMessageDialog(frame, e.getMessage(), "I/O error", JOptionPane.ERROR_MESSAGE);
					FileTime ft = FileTime.fromMillis(new Date().getTime());
					question.setDate(ft);

				}
				this.questions.add(question);
			} else {
				System.out.println("No Answer Found for: " + path);
			}
		}
		return FileVisitResult.CONTINUE;
	}
	
	
	private boolean isQuestion(Path path) {
		String filename = path.getFileName().toString();
		String ext = Util.getExtention (filename);
		switch (ext) {
		case "txt":
		case "png":{
			filename = filename.toUpperCase();
			return filename.startsWith("Q");
		}
		}
		return false;		
	}
	
	
	private Path getAnswerPath(Path questionPath, String ext) {
		String filename = questionPath.getFileName().toString();
		filename = "A" + filename.substring(1);
		filename = Util.removeExtension(filename) + ext;
		Path sourcePath = questionPath.getParent();
		return Paths.get(sourcePath.toString(), filename);
	}
	
	private Path getAnswerPath(Path path) {
		String[] extensions = { ".txt", ".png" };
		for (String e : extensions) {
			Path ans = getAnswerPath(path, e);
			if (Files.exists(ans))
				return ans;
		}
		return null;
	}
	
	// returns an String representing the subjects for the card
		public String getSubject() {
			String result = "";
			for (String s : subjects) {
				result += s + " ";
			}
			return result;
		}
	
}

