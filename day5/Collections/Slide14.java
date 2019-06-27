package day5.Collections;

import java.util.ArrayList;
import java.util.Scanner;

public class Slide14 {
	String name;
	char c;
	String hall;
	public Slide14(String name,String hall) {	
	this.name=name;
	this.hall=hall;
	}
	public Slide14(String name,String hall, char c) {
		ArrayList<Slide14> extempore=new ArrayList<>();
		if(c=='y') {
			extempore.add(new Slide14( name,hall));
			System.out.println("value added");
		
				System.out.println(extempore);
			
		}
		else if(c=='n'){
			extempore.remove(name);
		}
		
	}

	public Slide14() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) {
		Scanner s= new Scanner(System.in);
		Slide14 o=new Slide14();
		System.out.println("Do you want to add or remove ..if add press y else press n");
		o.c=s.next().charAt(0);
		o.name=s.next();
		o.hall=s.next();
		Slide14 obj=new Slide14(o.name,o.hall,o.c);
	}
	
	
}
