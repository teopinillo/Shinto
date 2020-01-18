package me.theofrancisco;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class QuestionInput extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel dirLbl;
	JFileChooser fileChooser;
	JButton changeDirBtn;
	private JFrame mainPanel;
	private File directory;
	PanelInterfaz panelInterfaz;

	JLabel questionLbl = new JLabel("Question");
	JLabel answerLbl = new JLabel("Answer");

	JButton addBtn;
	JButton cancelBtn;

	JTextArea txtQuestion = new JTextArea(10, 40);
	JScrollPane questionScroll = new JScrollPane(txtQuestion);

	JTextArea txtAnswer = new JTextArea(10, 40);
	JScrollPane answerScroll = new JScrollPane(txtAnswer);

	GridBagLayout layout = new GridBagLayout();
	GridBagConstraints constraints;
	GridBagConstraints constraints2;
	
	static QuestionInput questionInput = null;
	
	private GridBagConstraints createGridBagConstraint(int fill, int gridx, int gridy, int gridWidth, Insets intsets) {
		GridBagConstraints newConstraints = new GridBagConstraints();
		 newConstraints.fill = fill;
		 newConstraints.gridx = gridx;
		 newConstraints.gridy = gridy;
		 newConstraints.gridwidth = gridWidth;
		 newConstraints.insets = intsets;
		 
		return newConstraints;
	}

	private QuestionInput(JFrame _mainPanel, File _directory, PanelInterfaz _panelInterfaz) {
		panelInterfaz = _panelInterfaz;
		mainPanel = _mainPanel;
		directory = _directory;

		setBounds(5, 5, 480, 460);
		// General insets
		Insets insets = new Insets(3, 3, 3, 3);

		// questionScroll.setPreferredSize( new Dimension (200,50));

		setLayout(layout);
		// adding label directory
		dirLbl = new JLabel(directory.getAbsolutePath());
		constraints = createGridBagConstraint(GridBagConstraints.HORIZONTAL,0,0,8,insets);
		dirLbl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		add(dirLbl, constraints);

		// adding button "Change Directory"
		changeDirBtn = new JButton("Change Dir");
		changeDirBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Creates a file chooser
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int v = fc.showOpenDialog(mainPanel);
				if (v == JFileChooser.APPROVE_OPTION) {
					directory = fc.getSelectedFile();
					if (directory.toString().length() > 50) {
						dirLbl.setText(directory.toString().substring(0, 50));
					} else {
						dirLbl.setText(directory.toString());
					}
				}
			}
		});
		constraints2 = createGridBagConstraint (GridBagConstraints.HORIZONTAL,8,12,2,insets);		
		//add(changeDirBtn, constraints2);
		add(changeDirBtn);
		// adding label question
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(questionLbl, constraints);

		// adding question text area
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 10;
		add(questionScroll, constraints);

		// adding label Answer
		constraints.gridx = 0;
		constraints.gridy = 10;
		constraints.gridwidth = 10;
		add(answerLbl, constraints);

		// adding answer text area
		constraints.gridx = 0;
		constraints.gridy = 11;
		constraints.gridwidth = 10;
		add(answerScroll, constraints);

		// adding Add Cancel Button
		cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panelInterfaz.setPanelsVisible(true);
			}
		});
		constraints.gridx = 6;
		constraints.gridy = 12;
		constraints.gridwidth = 1;
		add(cancelBtn, constraints);

		// adding Add Question Button
		addBtn = new JButton("Add Question");
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File tmpFile = File.createTempFile("QST_", ".txt", directory);
					FileWriter fw = new FileWriter(tmpFile);
					BufferedWriter bw = new BufferedWriter(fw);
					String text = txtQuestion.getText();
					bw.write(text);
					bw.close();

					// saving the answer
					// 1-Get the name of the Question File, and replace QST by ANS
					Path ansPath = tmpFile.toPath();
					String fName = ansPath.getFileName().toString().replaceFirst("QST_", "ANS_");
					ansPath = Paths.get(ansPath.getParent().toString(), fName);
					// writing to the file
					fw = new FileWriter(ansPath.toFile());
					bw = new BufferedWriter(fw);
					text = txtAnswer.getText();
					bw.write(text);
					bw.close();
					txtAnswer.setText("");
					txtQuestion.setText("");
					txtQuestion.requestFocusInWindow();

				} catch (IOException ex) {
					JOptionPane.showInternalMessageDialog(mainPanel.getContentPane(), ex.getMessage(), "Error I/O",
							ERROR);
				}
			}

		});
		constraints.gridx = 7;
		constraints.gridy = 12;
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		add(addBtn, constraints);

		// Panel properties
		// this.setPreferredSize( new Dimension (500,400));
		setVisible(true);
		setBorder(BorderFactory.createLineBorder(Color.PINK));
	}

	public static JPanel getQuestionInputPanel(JFrame _mainPanel, File _directory, PanelInterfaz _panelInterfaz) {

		if (questionInput == null) {
			questionInput = new QuestionInput(_mainPanel, _directory, _panelInterfaz);
		}
		return questionInput;
	}

}
