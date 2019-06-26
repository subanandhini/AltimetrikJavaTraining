package two.classes2_3;

public class Truck extends Vehicle{
	Truck(){
		
	}
@Override
public void move() {
	super.move();
	System.out.println("Truck moves slowly");
}
}
