package day5.Collections;

import java.util.Scanner;
import java.util.Stack;

public class Slide_24 {
	int top;
	long stackArray[];

	public Slide_24() {
	
	}
	public void push(int push) {
		top++;
		stackArray[top]=push;
	}

	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		String operator=s.next();
		Stack<Integer> stackOpr=new Stack();
		for(int i=0;i<operator.toCharArray().length;i++) {
			if(Character.isDigit(operator.charAt(i))) {
				stackOpr.push( operator.charAt(i)-'0');		
			}
			else {
				int one= stackOpr.pop();
				int two=  stackOpr.pop();
				
				switch (operator.charAt(i)) {
				case '+':
					stackOpr.push(one+two);
					break;
				case '-':
					stackOpr.push(one-two);
					break;
				case '*':
					stackOpr.push(one*two);
					break;
				case '/':
					stackOpr.push(one/two);
					break;
					

				default:
					break;
				}
			}
			
		}
		
		
		
		
		
	}
	
	
	
	
	
	

}
