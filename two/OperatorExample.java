package two;

import java.util.Scanner;

public class OperatorExample {
public static void main(String[] args) {
	int i=0;
	i++;
	System.out.println(i);
	char c='w';

	System.out.println("c++ is "+ ++c);
	char s='c';
	System.out.println("s-- is "+ --s);
	double d=0.121;
	System.out.println(++d);
	
//	System.out.println("Swapping using bitwise Operator");
	Scanner scan= new Scanner(System.in);
//	System.out.println("Enter the first number : ");
//	int first=scan.nextInt();
//	System.out.println("Enter the second number : ");
//	int second=scan.nextInt();
//	first=  first^second;
//	second= first^second;
//	first=  first^second;
//	
//	System.out.println("first number"+first);
//	System.out.println("second number"+ second	);
	System.out.println(c);
	System.out.println(c|s);
	System.out.println(c^s);
	System.out.println(~c);
	
	System.out.println("leap year concept");
	System.out.println("Enter the year you want to check");
	int year=scan.nextInt();
	if(year%4==0) 
		System.out.println(year+" is a leap year  ");
	else
		System.out.println(year+" is not a leap year");
	
	
	
}
}
