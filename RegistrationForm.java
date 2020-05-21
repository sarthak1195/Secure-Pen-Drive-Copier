package usbdrivedectector;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import usbdrivedectector.detectors.WindowsStorageDeviceDetector;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegistrationForm extends JFrame implements ActionListener {

	private static Process process;
	private final static String Hide_Contents = "attrib +s +h ";
	private final static String Path = WindowsStorageDeviceDetector.Path;
	
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) screenSize.getWidth();
	int height = (int) screenSize.getHeight();
	
	JPanel panelreg;
	// JLabel lblnamereg;
	JLabel lblpasswordreg;
	JLabel lblmessreg;
	// JTextField txtnamereg;
	JPasswordField txtpasswordreg;
	JButton btsubmit;
	String serialnumber;	

	RegistrationForm(String uname) {
		serialnumber = uname;
		setTitle("Member login and Registration");
		setSize(330, 250);
		setLocation((width/4)+ 120, (height/4)+50);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container content = getContentPane();
		JDesktopPane des = new JDesktopPane();

		// Registration form
		JInternalFrame freg = new JInternalFrame();
		freg.setSize(300, 200);
		freg.setLocation(12, 10);
		freg.setTitle("Member Registration");
		// lblnamereg = new JLabel("User Name:");
		lblpasswordreg = new JLabel("Password:");
		lblmessreg = new JLabel("");
		btsubmit = new JButton("Submit");
		btsubmit.addActionListener(this);
		// txtnamereg = new JTextField(20);
		txtpasswordreg = new JPasswordField(20);
		txtpasswordreg.addKeyListener(new KeyList());
		panelreg = new JPanel();
		// panelreg.add(lblnamereg);
		// panelreg.add(txtnamereg);
		panelreg.add(lblpasswordreg);
		panelreg.add(txtpasswordreg);
		panelreg.add(btsubmit);
		panelreg.add(lblmessreg);
		freg.add(panelreg);
		freg.setVisible(true);
		des.add(freg);
		content.add(des, BorderLayout.CENTER);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btsubmit) {

			String passw = new String(txtpasswordreg.getPassword());
			if (!checkBlank(passw, lblpasswordreg)) {

				passw = new String(encrypt(passw));
				String accinfo = serialnumber + "-" + passw;
				saveToFile("accounts.txt", accinfo);
				try {
					process = Runtime.getRuntime().exec(Hide_Contents + Path + "accounts.txt");
				} catch (IOException e1) {					
					e1.printStackTrace();
				}
				try {
					DriveProperties.HideContents();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
				LoginForm flog = new LoginForm(serialnumber);
				//setVisible(true);								

			} // checkBlank if

		} //// getsource if
	}// actionPerformed Function

	public class KeyList extends KeyAdapter {
		public void keyPressed(KeyEvent ke) {
			String passw = new String(txtpasswordreg.getPassword());
			String mess = checkStrength(passw);
			showMess(mess + " password", lblpasswordreg);
		}

	}

	public boolean checkBlank(String passw, JLabel passwmess) {
		boolean hasBlank = false;

		if (passw.length() < 1) {
			showMess("Password is required.", passwmess);
			hasBlank = true;
		}
		return hasBlank;

	}

	public static void showMess(String mess, JLabel lbl) {
		lbl.setText(mess);
		lbl.setForeground(Color.RED);
	}

	public String checkStrength(String passw) {
		Pattern pat = Pattern.compile("([0-9][aA-zZ]|[aA-zZ][0-9])");
		Matcher mat = pat.matcher(passw);
		if (mat.find()) {
			if (passw.length() >= 8)
				return "Strong";
			else
				return "Medium";
		} else
			return "Weak";

	}

	public void reset(JLabel lblpassw) {
		// lblname.setText("User Name:");
		// lblname.setForeground(Color.BLACK);
		lblpassw.setText("Password:");
		lblpassw.setForeground(Color.BLACK);
	}

	/*
	 * Function of Login Form class
	 * 
	 * 
	 * 
	 * public boolean check(String accinfo, String name, String passw) {
	 * String[] info = accinfo.split("-"); String uname = info[0]; String pass =
	 * new String(decrypt(info[1])); if (uname.equals(name) &&
	 * pass.equals(passw)) return true; else return false;
	 * 
	 * }
	 *
	 * 
	 */

	public static boolean checkExist(String filename, String name) {
		FileReader fr;
		BufferedReader br;
		String accinfo;
		boolean exist = false;
		try {

			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			while ((accinfo = br.readLine()) != null) {

				if (check(accinfo, name)) {
					//showMess("The account already exists.", lblmessreg);
					exist = true;
					break;
				}

			}

			br.close();
			fr.close();
		} catch (Exception ie) {
			System.out.println("Error!");
		}
		return exist;
	}

	public static boolean check(String accinfo, String name) {
		String[] info = accinfo.split("-");
		String uname = info[0];
		if (uname.equals(name))
			return true;
		else
			return false;

	}

	public void saveToFile(String filename, String text) {
		try {
			FileWriter fw = new FileWriter(filename, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.newLine();
			bw.flush();
			bw.close();
			showMess("The account is created.", lblmessreg);
			reset(lblpasswordreg);
			
		} catch (IOException ie) {
			System.out.println("Error in writing to file...");
		}
	}

	public byte[] encrypt(String passw) {
		byte[] sb = passw.getBytes();
		int i;
		for (i = 0; i < sb.length; i++)
			sb[i] = (byte) (sb[i] + 1);

		return (sb);
	}

	public byte[] decrypt(String passw) {

		byte[] sb = passw.getBytes();
		int i;
		for (i = 0; i < sb.length; i++)
			sb[i] = (byte) (sb[i] - 1);

		return (sb);
	}

}