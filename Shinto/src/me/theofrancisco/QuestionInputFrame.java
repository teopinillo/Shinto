package me.theofrancisco;

/*
 * Adding image icon to a label
 * Image img = new ImageIcon(this.getClass().getResources("/resources.png")).getImage();
 * myLabel.setIcon( new ImageIcon(img));
 */
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.border.BevelBorder;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class QuestionInputFrame extends JFrame {

	private JPanel contentPane;
	private final JButton btnAddImg = new JButton("IMG");
	java.awt.Image img = new ImageIcon(this.getClass().getResource("/resources.png")).getImage();	
	//btnAddImg.setIcon( new ImageIcon(img) );

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionInputFrame frame = new QuestionInputFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QuestionInputFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblQuestion = new JLabel("Question");
		GridBagConstraints gbc_lblQuestion = new GridBagConstraints();
		gbc_lblQuestion.anchor = GridBagConstraints.WEST;
		gbc_lblQuestion.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuestion.gridx = 0;
		gbc_lblQuestion.gridy = 0;
		contentPane.add(lblQuestion, gbc_lblQuestion);
		
		JTextArea textArea = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 3;
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		contentPane.add(textArea, gbc_textArea);
		
		JButton btnAudio = new JButton("");
		btnAudio.setIcon(new ImageIcon(QuestionInputFrame.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaMuteDisabled.png")));
		GridBagConstraints gbc_btnAudio = new GridBagConstraints();
		gbc_btnAudio.insets = new Insets(0, 0, 5, 5);
		gbc_btnAudio.gridx = 1;
		gbc_btnAudio.gridy = 2;
		contentPane.add(btnAudio, gbc_btnAudio);
		GridBagConstraints gbc_btnAddImg = new GridBagConstraints();
		gbc_btnAddImg.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddImg.gridx = 2;
		gbc_btnAddImg.gridy = 2;
		contentPane.add(btnAddImg, gbc_btnAddImg);
		
		JLabel lblAnswer = new JLabel("Answer");
		GridBagConstraints gbc_lblAnswer = new GridBagConstraints();
		gbc_lblAnswer.anchor = GridBagConstraints.WEST;
		gbc_lblAnswer.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnswer.gridx = 0;
		gbc_lblAnswer.gridy = 3;
		contentPane.add(lblAnswer, gbc_lblAnswer);
		
		JTextArea textArea_1 = new JTextArea();
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.gridwidth = 3;
		gbc_textArea_1.insets = new Insets(0, 0, 5, 0);
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 4;
		contentPane.add(textArea_1, gbc_textArea_1);
		
		JButton btnDirectory = new JButton("Directory");
		GridBagConstraints gbc_btnDirectory = new GridBagConstraints();
		gbc_btnDirectory.insets = new Insets(0, 0, 5, 5);
		gbc_btnDirectory.gridx = 0;
		gbc_btnDirectory.gridy = 6;
		contentPane.add(btnDirectory, gbc_btnDirectory);
		
		JButton btnAddQuestion = new JButton("Add Question");
		GridBagConstraints gbc_btnAddQuestion = new GridBagConstraints();
		gbc_btnAddQuestion.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddQuestion.gridx = 2;
		gbc_btnAddQuestion.gridy = 6;
		contentPane.add(btnAddQuestion, gbc_btnAddQuestion);
		
		JLabel lblDirectory = new JLabel("Directory:");
		GridBagConstraints gbc_lblDirectory = new GridBagConstraints();
		gbc_lblDirectory.insets = new Insets(0, 0, 0, 5);
		gbc_lblDirectory.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblDirectory.gridx = 0;
		gbc_lblDirectory.gridy = 7;
		contentPane.add(lblDirectory, gbc_lblDirectory);
	}

}
