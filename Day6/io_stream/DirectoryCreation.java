package Day6.io_stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class DirectoryCreation {

	public DirectoryCreation() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter The path where you want to create a file");
		String filepath = s.next();
		File b = new File(filepath);
		try {
			if (b.exists()) {
				b.delete();
				System.out.println("Deleting existing file .....");
				boolean tru = b.createNewFile();
			} else {
				boolean tru = b.createNewFile();

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		} finally {

			System.out.println("created file " + b);
		}
		
		File path = new File(filepath);
		try {
		File filelist[] = path.listFiles();
		
		if(filelist.length<0){
			System.out.println("No files persent in this path "+path.separator+path);
		}else {
		System.out.println("--------------------------------------------");
		System.out.println();
		System.out.println("Listing Directories in Batch folder");
		System.out.println();
		System.out.println("--------------------------------------------");
		
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				System.out.println("Directory : " + filelist[i].getName());
			} else if (filelist[i].isFile()) {
				System.out.println("file : " + filelist[i].getName());
			}
		}
		System.out.println("--------------------------------------------");

		}
		}catch(	NullPointerException e)
	{
		System.out.println(filepath+"  is a file there are no files inside this file");
	}
}

}
