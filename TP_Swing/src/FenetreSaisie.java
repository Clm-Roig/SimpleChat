import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FenetreSaisie extends JFrame {
	
	private JButton bResume;
	private JButton bQuit;
	private String nom = "";
	private String prenom = "";
	private String tel = "";
	private JTextArea textArea;
	private JTextField Tfnom;
	private JTextField Tfprenom;
	private JTextField Tftel;
	
	public JButton getbResume() {
		return bResume;
	}
	public void setbResume(JButton bResume) {
		this.bResume = bResume;
	}
	public JButton getbQuit() {
		return bQuit;
	}
	public void setbQuit(JButton bQuit) {
		this.bQuit = bQuit;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public JTextArea getTextArea() {
		return textArea;
	}
	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

	private class EcouteurBouton implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

			JButton btCaller = ((JButton)e.getSource());
			
			if (btCaller.equals(bResume)){
				nom = Tfnom.getText();
				prenom = Tfprenom.getText();
				tel = Tftel.getText();
				textArea.setText(nom +"\n"+ prenom+"\n"+tel);
			}
			else if (btCaller.equals(bQuit)){
				setVisible(false);
				dispose();
				System.exit(0);
			}
		}
	}
	
	public FenetreSaisie() {
		super("Saisie de données"); 
		setVisible(true);
		
		// Saisie en haut
		JPanel panelNorth = new JPanel();
		
		JLabel labelNom = new JLabel("Nom: ");
		JTextField textNom = new JTextField(10);
		this.Tfnom = textNom;
		
		JLabel labelPrenom = new JLabel("Prénom: ");
		JTextField textPrenom = new JTextField(10);
		this.Tfprenom = textPrenom;
		
		JLabel labelTel = new JLabel("Tel: ");
		JTextField textTel = new JTextField(10);
		this.Tftel = textTel;
		
		panelNorth.setLayout(new GridLayout(3,2));
		panelNorth.add(labelNom);
		panelNorth.add(textNom);
		panelNorth.add(labelPrenom);
		panelNorth.add(textPrenom);
		panelNorth.add(labelTel);
		panelNorth.add(textTel);
				
		pack();	
		
		this.add(panelNorth, BorderLayout.NORTH);
		
		// Zone centrale
		JTextArea txtA = new JTextArea(3,10);
		this.textArea = txtA;
		this.add(txtA, BorderLayout.CENTER);
		
		// Bas
		JPanel panelSouth = new JPanel();
		JButton b1 = new JButton("Resume");
		b1.addActionListener(new EcouteurBouton());
		this.bResume = b1;
		JButton b2 = new JButton("Quit");
		b2.addActionListener(new EcouteurBouton());
		this.bQuit = b2;
		
		panelSouth.add(b1);
		panelSouth.add(b2);

		this.add(panelSouth, BorderLayout.SOUTH);
		
		pack();
	}
	
	public static void main(String[] args) {
		JFrame fen = new FenetreSaisie();
	}
	
}
