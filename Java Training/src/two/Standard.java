package two;

import java.lang.reflect.Array;

public class Standard {
	public static void main(String[] args) {
	Student m= new Student();
	int[][] arr=new int[4][3];
	m.setStudentName("ravi");
	m.setRegNo(1);
	m.setMarkInEng(100);
	m.setMarkInMaths(100);
	m.setMarkInScience(99);
	arr[0][0]=m.getRegNo();
	arr[0][1]=m.getMarkInEng();
	arr[0][2]=m.getMarkInMaths();
	arr[0][3]=m.getMarkInScience();
	
	Student m1= new Student();
	m1.setStudentName("nandhini");
	m1.setRegNo(1);
	m1.setMarkInEng(100);
	m1.setMarkInMaths(100);
	m1.setMarkInScience(99);
	
	arr[1][0]=m1.getRegNo();
	arr[1][1]=m1.getMarkInEng();
	arr[1][2]=m1.getMarkInMaths();
	arr[1][3]=m1.getMarkInScience();
	
	Student m2= new Student();
	m2.setStudentName("Dinesh");
	m2.setRegNo(1);
	m2.setMarkInEng(100);
	m2.setMarkInMaths(100);
	m2.setMarkInScience(99);
	
	arr[2][0]=m2.getRegNo();
	arr[2][1]=m2.getMarkInEng();
	arr[2][2]=m2.getMarkInMaths();
	arr[2][3]=m2.getMarkInScience();
	
	Student m3= new Student();
	m3.setStudentName("kavi");
	m3.setRegNo(1);
	m3.setMarkInEng(100);
	m3.setMarkInMaths(100);
	m3.setMarkInScience(99);
	
	arr[0][0]=m.getRegNo();
	arr[0][1]=m.getMarkInEng();
	arr[0][2]=m.getMarkInMaths();
	arr[0][3]=m.getMarkInScience();
	
	Student m4= new Student();
	m4.setStudentName("Aishu");
	m4.setRegNo(1);
	m4.setMarkInEng(100);
	m4.setMarkInMaths(100);
	m4.setMarkInScience(99);
	
	arr[0][0]=m.getRegNo();
	arr[0][1]=m.getMarkInEng();
	arr[0][2]=m.getMarkInMaths();
	arr[0][3]=m.getMarkInScience();
	
	}


}
