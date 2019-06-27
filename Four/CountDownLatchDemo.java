package Four;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CountDownLatchDemo {

    public static void main(String args[]) {
       final CountDownLatch latch = new CountDownLatch(3);
       Thread one = new Thread(new Service("Nandhini", 10, latch));
       Thread two = new Thread(new Service("Anu", 10, latch));
      // Thread three = new Thread(new Service("Aishu", 1000, latch));
      
       one.start(); 
       two.start();
      // three.start();
         try{
            latch.await(); 
            System.out.println("Hurray Let's get this party Started Everyone came, Application is starting now");
       }catch(InterruptedException ie){ 
           ie.printStackTrace();
       }
      
    }
  
}

/**
 * Service class which will be executed by Thread using CountDownLatch synchronizer.
 */
class Service implements Runnable{
    private final String name;
    private final int timeToStart;
    private final CountDownLatch latch;
  
    public Service(String name, int timeToStart, CountDownLatch latch){
        this.name = name;
        this.timeToStart = timeToStart;
        this.latch = latch;
    }
  
    @Override
    public void run() {
        try {
            Thread.sleep(timeToStart);
        } catch (InterruptedException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println( name + " has came");
        latch.countDown(); //reduce count of CountDownLatch by 1
    }
  
}