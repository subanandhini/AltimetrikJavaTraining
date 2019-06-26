package two.classes2_3;

public class Suv extends Car {
Suv(){
	
}
	Suv(String color) {
		super(color);
		System.out.println("Suv constructor called  "+color);
	}
@Override
public void move() {
	super.move();
	System.out.println("Suv is faster than Car");
}
}
