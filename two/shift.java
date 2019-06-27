package two;

import java.util.Scanner;

public class shift {
public static void main(String[] args) {
	System.out.println(Integer.toBinaryString(12));
	System.out.println(-3>>2);
	System.out.println(3<<2);
	
	System.out.println(-3>>>2);
	System.out.println("-------------------");
	System.out.println("palindrome program");
	System.out.println("-------------------");
	Scanner sc=new Scanner(System.in);
	System.out.println("enter a string to check palindrome");
	String s=sc.next();
	StringBuilder sb = new StringBuilder(s);
	String r=sb.reverse().toString();
	
	if(s.equals(r)) 
		System.out.println("The reverse is  "+r+". This is  a Palindrome.....");
	else
		System.out.println("The reverse is  "+r+".  This is Not a palindrome");
}
}
