package two.classes2_3_slide_40;

import java.util.Scanner;

public class Worker {

	public void pay(String name, String gender) {
		System.out.println("Worker Pay");
	}

public static void main(String[] args) {
	
	Worker w=new DailyWorker();
	Scanner sc=new Scanner(System.in );
	w.pay("Nandhini", "female");
	w.pay("nandh", "male");
	
	
	
//	do {
//		Scanner s=new Scanner(System.in);
//		String name=s.nextLine();
//		int days=s.nextInt();
//		
//		
//		
//		
//		
//	    addWorker=s.next();}
//	while(addWorker.equals("y"));

	

}
}
