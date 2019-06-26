package two;

import java.util.Scanner;

public class slide2_1 {
	public static void main(String[] args) {
	
//Scanner s= new Scanner(System.in);

	System.out.println("slide 2.1 56,57");
	String str="The quick brown Fox jumps over the lazy Dog";

System.out.println(str.charAt(11));
System.out.println(str.contains("is"));
System.out.println(str.concat(" and killed it"));
System.out.println(str.endsWith("dogs"));
System.out.println(str.equals("The quick brown Fox jumps over the lazy Dog"));
System.out.println(str.equalsIgnoreCase("The quick brown Fox jumps over the lazy Dog"));
System.out.println(str.length());
System.out.println(str.equals("The quick brown Fox jumps over the lazy Dog"));
System.out.println(str.replace("The", "A"));
String a[]=str.split("Fox",2);
for(String arr:a)
System.out.println(arr);
System.out.println(str.contains("Fox")+" "+str.contains("Dog"));

System.out.println(str.toLowerCase());
System.out.println(str.toUpperCase());
System.out.println(str.indexOf("a"));
System.out.println(str.lastIndexOf("e"));




System.out.println("/n/n/n/n");
System.out.println("slide 64");

String arr[]= {"nandhini","dinesh","nandhaganesh","raghul","shree","guna","karthik","suba","kavi","keerthana"};
String arra="";
for(int i=0;i<=9;i++) {
	arra+=arr[i];
}
System.out.println(arra);


}}
