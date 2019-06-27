package two.classes2_3;

public class Car extends Vehicle{
	Car(){
		
	}
	Car(String color){
		super();
		System.out.println("Car class constructor"+"color");
	}
	@Override
	public void move() {
		super.move();
		System.out.println("car moves Faster");
	}

}
