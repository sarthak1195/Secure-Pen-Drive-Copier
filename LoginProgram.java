package usbdrivedectector;

public class LoginProgram {
    static String serial=serialNumber.HDD_SerialNo();
    
    LoginProgram()
    {
  	  if(RegistrationForm.checkExist("accounts.txt", serial))
	   {
		  new LoginForm(serial);
	   }
	  else
	  {
		  new RegistrationForm(serial);
	  }
    }
    
	/*public static void main(String args[]) {
	//RegistrationForm rf=new RegistrationForm();
	//LoginForm lf=new LoginForm();
	

	}*/

}