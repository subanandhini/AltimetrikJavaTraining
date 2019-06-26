package Four;

public class Appear implements Runnable{
	static  Thread t;
	static char c[]={'H','E','L','L','O'};
	 public void run() {
		 int i=0;
		while (i<5){
		System.out.print(c[i++]);
		try {
			t.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	 }
	 public static void main(String str[]){
		t =new Thread(new Appear());
		t.start();
		
	 }}
