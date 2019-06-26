package two;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

public class StackDemo {
	static int max=2;
	int top;
	long stackArray[];

	public StackDemo(int max) {
		StackDemo.max = max;
		this.top = -1;
		this.stackArray = new long[max];
	}

	public void push(int push) {
		if (isFull()) {
			max++;
	    	long[] stacksArray=Arrays.copyOf(stackArray, max);
			//System.out.println(Arrays.copyOf(stackArray, max));
			//System.out.println(" stackarray-len, max " + stackArray.length +  " " + max );
			//
		
			//System.out.println("increased" + stackArray.length);
//			Arrays.copy
			top++;
			stacksArray[top] = push;
		} else {
			top++;
			stackArray[top] = push;
		}
	}

	public long pop() {
		int old_top = top;
		top--;
		return stackArray[old_top];
	}

	public long peak() {
		return stackArray[top];
	}

	boolean isEmpty() {
		return (top == -1);

	}

	boolean isFull() {
		return (max - 1 == top)?true:false;
		
	}

	public static void main(String[] args) {
		StackDemo s = new StackDemo(max);
		s.push(10);
		s.push(20);
//		s.push(30);
//		s.push(40);
		while (!s.isEmpty()) {
			long value = s.pop();
			System.out.println(value);

		}
		System.out.println(s.isEmpty());
	}
}
