package usbdrivedectector;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginForm extends JFrame implements ActionListener{

	JPanel panel;
	JLabel lblname;
	JLabel lblpassword;
	JLabel lblmess;
	JTextField txtname;
	JPasswordField txtpassword;
	JButton btlogin;
	String serialnumber;	
	
	LoginForm(String uname){
		serialnumber=uname;
		setSize(300, 200);
		setLocation(500, 250);
		setTitle("Member Login");		
		lblpassword = new JLabel("Password:");
		lblmess = new JLabel("");
		btlogin = new JButton("Login");
		btlogin.addActionListener(this);
		txtpassword = new JPasswordField(20);
		panel = new JPanel();
		panel.add(lblpassword);
		panel.add(txtpassword);
		panel.add(btlogin);
		panel.add(lblmess);
		add(panel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e){
		
		if (e.getSource() == btlogin) {			
			String passw = new String(txtpassword.getPassword());
			if (!checkBlank(passw, lblname, lblpassword))
				validateUser("accounts.txt", passw);
		}
		
	}
	
	public boolean checkBlank(String passw, JLabel namemess, JLabel passwmess) {
		boolean hasBlank = false;
		if (passw.length() < 1) {
			showMess("Password is required.", passwmess);
			hasBlank = true;
		}
		return hasBlank;

	}
	
	public void showMess(String mess, JLabel lbl) {
		lbl.setText(mess);
		lbl.setForeground(Color.RED);
	}
	
	public void validateUser(String filename, String password) {
		FileReader fr;
		BufferedReader br;
		boolean valid = false;
		String accinfo;
		try {

			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			while ((accinfo = br.readLine()) != null) {

				if (check(accinfo, serialnumber, password)) {
					showMess("Valid login", lblmess);
					valid = true;
					//DataLogging dlog=new DataLogging();
					DriveProperties.UnhideContents();
					EndOperation eo = new EndOperation();
					dispose();
					break;
				}

			}

			if (!valid)
				showMess("invalid login", lblmess);
			reset(lblpassword);

			br.close();
			fr.close();
		} catch (Exception ie) {
			System.out.println("Error!");
		}

	}
	
	public boolean check(String accinfo, String name, String passw) {
		String[] info = accinfo.split("-");
		String uname = info[0];
		String pass = new String(decrypt(info[1]));
		if (uname.equals(name) && pass.equals(passw))
			return true;
		else
			return false;

	}
	
	public void reset(JLabel lblpassw) {
		lblpassw.setText("Password:");
		lblpassw.setForeground(Color.BLACK);
	}
	
	public byte[] decrypt(String passw) {

		byte[] sb = passw.getBytes();
		int i;
		for (i = 0; i < sb.length; i++)
			sb[i] = (byte) (sb[i] - 1);

		return (sb);
	}
}
