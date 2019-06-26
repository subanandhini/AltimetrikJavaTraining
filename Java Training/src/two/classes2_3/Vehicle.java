package two.classes2_3;

public class Vehicle {
	Vehicle(){
		System.out.println("The Super class constructor ");
	}
public static void main(String[] args) {
	Vehicle v= new Vehicle();
	Vehicle vc=new RacingCar("jfis");
	//RacingCar rc=(RacingCar) new Vehicle();// does not work java.lang.ClassCastException: occurs
	Car c= new Car("Black Car");
	Suv s=new Suv("white Suv");
	RacingCar rc= new RacingCar("Red Racing Car");
	Vehicle vehicle[]= {c,s,rc,vc,v,new Car(),new Bus(),new Truck()};
	
	for(int i=0;i<vehicle.length;i++) {
		vehicle[i].move();
	}
}

public void move() {
	System.out.println("vehicle moves");
}
}
