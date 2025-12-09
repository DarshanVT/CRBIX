package paymentGateway;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


class helloWorldBTN{
	
	public void createBTN(){
		
		JFrame j1 = new JFrame("AWT Swing Program For Button");
		j1.setSize(300,300);
		j1.setVisible(true);
		
		
		JButton btn = new JButton("Hello World");
		Font f1 = new Font("Times New Romen",Font.BOLD , 50);
		btn.setBounds(500, 200, 100, 50);
		btn.setFont(f1);     
		btn.setForeground(Color.BLACK);   
		btn.setOpaque(true);              
		btn.setBorderPainted(false); 
		j1.add(btn);			
	}	
}
class AddShape extends JPanel{
	
	public void createShape(Graphics2D g1) {
		
		g1.setColor(Color.BLUE);
		g1.fillOval(50, 50, 100, 60);
				
	}

	
	
}

public class AWTProgram{
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		helloWorldBTN hw = new helloWorldBTN();
		hw.createBTN();
		
		AddShape as = new AddShape();
		

	}

}
