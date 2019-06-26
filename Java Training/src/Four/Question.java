package Four;

import java.util.Scanner;

public class Question extends Thread {
   static Thread t;
   public void run() {
	   System.out.println("Wanna a join....?");
	   try {
		   Scanner s=new Scanner(System.in);
		   String ss= s.next();
		
		System.out.println("Congrats");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("Better Luck Next Time");
	}
   }
   
	public Question() {
		}
public static void main(String[] args) {
	t=new Thread();
	t.start();
	try {
		t.sleep(1000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	if(t.isAlive()) {
		System.out.println("Better Luck Next Time");
	}
}
}
