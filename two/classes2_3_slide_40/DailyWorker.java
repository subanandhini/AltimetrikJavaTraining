package two.classes2_3_slide_40;

import java.util.Scanner;

public class DailyWorker extends Worker {
public DailyWorker() {
	}
@Override
public void pay(String name, String gender) {
	Scanner sc=new Scanner(System.in );
	System.out.println("How Many days did You work ?"+name);
	int noOfDays=sc.nextInt();	
	//String mrmrs=gender.equalsIgnoreCase("female")?"she":"he";
	float salary=1000*noOfDays;
	System.out.println(name +" you are  a DailyWorker you  has worked "+noOfDays +" Days so you will be paid Rs."+salary+"  as salary");
	
	}
}
