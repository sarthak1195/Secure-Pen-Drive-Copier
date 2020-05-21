package usbdrivedectector;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EndOperation extends JFrame implements ActionListener{
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	
	JPanel panel;
	JButton stopButton;
		
	
	EndOperation(){		
		setSize(200, 75);
		setLocation(width/2, height/2);
		setTitle("End File Transfer");		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(this);	
		panel = new JPanel();
		panel.add(stopButton);
		add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e){
		
		if (e.getSource() == stopButton) {
			try {
				DriveProperties.HideContents();
			} catch (IOException e1) {				
				e1.printStackTrace();
			}
			dispose();
			
		}
		
	}

}
