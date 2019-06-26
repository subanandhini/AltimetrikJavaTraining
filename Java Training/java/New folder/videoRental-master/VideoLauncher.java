import java.util.*;

class Video {
	String name;
	boolean checkout ;
	//VideoStore m=new VideoStore();
	int rating;
	int p=0;
	String[] video=new String[20];
	int [] rat=new int[20];
	String videoche[]= {"true","true","true","true","true","true","true","true","true","true","true","true","true","true","true","true","true","true","true","true"};
	public void getName(String name) {
		int count=0;
		for(int i=0;i<p;i++) {
			if(video[i].equals(name)) {
				count++;
				System.out.println("This vidoe is already in the list try to add new videos  :) \n\n");break;}
		}if(count==0) {
			System.out.println("Video "+name+" added Successfully");
		}
		video[p++]= name;
	}

	public void doCheckout(String name) {
		for(int i=0;i<p;i++) {
			if(p!=0) {
				
			if(video[i].equals(name)) {			
				System.out.println("Video "+name+" CheckedOut Successfully");
				videoche[i]=(videoche[i]=="true")?"false":"true";
				break;}}
			else {

				System.out.println("Video cannot be checked out");}}
						
			}
		
		
	public int getRating(int rating,String name) {
		int count=0;
		for(int i=0;i<p;i++) {
			if(	p!=0) {
				count++;
			if(video[i].equals(name)) {
				rat[i]=rating;
				System.out.println("Rating "+rating+" has been mapped to Video"+name);
			}
			}}
		if(count==0) {System.out.println("This video is not in the list . Add the video to the list first");count=0;}
			return rating;}
	public void doReturn(String vname) {
			
		for(int i=0;i<p;i++) {
			if(p!=0)
			if(video[i].equals(vname)) 
				videoche[i]="false";
				}
			}
	public void receiveRating(float rating) {
		float arating=(float)this.rating/rating;
	System.out.println( arating);
	}
	public boolean getcheckout() {
		return checkout;
		
	}
	public void Video() {
		if(p==0) {
			System.out.println("There is no video in the list try to add them  ;)\n\n");
		}else {
		
		System.out.println("--------------------------------------------------------");
		System.out.println("Videoname\t|\t CheckOutStatus\t|\t Rating");
		System.out.println("--------------------------------------------------------");
		for(int i=0;i<p;i++)System.out.println(video[i]+"\t"+videoche[i]+"\t"+rat[i]+"\t");
		
	}
	}

}
public class VideoLauncher {
public static void main(String[] args) { 
	VideoStore n=new VideoStore();
	int choice=0;
	do {
System.out.println("MAIN MENU \n============\n1.Add Video: \n2.Check Out video :\n3.Return Video: \n4.Receive Rating :\n5.List Inventory :\n6.Exit :  Enter your choice(1..6):");
Scanner s=new Scanner(System.in);
choice=s.nextInt();
switch(choice) {
case 1:
	n.addVideo();
	break;
case 2:
	n.doCheckout();break;
case 3:
	n.doReturn();
	break;	
case 4:
	n.receiveRating();break;
case 5:
	n.listInventory();break;
case 6:
	n.exit();break;
default:
	System.out.println("Give a valid value");break;}}
	while(choice!=6);


}
}
class VideoStore {
String []videostore;
Video v=new Video();

Scanner s=new Scanner(System.in);
public void addVideo() {
	System.out.println("Enter name of the video you want to add:");
	String vname=s.next();
	v.getName(vname);

}
public void doCheckout() {
	System.out.println("Enter name of the video you want to CheckOut:");
	String vname=s.next();
	v.doCheckout(vname);
	

}
public void doReturn() {
	System.out.println("Enter name of the video you want to Return:");
	String vname=s.next();
	v.doReturn(vname);
	System.out.println("Video "+vname+" Returned Successfully");
}
public void receiveRating() {
	System.out.println("Enter the video name to be rated");
	String vname=s.next();
	System.out.println("Enter the Rating for video:");
	int rate=s.nextInt();
	v.getRating(rate,vname);
	
	
}
public void listInventory(){
	
	v.Video();
}
public void exit() {
	System.out.println("Exiting Thanks for Using out Application !!!");
}
}
