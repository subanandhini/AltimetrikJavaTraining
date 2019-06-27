package day5.Collections;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Slide_26 {
	String name;
	int age;

	public String getName() {
		return name;
	}

	public void setName(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Slide_26() {

	}

	public Slide_26(String name, int age) {
		this.name = name;
		this.age = age;

	}

	public static void main(String[] args) {
		Slide_26 s = new Slide_26();
		int counter = 0;
		Queue Person = new LinkedList();
		Queue SeniorCitizen = new LinkedList();
		s.setName("nandhini", 21);
		//s.setName("nandhini", 12);
		s.setName("nandhini", 59);

		if (s.getAge() > 50) {
			SeniorCitizen.add(new Slide_26(s.name, s.age));
		} else {
			Person.add(new Slide_26(s.name, s.age));
		}
		Iterator itr = Person.iterator();
		Iterator itr2 = SeniorCitizen.iterator();
		while (itr.hasNext()==true || itr2.hasNext()==true) {
			if (itr.hasNext()) {
				if(itr.hasNext()==false && itr2.hasNext()==false) {
					System.exit(0);
				}
				if (counter < 2) {
					System.out.println("one "+itr.next());
					counter++;
				}}
			else if(itr.hasNext()==false && itr2.hasNext()==true) {
					counter = 2;
				}

			
			else if (counter == 2) {
				if(itr.hasNext()==false && itr2.hasNext()==false) {
					System.exit(0);
				}
				if (itr2.hasNext()) {
					System.out.println("two "+ itr2.next());
					counter = 0;
				} else if(itr.hasNext()==true && itr2.hasNext()==false) {
					counter = 0;
				}
			}

		}

	}

}
