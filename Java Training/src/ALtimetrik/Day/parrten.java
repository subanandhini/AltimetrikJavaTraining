package ALtimetrik.Day;

public class parrten {
	public static void main(String[] args) {
		for(int i=0;i<3;i++) {
			for(int j=0;j<=6;j++) {
				if((i%2==0 &&j%2==0)||(i%2==1&&j%2==1)){
					System.out.print("*");
					
				}else
					System.out.print(" ");
			}
			System.out.println();
			
		}
	}

}
