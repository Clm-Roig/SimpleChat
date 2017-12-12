import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.util.Scanner;

public class MaFenetre extends JFrame{

	public MaFenetre() {
		super("Une fenêtre"); 
		setVisible(true);
		JLabel label = new JLabel("Bonjour");
		JButton b1 = new JButton("Cliquez moi !");
		this.add(label, BorderLayout.NORTH);
		this.add(b1, BorderLayout.SOUTH);
		pack();
	}
	
	private class EcouteurBouton implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String text = ((JButton)e.getSource()).getText();
			System.out.println(text);
			
		}
		
	}
		
	public MaFenetre(int n) {
		super("Une fenêtre"); 
		setVisible(true);
		JLabel label = new JLabel("Bonjour");
		JPanel panel = new JPanel();
		for(int i=1;i<=n;i++) {
			JButton butt = new JButton("BOUTON" + i);
			ActionListener eb = new EcouteurBouton();
			butt.addActionListener(eb);
			panel.add(butt);
		}
		this.add(label, BorderLayout.NORTH);
		this.add(panel, BorderLayout.SOUTH);
		pack();
		
	}
	
	public static void main(String[] args) {
		Scanner kb = new Scanner(System.in);
		System.out.println("Combien de boutons dans ta fenêtre ?");
		int n = kb.nextInt();
		JFrame fen = new MaFenetre(n);
	}


}
