package Four;

public class Road extends Thread{

public void  Toll(int vehicleno) {
	System.out.println("The vehicle no "+vehicleno+" has crossed the Toll");
}
public static void Bridge(int vehicleno, Road r) {
	System.out.println("The vehicle no "+vehicleno+" has crossed the bridge");
	r.Toll(vehicleno);
}
}
