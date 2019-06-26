package day5.Collections;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Slide_26 {
	String name;
	int age;
	
	public String getName() {
		return name;
	}
	public void setName(String name , int age) {
		this.name = name;
		this.age=age;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public Slide_26() {
		
	}
    public Slide_26(String name,int age) {
    	this.name=name;
    	this.age=age;
    	
    }
	
	public static void main(String[] args) {
		Slide_26 s=new Slide_26();
		int counter=0;
		Queue Person=new LinkedList();
		Queue SeniorCitizen=new LinkedList();
		s.setName("nandhini",21);
		s.setName("nandhini",12);
		s.setName("nandhini",59);
	
		if(s.getAge()>50) {
			SeniorCitizen.add(new Slide_26(s.name,s.age));
		}
		else {
			Person.add(new Slide_26(s.name,s.age));
		}
		 Iterator iterator = Person.iterator(); 
		
		
		
		
		
		 
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		while(SeniorCitizen.size()>0||Person.size()>=0) {
			if(counter<=2) {
				if(Person.size()!=0) {
				//String a= s.Person.remove().toString();
				System.out.println(s.getName()+""+s.getAge());
				counter++;
			}else {
				counter++;
			}
				}
			else if(counter==2) {
				if(SeniorCitizen.size()!=0) {
				String a= SeniorCitizen.remove().toString();
				System.out.println("Senior "+s.getName()+""+s.getAge());
				counter=0;
				}
				else {
					counter=0;
				}
				
			
		}
	}
	
	
	
	}

}
