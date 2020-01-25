package me.theofrancisco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;

//TODO:
//Cuando termine que no haya mas tarjetas, mostrar mensaje e invalidar el boton next
//mostrar las estadisticas:
//Total de Preguntas, Ok, Failed, %
//preguntar si desea comnezar de nuevo desde el principio
//
//Crear un boton que muestre las estadisticas:
// Total de preguntas: n
// Ultimas Respuestas Correctas:  
// Ultimas Respuestas Incorrectas:
// Preguntas Respondidas en esta sesion:
// Respuestas correctas en esta sesion:
// Respuestas correctas en esta sesion:
//
// La etiqueta question debe ser el numero de pregunta de la sesion

//Crear un save y un save as

//Permitir marcar una tarjeta como important y la opcion de ordenar las mas importantes primeros

//Mostrar cual es el nombre del fichero a guardar en la parte superior de la app

//Valorar guardar las respuestas.

//Que pasa si se quita una carta del directorio original, hay error? Habria que actualizar el set. Hacerlo cuando se se haga un updateSet.
//Hecho el 10/30/2017. Falta pr comprobar.

//https://en.wikipedia.org/wiki/Leitner_system

//Cuando se crea un  set y se da salir hay un error. de null point

//Si se ejecuta el programa desde otra pc y se cargan las tarjetas, al no encontrar las preguntas va eliminando las preguntas, solucion: 
//despues de cargar las tarjetas comprobar que exista el directorio si no existe, pedir que indique donde esta.

public class MainClass implements PanelInterfaz {

	final int MYAPP_WIDTH = 900;
	final int MYAPP_HEIGH = 600;

	private JFrame myApp;
	private Card currentCardSet;
	private Util util;
	private JLabel lblQuestion;
	public JScrollPane questionPanel;
	JButton btnNext;
	JLabel lblQuestionNumber;
	JLabel lblSubject;
	JLabel lblTotalOk;
	JLabel lblTotalFail;
	private Question currentQuestion = null;
	private boolean shownQuestion;
	private boolean dataModified = false;
	private File currentFile = null;
	private JButton btnFlipCard;
	private JButton btnIncorrect;
	private JButton btnCorrect;
	private JLabel lblSource;
	private JMenuItem mntmStatisctics;
	private JButton btnRefresh;
	private JButton[] optionsButton = new JButton[7];
	private Color defaultBckColor;
	private static final String versionDate = "01/25/2020";
	private JPanel buttonPanel;
	private JPanel panelInfo;
	private JPanel addQuestionPanel;
	private static MainClass window;
	private JButton btnNumbers;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainClass();
					window.myApp.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainClass() {
		initialize();
	}

	private void unselectAnswerButtons(Color _defaultBckColor) {
		for (JButton b : optionsButton) {
			b.setSelected(false);
			b.setBackground(_defaultBckColor);
			b.setOpaque(true);
		}
	}

