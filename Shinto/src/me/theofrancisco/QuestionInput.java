package me.theofrancisco;

/*
 * Adding image icon to a label
 * Image img = new ImageIcon(this.getClass().getResources("/resources.png")).getImage();
 * myLabel.setIcon( new ImageIcon(img));
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.border.BevelBorder;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class QuestionInput extends JPanel {

	//private JPanel contentPane;
	static QuestionInput questionInput = null;
	private static final long serialVersionUID = 1L;
	JLabel dirLbl;
	JFileChooser fileChooser;
	JButton changeDirBtn;
	private JFrame mainPanel;
	private File directory;
	PanelInterfaz panelInterfaz;



	/**
	 * Create the frame.
	 */
	public QuestionInput(JFrame _mainPanel, File _directory, PanelInterfaz _panelInterfaz) {		
		panelInterfaz = _panelInterfaz;
		mainPanel = _mainPanel;
		directory = _directory;
		
		setBounds(100, 100, 630, 499);		
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gbl_contentPane);
		
		JLabel lblQuestion = new JLabel("Question");
		GridBagConstraints gbc_lblQuestion = new GridBagConstraints();
		gbc_lblQuestion.anchor = GridBagConstraints.WEST;
		gbc_lblQuestion.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuestion.gridx = 0;
		gbc_lblQuestion.gridy = 0;
		add(lblQuestion, gbc_lblQuestion);
		
		JTextArea textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 3;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		add(textArea, gbc_textArea);
		
		JButton btnAudio = new JButton("");
		btnAudio.setToolTipText("Insert Audio");
		btnAudio.setIcon(new ImageIcon(QuestionInput.class.getResource("/me/theofrancisco/images/Audio_File_24325.png")));
		GridBagConstraints gbc_btnAudio = new GridBagConstraints();
		gbc_btnAudio.insets = new Insets(0, 0, 5, 5);
		gbc_btnAudio.gridx = 1;
		gbc_btnAudio.gridy = 2;
		add(btnAudio, gbc_btnAudio);
		
		JButton btnInsertPicture = new JButton("");
		btnInsertPicture.setToolTipText("Insert Image");
		btnInsertPicture.setVerticalAlignment(SwingConstants.BOTTOM);
		btnInsertPicture.setIcon(new ImageIcon(QuestionInput.class.getResource("/me/theofrancisco/images/picture.png")));
		GridBagConstraints gbc_btnInsertPicture = new GridBagConstraints();
		gbc_btnInsertPicture.insets = new Insets(0, 0, 5, 0);
		gbc_btnInsertPicture.gridx = 2;
		gbc_btnInsertPicture.gridy = 2;
		add(btnInsertPicture, gbc_btnInsertPicture);
		
		JLabel lblAnswer = new JLabel("Answer");
		GridBagConstraints gbc_lblAnswer = new GridBagConstraints();
		gbc_lblAnswer.anchor = GridBagConstraints.WEST;
		gbc_lblAnswer.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnswer.gridx = 0;
		gbc_lblAnswer.gridy = 3;
		add(lblAnswer, gbc_lblAnswer);
		
		JTextArea textArea_1 = new JTextArea();
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.gridwidth = 3;
		gbc_textArea_1.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 4;
		add(textArea_1, gbc_textArea_1);
		
		JButton btnDirectory = new JButton("");
		btnDirectory.setToolTipText("Change Saving Directory");
		btnDirectory.setIcon(new ImageIcon(QuestionInput.class.getResource("/me/theofrancisco/images/directory.png")));
		btnDirectory.addActionListener(new ActionListener() {

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
				
		GridBagConstraints gbc_btnDirectory = new GridBagConstraints();
		gbc_btnDirectory.anchor = GridBagConstraints.EAST;
		gbc_btnDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_btnDirectory.gridx = 0;
		gbc_btnDirectory.gridy = 5;			
		add(btnDirectory, gbc_btnDirectory);
		
		JButton button_1 = new JButton("");
		button_1.setToolTipText("Insert Audio");
		button_1.setIcon(new ImageIcon(QuestionInput.class.getResource("/me/theofrancisco/images/Audio_File_24325.png")));
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.insets = new Insets(0, 0, 5, 5);
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 5;
		add(button_1, gbc_button_1);
		
		JButton button_2 = new JButton("");
		button_2.setToolTipText("Insert Image");
		button_2.setIcon(new ImageIcon(QuestionInput.class.getResource("/me/theofrancisco/images/picture.png")));
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.insets = new Insets(0, 0, 5, 0);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 5;
		add(button_2, gbc_button_2);
		
		JLabel lblDirectory = new JLabel("Directory:");
		GridBagConstraints gbc_lblDirectory = new GridBagConstraints();
		gbc_lblDirectory.insets = new Insets(0, 0, 0, 5);
		gbc_lblDirectory.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblDirectory.gridx = 0;
		gbc_lblDirectory.gridy = 7;
		add(lblDirectory, gbc_lblDirectory);
	}

	public static JPanel getQuestionInputPanel(JFrame _mainPanel, File _directory, PanelInterfaz _panelInterfaz) {

		if (questionInput == null) {
			questionInput = new QuestionInput(_mainPanel, _directory, _panelInterfaz);
		}
		return questionInput;
	}
}
