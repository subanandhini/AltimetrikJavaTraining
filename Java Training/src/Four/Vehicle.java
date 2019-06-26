package Four;

public class Vehicle implements Runnable{
	int vehicleno;
	Road r;
	Vehicle(int vehicleno, Road r){
		this.vehicleno=vehicleno;
		this.r=r;
	}
	public   void run() {
		synchronized (r) {
			
		r.Bridge(vehicleno,r);}
	}

	public static void main(String[] args) {

		Road r=new Road();
		for(int i=1;i<=5;i++) {
		Vehicle v= new Vehicle(i,r);
		Thread t= new Thread(v);
		t.start();}
	
	}
	}