	private void buttonChecked(ActionEvent e, Color defaultBckColor) {
		if (e.getSource() instanceof JButton) {
			JButton b = (JButton) e.getSource();
			b.setSelected(!b.isSelected());
			if (b.isSelected()) {
				b.setBackground(new Color(46, 139, 87));
			} else
				b.setBackground(defaultBckColor);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		myApp = new JFrame();
		myApp.setIconImage(Toolkit.getDefaultToolkit().getImage(MainClass.class.getResource("/me/theofrancisco/images/human_brain.png")));
		myApp.setTitle("Mastery");			
		myApp.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				// TODO:
				// Check if the current file is valid o is not null
				saveCard(currentFile);
				if (JOptionPane.showConfirmDialog(myApp, "Are you sure to close this window?", "Really Closing?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		util = new Util();
		myApp.setBounds(10, 52, 1200, 670);

		// myApp.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		BorderLayout appLayout = new BorderLayout();
		myApp.getContentPane().setLayout(appLayout);

		// -------------------------------------------------------------------Information
		// Panel, On The Top

		// The FlowLayout class puts components in a row, sized at their preferred size
		panelInfo = new JPanel();
		FlowLayout infoPanelLayout = new FlowLayout();
		infoPanelLayout.setAlignment(FlowLayout.LEFT);
		panelInfo.setLayout(infoPanelLayout);

		lblSubject = new JLabel("Subject:");
		lblSubject.setPreferredSize(new Dimension(200, 20));
		panelInfo.add(lblSubject);

		lblQuestionNumber = new JLabel("Question:");
		lblQuestionNumber.setPreferredSize(new Dimension(180, 20));
		panelInfo.add(lblQuestionNumber);

		lblTotalOk = new JLabel("OK:");
		lblTotalOk.setPreferredSize(new Dimension(50, 20));
		panelInfo.add(lblTotalOk);

		lblTotalFail = new JLabel("Fail: ");
		lblTotalFail.setPreferredSize(new Dimension(100, 20));
		panelInfo.add(lblTotalFail);

		lblSource = new JLabel("Source:");
		lblSource.setPreferredSize(new Dimension(500, 20));
		panelInfo.add(lblSource);

		myApp.getContentPane().add(panelInfo, BorderLayout.PAGE_START);

		// -------------------------------------------------------------------
		// Information Panel, ends

		// -------------------------------------------------------------------
		// Question Panel

		// scrollPane.setBounds(10, 52, 1216, 496);

		lblQuestion = new JLabel();
		questionPanel = new JScrollPane(lblQuestion);
		questionPanel.setPreferredSize(new Dimension(1200, 500));
		// scrollPane.setColumnHeaderView(lblQuestion);

		myApp.getContentPane().add(questionPanel, BorderLayout.CENTER);

		// --------------------------------------------------------------------
		// Question Panel Definition Ends

		// --------------------------------------------------------------------
		// Defining the answer's button panel
		{
			buttonPanel = new JPanel();
			// BoxLayout buttonPanelLayout = new BoxLayout(buttonPanel, BoxLayout.X_AXIS);
			FlowLayout buttonPanelLayout = new FlowLayout();
			buttonPanelLayout.setAlignment(FlowLayout.RIGHT);
			infoPanelLayout.setAlignment(FlowLayout.LEFT);
			buttonPanel.setLayout(buttonPanelLayout);

			optionsButton = new JButton[7];
			String letters[] = { "A", "B", "C", "D", "E", "F", "G", "H" };

			defaultBckColor = new JButton().getBackground();

			for (int i = 0; i < optionsButton.length; i++) {
				optionsButton[i] = new JButton(letters[i]);

				optionsButton[i].setBorder(UIManager.getBorder("Button.border"));
				optionsButton[i].setSelected(false);
				optionsButton[i].setOpaque(true);
				optionsButton[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				optionsButton[i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						buttonChecked(e, defaultBckColor);
					}
				});
				buttonPanel.add(optionsButton[i]);
			}

			// +++++++++++++++++++++++++++++++++++++++++++++++INCORRECT BUTTON
			btnIncorrect = new JButton("Incorrect");
			// Image img = new ImageIcon
			// (this.getClass().getResource("/wrong.png")).getImage();
			btnIncorrect.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/wrong.png")));
			btnIncorrect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (currentQuestion != null) {
						currentQuestion.setResult(false);
						btnNext.setEnabled(true);
						btnCorrect.setEnabled(false);
						dataModified = true;
						currentCardSet.setTodayWrongAnswers();
					}
				}
			});

			btnNumbers = new JButton("");
			btnNumbers.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/numbers.png")));
			buttonPanel.add(btnNumbers);

			// +++++++++++++++++++++++++++++++++++++++++++++++ CORRECT BUTTON
			btnCorrect = new JButton("Correct");
			btnCorrect.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/ok.png")));
			btnCorrect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (currentQuestion != null) {
						currentQuestion.setResult(true);
						btnNext.setEnabled(true);
						btnIncorrect.setEnabled(false);
						dataModified = true;
						currentCardSet.setTodayOKAnswers();
					}
				}
			});
			buttonPanel.add(btnCorrect);

			//
			// +++++++++++++++++++++++++++++++++++++++++++++++ FLIP BUTTON
			btnFlipCard = new JButton("Flip Card");
			btnFlipCard.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/flip.png")));
			btnFlipCard.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if ((shownQuestion) && (currentQuestion != null)) {
						showAnswer();
					} else {
						if (currentQuestion != null) {
							showQuestion();
						}
					}
				}
			});
			buttonPanel.add(btnFlipCard);

			// +++++++++++++++++++++++++++++++++++++++++++++++ BUTTON REFRESH
			btnRefresh = new JButton("Refresh");
			btnRefresh.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/refresh.png")));
			btnRefresh.setSelectedIcon(
					new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/refresh.png")));
			btnRefresh.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					refresh();
				}
			});
			buttonPanel.add(btnRefresh);

			// +++++++++++++++++++++++++++++++++++++++++++++++ NEXT BUTTON
			btnNext = new JButton("Next");
			btnNext.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/go-next.png")));
			btnNext.setSelectedIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/go-next.png")));
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// if there is a working card set
					if (currentCardSet != null) {
						// get the next card
						showNextQuestion();
					}
				}
			});
			buttonPanel.add(btnNext);

			myApp.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
			// --------------------------------------------------------------------
			// End Defining the answer's button panel
		}

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++CREATE MENU

		JMenuBar menuBar = new JMenuBar();
		myApp.setJMenuBar(menuBar);

		JMenu mnSet = new JMenu("Card Set");
		menuBar.add(mnSet);
		JMenuItem mntmCreate = new JMenuItem("Create");
		mntmCreate.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/folder-new.png")));

		mntmCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int v = fc.showOpenDialog(myApp);
				if (v == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					currentCardSet = new Card(myApp);
					currentCardSet.createSet(file);
					lblSubject.setText(currentCardSet.getSubject());
					myApp.setTitle("Mastery. Study: " + currentCardSet.getSetName());
					JOptionPane.showMessageDialog(myApp, "New Set Created With " + currentCardSet.size(), "Set Created",
							JOptionPane.INFORMATION_MESSAGE);
					showNextQuestion();
					currentFile = null;
					enableNavigation(true);
				}
			}
		});
		mnSet.add(mntmCreate);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++OPEN MENU
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/folder_open-accept.png")));
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadCardSet();
			}
		});
		mnSet.add(mntmOpen);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++Save MENU
		JMenuItem mntmSave = new JMenuItem("Save");
		mntmSave.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/save_accept.png")));
		mntmSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveCard();
			}
		});
		mnSet.add(mntmSave);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++CLOSE MENU
		JMenuItem mntmClose = new JMenuItem("Close");
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//currentCardSet = null;
				//lblSubject.setText("Subject:");
				//lblQuestionNumber.setText("");				
				//questionPanel.setViewportView(lblQuestion);
				//enableNavigation(false);
				//currentQuestion = null;
				if (dataModified) {
					saveCard();					
				}
				if (JOptionPane.showConfirmDialog(myApp, "Are you sure to close this window?", "Really Closing?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});

		// +++++++++++++++++++++++++++++++++++++++++++++++++++UPDATE MsetIcon(new
		// ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/folder_closed-delete3.png")));

		JMenuItem mntmUpdate = new JMenuItem("Update");
		mntmUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				upDateCardSet();
			}
		});
		JMenuItem mntmSort = new JMenuItem("Save As");
		mnSet.add(mntmSort);
		mnSet.add(mntmUpdate);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++STATICS MENU
		mntmStatisctics = new JMenuItem("Statisctics");
		mntmStatisctics.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String info = "<HTML>";
				info += "Number of Questions: " + currentCardSet.size();
				info += "<br>No Answered Yet: " + currentCardSet.noAnsweredYet();
				info += "<br><br>Today's Session:";
				info += "<br><br>Questions: " + currentCardSet.getTodayCards();
				info += "<br> Right Answers: " + currentCardSet.getTodayOkAnswers();
				info += "<br> Wrong Answers: " + currentCardSet.getTodayWrongAnswers();
				info += "<br>" + currentQuestion;
				info += "</HTML>";
				lblQuestion = new JLabel(info);
				questionPanel.setViewportView(lblQuestion);

			}
		});

		// +++++++++++++++++++++++++++++++++++++++++++++++++++ Default Order

		JMenu mnSort = new JMenu("Sort");
		mnSort.setIcon(new ImageIcon(MainClass.class.getResource("/me/theofrancisco/images/sort25x25.png")));
		mnSet.add(mnSort);

		JMenuItem mntmDefault = new JMenuItem("Default (Leitner System)");
		mntmDefault.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentCardSet.defaultSortCards();
			}
		});
		mnSort.add(mntmDefault);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++ Non Answered Yet

		JMenuItem mntmNonAnsweredYet = new JMenuItem("Non Answered Yet");
		mntmNonAnsweredYet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int r = currentCardSet.noAnsweredYet();
				if (r > 0) {
					currentCardSet.setSortMode(Card.RetrieveQuestionMode.NON_ANSWERED_FIRST);
					JOptionPane.showMessageDialog(myApp, "Found " + r + " Unanswered Question(s)", "Question(s) FOund!",
							JOptionPane.INFORMATION_MESSAGE);
					showNextQuestion();
				} else {
					JOptionPane.showMessageDialog(myApp, "All Questions had been Aswered at least one\n", "No Found",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		mnSort.add(mntmNonAnsweredYet);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++ SET RANDOM SORT
		JMenuItem mntmRandom = new JMenuItem("Random");
		mntmRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currentCardSet.init();
				currentCardSet.randomSortCard();
			}
		});
		mnSort.add(mntmRandom);

		// +++++++++++++++++++++++++++++++++++++++++++++++++++ MENU ADD QUESTION
		// show Panel for adding Questions Button
		JMenuItem mntmAddQuestion = new JMenuItem("Add Question");
		mntmAddQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				myApp.setSize(505, 530);
				if (currentCardSet != null) {
					addQuestionPanel = QuestionInput.getQuestionInputPanel(myApp, currentCardSet.getSourceDir(),
							window);
				} else {
					String d = System.getProperties().getProperty("user_home", ".\\");
					addQuestionPanel = QuestionInput.getQuestionInputPanel(myApp, new File(d), window);
				}
				setPanelsVisible(false);
				myApp.getContentPane().add(addQuestionPanel);
			}
		});
		mnSet.add(mntmAddQuestion);
		mnSet.add(mntmStatisctics);
		mnSet.add(mntmClose);
		enableNavigation(false);

		// frame.getContentPane().add(yellowLabel, BorderLayout.CENTER);
	}

	private void enableNavigation(boolean op) {
	}

	private void showQuestion() {

		if (currentQuestion != null) {
			// unselectAnswerButtons(defaultBckColor);
			lblSubject.setText("Subject: " + currentQuestion.getSubject());
			lblQuestionNumber.setText("Question: " + currentCardSet.getTodayCards());
			lblTotalOk.setText("Ok: " + currentQuestion.getTotalCorrect());
			lblTotalFail.setText("Fails: " + currentQuestion.getTotalWrong());
			show(currentQuestion.getPath().toString());
			shownQuestion = true;
			lblSource.setText("Source: " + currentQuestion.getPath());
		} else {

		}
	}

	private void showNextQuestion() {
		if ((currentQuestion = currentCardSet.getNext()) != null) {
			unselectAnswerButtons(defaultBckColor);
			showQuestion();
			btnCorrect.setEnabled(true);
			btnIncorrect.setEnabled(true);
			btnNext.setEnabled(false);
		} else {
			lblQuestion.setText("No more questions to show.");
			enableNavigation(false);
		}
	}

	public void showAnswer() {
		show(currentQuestion.getAnswerPath().toString());
		shownQuestion = false;
	}

	public void show(String fileSource) {
		String debugStep = "";
		String ext = Util.getExtention(fileSource);
		switch (ext) {
		case "png": {
			lblQuestion = new JLabel(new ImageIcon(fileSource));
			questionPanel.setViewportView(lblQuestion);
			break;
		}
		case "txt": {
			if (!Files.exists(Paths.get(fileSource))) {
				String err = "\nQuestion: " + currentQuestion.getPath();
				err += "not found in Path. Card will be removed.";
				JOptionPane.showMessageDialog(myApp, err, "I/O error", JOptionPane.ERROR_MESSAGE);
				currentCardSet.remove(currentQuestion);
				showNextQuestion();
				return;
			}
			try {
				debugStep = "reading lines";
				List<String> lines = new ArrayList<>();
				lines = Files.readAllLines(Paths.get(fileSource));
				String allLines = "";
				debugStep = "Adding /\n to Lines.";
				for (String l : lines) {
					allLines += l + "\n";
				}
				debugStep = "String to htmlString";
				allLines = util.stringToHtmlString(allLines);
				lblQuestion = new JLabel(allLines);
				lblQuestion.setBackground(Color.WHITE);
				questionPanel.setViewportView(lblQuestion);
			}

			catch (MalformedInputException mie) {
				openInNotepad(fileSource);
				mie.printStackTrace();
				String err = "\nFile: " + fileSource + "\n "
						+ "Error in the charset. Malformed Input. Please check the file.";
				JOptionPane.showMessageDialog(myApp, err, "I/O error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException ioe) {
				ioe.printStackTrace();
				String err = "\nFile: " + fileSource + "\n " + debugStep;

				JOptionPane.showMessageDialog(myApp,
						"Error in show()\n " + ioe.getMessage() + "\n" + ioe.getLocalizedMessage() + err, "I/O error",
						JOptionPane.ERROR_MESSAGE);
			}
			break;
		}
		case "": {
			JOptionPane.showMessageDialog(myApp, "No extension in: " + fileSource, "Card error",
					JOptionPane.ERROR_MESSAGE);
		}
		}
	}

	private void openInNotepad(String fileSource) {
		String os = System.getProperty("os.name");
		if (os.contains("Windows")) {
			if (Files.exists(Paths.get("C:Windows\\system32\\notepad.exe"))) {
				Runtime run = Runtime.getRuntime();
				try {
					Process proc = run.exec("C:Windows\\system32\\notepad.exe " + fileSource);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(myApp,
							"Could not open C:Windows\\system32\\notepad.exe " + fileSource, "IO error",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		}
	}

	public boolean saveCard() {

		JFileChooser fc = new JFileChooser();
		int v = fc.showSaveDialog(myApp);
		if (v == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return saveCard(file);
		}
		return false;
	}

	private boolean saveCard(File file) {
		if (currentCardSet != null) {
			try {
				currentCardSet.setLastSession(new Date()); // Set the date for
															// last Session
				FileOutputStream fileOut = new FileOutputStream(file);
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(currentCardSet);
				objOut.close();
				fileOut.close();
				JOptionPane.showMessageDialog(myApp, "Set saved in " + file.getAbsolutePath(), "Set saved.",
						JOptionPane.INFORMATION_MESSAGE);
				return true;

			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(myApp, "saveCard()\n " + ioe.getMessage(), "I/O error",
						JOptionPane.ERROR_MESSAGE);
				ioe.printStackTrace();
			}
		}
		return false;
	}

	public boolean loadCardSet() {

		try {
			// open the last location

			AppProperties appProperties = new AppProperties();
			String lastDir = appProperties.getLastSetDirectory();

			FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("Card Set File", "set");

			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(fileFilter);

			if (lastDir != null) {
				fc.setCurrentDirectory(new File(lastDir));
			}

			int v = fc.showOpenDialog(myApp);
			if (v == JFileChooser.APPROVE_OPTION) {
				currentFile = fc.getSelectedFile();

				// save the directory in the properties file

				FileInputStream fileIn = new FileInputStream(currentFile);
				appProperties.setOpenedDirectory(currentFile.getParent());
				ObjectInputStream objIn = new ObjectInputStream(fileIn);
				currentCardSet = (Card) objIn.readObject();
				objIn.close();
				fileIn.close();

				// ---------------------------------------
				currentCardSet.setFrame(myApp);
				myApp.setTitle("ShintoFlashCard. Study: " + currentCardSet.getSetName());
				JOptionPane.showMessageDialog(myApp, "Card Set Loaded With " + currentCardSet.size(), " Cards",
						JOptionPane.INFORMATION_MESSAGE);
				currentCardSet.init();
				currentCardSet.setPath(currentFile);
				currentCardSet.defaultSortCards();
				showNextQuestion();
				enableNavigation(true);
				return true;
			}
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(myApp, "loadCard()\n " + ioe.getMessage(), "I/O error",
					JOptionPane.ERROR_MESSAGE);
			ioe.printStackTrace();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(myApp, "loadCard()\n " + e.getMessage(), "Class error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return false;
	}

	public void upDateCardSet() {
		Card compareCardSet;
		Question question;
		int startSize = currentCardSet.size();
		File sourceDir = currentCardSet.getSourceDir();

		if (sourceDir != null) {
			compareCardSet = new Card(myApp);
			compareCardSet.createSet(sourceDir);
			while (compareCardSet.hasNext()) {
				question = compareCardSet.getNext();
				// if the path no longer exists then remove it form the
				// questions list
				if (!Files.exists(Paths.get(question.getPath()))) {
					currentCardSet.remove(question);
					System.out.println("Removed: " + question.getPath());
				} else if (!currentCardSet.Contains(question)) {
					currentCardSet.addQuestion(question);
				}
			}
		}

		int endSize = currentCardSet.size();

		if (endSize > startSize) {
			JOptionPane.showMessageDialog(myApp, "Card Set Upadted with  " + (endSize - startSize), " Cards",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(myApp, "No new cards were founds  ", " Cards",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void refresh() {
		if (currentQuestion != null) {
			if (shownQuestion)
				showQuestion();
			else
				showAnswer();
		}
	}

	@Override
	public void setPanelsVisible(boolean visible) {

		if (visible) {
			// Restore my App
			addQuestionPanel.setVisible(false);
			myApp.getContentPane().remove(addQuestionPanel);
			addQuestionPanel = null;
			buttonPanel.setVisible(visible);
			questionPanel.setVisible(visible);
			panelInfo.setVisible(visible);
			myApp.setBounds(10, 52, 1250, 670);
		} else {
			// Shows only New/Add Question Panel.
			addQuestionPanel.setVisible(true);
			buttonPanel.setVisible(visible);
			questionPanel.setVisible(visible);
			panelInfo.setVisible(visible);
		}

		// frmShintoFlashCard.getContentPane().add(panel);

	}
}
