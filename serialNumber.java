package usbdrivedectector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
public class serialNumber {
    //static String SERIAL[]=new String[200];
    //static int i=0;
	public static String HDD_SerialNo(){

	    Runtime rt = Runtime.getRuntime();
	    String s = null;
	    String str[]=new String[5];
	    int i=0;
	    try {	    	
	        Process process=rt.exec(new String[]{"CMD", "/C", "WMIC diskdrive get serialnumber"});
	        
	        //Reading successful output of the command
	        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        while ((s = reader.readLine()) != null) {

	        	StringTokenizer st = new StringTokenizer(s," ");  
	            while (st.hasMoreTokens()) {  
                     
	            	  str[i]=st.nextToken(); 
                      i++;
	            }
	            
	            
	            
	        }
	         return str[1];
	        /*
	         for(int j=0;j<str.length;j++)
	        {
	        	System.out.println(str[j]);
	        }*/
	        // Reading error if any
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        
	    }
	   return str[1];
	}
	
}
