package two.classes2_2;

import java.math.BigInteger;
import java.util.Scanner;

public class PasswordGenerator {
	public static void main(String[] args) {
		System.out.println("Slide No 17..........");
		Scanner sc = new Scanner(System.in);
		PasswordGenerator pg=new PasswordGenerator();
		String name = sc.next();
		int[] arr = new int[name.length()];
		for (int i = 0; i < name.length(); i++) {
			arr[i] = Integer.valueOf(name.codePointAt(i));
		}
	   pg.generatePassword(arr);
		
	}

	public void generatePassword(int[] arr) {
		String reversed = "";
		for (int i = 0; i < arr.length; i++) {
			
			String a =Integer.toString(arr[i]);
			StringBuilder sa = new StringBuilder(a);
			sa.reverse();

			reversed += sa.toString();
		}
		System.out.println(reversed);
		while(reversed.length()>5) {
			BigInteger bigIntegerStr=new BigInteger(reversed);
			BigInteger w=bigIntegerStr;
			BigInteger bigInt = BigInteger.valueOf(5);
		        w=w.divide(bigInt);
		    reversed=String.valueOf(w);
		   // System.out.println( "reversed"+reversed);
		
		}
		System.out.println(reversed);
		System.out.println(Integer.toOctalString(Integer.valueOf(reversed)));
		System.out.println(Integer.toHexString(Integer.valueOf(reversed)));
		
		
	}
}
