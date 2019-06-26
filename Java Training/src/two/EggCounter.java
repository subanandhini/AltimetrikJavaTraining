package two;

import java.util.Scanner;

public class EggCounter {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Total No of Eggs");
		int egg = sc.nextInt();
		int gross = egg / 144;
		int grem = egg % 144;
		int dozen;
		int drem;
		if (grem != 0) {
			dozen = grem / 12;
			drem = grem % 12;
			if (drem != 0) {
				System.out.println("Your number of eggs is equal to  " + gross + " gross, " + dozen + " dozen, and " + drem+" egg");
			}
			if (drem == 0) {
				System.out.println("Your number of eggs is equal to " + gross + " gross, and  " + dozen + " dozen");
			}
		} else {
			System.out.println("Your number of eggs is equal to " + gross + " gross ");
		}

	}
}
