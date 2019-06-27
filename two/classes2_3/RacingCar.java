package two.classes2_3;

public class RacingCar extends Car{
RacingCar(){
	
}
	RacingCar(String color) {
		super(color);
		System.out.println("Racing car constructor called  "+color);
	}
	@Override
	public void move() {
		System.out.println("This is the Fastest car ");
	}

}
