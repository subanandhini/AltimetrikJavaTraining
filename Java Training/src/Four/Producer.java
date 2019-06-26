package Four;
	public class Producer implements Runnable{
		int  apples; // mainatins the count of the apples
		public static void main(String s[]){
		// Creating 3 threads 
		Producer pc = new Producer();
		Thread producer= new Thread(pc, "Tree");
		Thread consumer1= new Thread(pc, "Worm");
		Thread consumer2= new Thread(pc, "Man");
		// Starting 3 threads 
		producer.start();
		consumer1.start();
		consumer2.start();
		}
		synchronized void produce(){
			while(true){
			//produce apples only if there are <=100 apples
			if(apples>100)
			try{ 
			System.out.println("Waiting for apples to be eaten");
			wait();
			}
			catch(InterruptedException e){}
			try{
			int i=(int)(Math.random()*100)+1;  
			Thread.sleep(i*10); // time taken to produce apples
			apples=apples+i;
			System.out.println("Produced apples ="+ apples);

			}catch(InterruptedException e){}
			notifyAll();
			}}
			//Consumers calls this method
			synchronized void consume(){
			while(apples>0||apples<(int)(Math.random()*100)+1){
			int i=(int)(Math.random()*100)+1;
			// consume only if there are enough apples 
			if(apples>0 && apples<i)
			try{ System.out.println(Thread.currentThread().getName() +" waiting for "+ (i-apples) +" more apples");
			wait();}catch(InterruptedException e){}
			try{
				System.out.println(Thread.currentThread().getName()+ " busy eating  "+ i +" apples");
				Thread.sleep(i); // time taken to consume apples
				apples=apples-i;}
				catch(InterruptedException e){}
				notify();}
				}
				public void run(){
				if(Thread.currentThread().getName().equals("Tree"))
				produce();
				else consume();

				}}

