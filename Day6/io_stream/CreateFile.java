package Day6.io_stream;

import java.io.File;
import java.io.IOException;

public class CreateFile {

	public CreateFile() {
		// TODO Auto-generated constructor stub
	}
public static void main(String[] args) {
	File f= new File("Created.txt");
	try {
	if(f.exists())f.delete();
		
	boolean b=f.createNewFile();
	System.out.println(b);}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
