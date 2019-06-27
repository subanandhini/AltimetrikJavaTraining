package two.classes2_3;

public class Bus extends Vehicle {
	@Override
	public void move() {
		super.move();
		System.out.println("bus moves slower");
	}
}
