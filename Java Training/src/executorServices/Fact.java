package executorServices;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class Factorial implements Callable<Long> {
long n;
public Factorial (long n) {
	this.n=n;
}
	@Override
	public Long call() throws Exception {
		if(n<=0) {
			throw new Exception("N should be >0");
		}
		long fact=2;
		for (int longVal = 1; n>= longVal; longVal++) {
			fact=longVal*fact;
			
		}
		return fact;
	}}
public class Fact{
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
long N=3;
Callable<Long> task=new Factorial(N);
ExecutorService es=Executors.newSingleThreadExecutor();
Future<Long> future=es.submit(task);
System.out.println("Factorial of "+N+ " is " +future.get());
es.shutdown();
	}

}
