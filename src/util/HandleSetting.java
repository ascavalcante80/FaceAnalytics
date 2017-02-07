package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class HandleSetting {


	public static Boolean writeSettingsFile(String path, String url, String user, String port, String password){

		FaceSettings fs = new FaceSettings(url, user, port, password);

		try {
			FileOutputStream fileOut =
					new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(fs);
			out.close();
			fileOut.close();
		}catch(IOException i) {

			return false;
		}
		return true;
	}
	
	public static FaceSettings readSettingsFile(String path){

	    FaceSettings fs = null;
	      try {
	         FileInputStream fileIn = new FileInputStream(path);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         fs = (FaceSettings) in.readObject();
	         in.close();
	         fileIn.close();
	         return fs;
	         
	      }catch(IOException i) {
	         i.printStackTrace();
	         return null;
	      }catch(ClassNotFoundException c) {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	         return null;
	      }
	}
	
	
	
	
}
