package two.classes2_2;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Calculator  {
	public void add(int a, int b)throws Exception {
		int sum=a/b;
		System.out.println(sum);
	}
	public void add(double a, double b)throws Exception {
		double sum=a+b;
		System.out.println(sum);
	}
	public void add(int a, double b) {
		double sum=a+b;
		System.out.println(sum);
	}
	public void add(double a, int b) {
		double sum=a+b;
		System.out.println(sum);
	}
	
	
public static void main(String[] args) throws Exception {
	System.out.println("Slide No 30....");
	
	try {
	Calculator c=new Calculator();
	Scanner s= new Scanner(System.in);
	c.add(10.12, 12.121);
	System.out.println("Enter a value");
	
	c.add(11, s.nextInt());
	c.add(1, Integer.parseInt("fshik"));
	c.add(12.23, 31);
}catch(InputMismatchException e) {
	
	System.out.println("InputMismatch check the input you have given "+e);
	throw new Exception();
}
catch (ArithmeticException e) {
		System.out.println("check the Arithmetic operations you have done you are getting "+e+"error");
		throw new Exception();
	}
	catch( java.lang.NumberFormatException e) {
		System.out.println(e+ " is not a valid ");
		throw new Exception();
	}
	
finally {
	System.out.println("Program has completed successfuly");
	
}

}
}
