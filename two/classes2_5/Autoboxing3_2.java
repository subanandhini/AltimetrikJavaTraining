package two.classes2_5;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Autoboxing3_2 {

	public Autoboxing3_2() {
		// TODO Auto-generated constructor stub
	}
String name;
int age;
String gender;
String weight;
String height;
public String getHeight() {
	return height;
}
public void setHeight(String height) {
	if(height.endsWith("inches")) {
		int i= height.indexOf('i');
		String ss=height.substring(0,i);
		int  h= Integer.valueOf(ss);
		this.height = Double.toString(h);
	}
	else if(height.endsWith("cm")) {
		int i= height.indexOf('c');
		String ss=height.substring(0,i);
		int  h= Integer.valueOf(ss);
		this.height = Double.toString(h*2.54);
	}
	else {
		this.height=height;
	}
	

}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	try {
	this.age = age;}
	catch(InputMismatchException e ) {
		System.out.println("Your age should only be a number");
	}
}
public String getGender() {
	return gender;
}
public void setGender(String gender) {
	if(gender.equals("male"))
	this.gender = gender;
	else {
		System.out.println("You are not eligible ... Only male candidates can apply");
		System.exit(0);
		}
	
}
public String getWeight() {
	return weight;
}
public void setWeight(String weight) {
	if(weight.endsWith("kgs")) {
		int i= weight.indexOf('k');
		String ss=weight.substring(0,i);
		int  w= Integer.valueOf(ss);
		this.weight = Double.toString(w*2.2);
	}
	else if(weight.endsWith("lbs")) {
		int i= weight.indexOf('l');
		String ss=weight.substring(0,i);
		int  w= Integer.valueOf(ss);
		this.weight = Double.toString(w);
	}
	else if(weight.chars().allMatch( Character::isDigit )) {
		this.weight=weight;
	}
	else {
		System.out.println("enter a valid weight");
		System.exit(0);
	}
}
	
	
	public static void main(String[] args) {
		Scanner s=new Scanner(System.in);
		Autoboxing3_2 ab=new Autoboxing3_2();
		try {
		System.out.println("What is your name");
		ab.setName(s.next());
		System.out.println("What is your gender");
		ab.setGender(s.next());
		System.out.println("What is your age");
		ab.setAge(s.nextInt());
		System.out.println("What is your height in inch or cm");
		ab.setHeight(s.next());
		System.out.println("What is your weight in kgs or pound");
		ab.setWeight(s.next());
		
		}
		catch(Exception e){
			System.out.println("Give a valid information....."+e);
		}
		if(ab.getAge()<20||ab.getAge()>50) {
			System.out.println("Your Age is not Eligible for this Position");
		}
		else if(ab.getAge()>=20&&ab.getAge()<=30) {
			if(Integer.valueOf(ab.getWeight())>=155 && Integer.valueOf(ab.getWeight())<=175) {
			     if(Integer.valueOf(ab.getHeight())>=5.2 &&Integer.valueOf(ab.getHeight())>=5.5) {
			    	 System.out.println("Yeah man welcome You are eligible .....  :)");
			     }
			}
		}
		else if(ab.getAge()>=30&&ab.getAge()<=40) {
			if(Integer.valueOf(ab.getWeight())>=170 && Integer.valueOf(ab.getWeight())<=180) {
			     if(Integer.valueOf(ab.getHeight())>=5.4 &&Integer.valueOf(ab.getHeight())>=5.6) {
			    	 System.out.println("Yeah man welcome You are eligible .....  :)");
			     }
			}
		}
		else if(ab.getAge()>=40&&ab.getAge()<=50) {
			if(Integer.valueOf(ab.getWeight())>=175 && Integer.valueOf(ab.getWeight())<=185) {
			     if(Integer.valueOf(ab.getHeight())>=5.6 &&Integer.valueOf(ab.getHeight())>=6.0) {
			    	 System.out.println("Yeah man welcome You are eligible .....  :)");
			     }
			}
		}
		else {
			System.out.println("We are really sorry you are not eligible....");
		}
		
		
		
	}
	
	
}
