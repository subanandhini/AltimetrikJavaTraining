package two;

public class Test {
public static void main(String[] args) {
	Student s= new Student();
	college c=new college();
	s.setStudentName("nandhu");
	System.out.println(s.getStudentName());
	System.out.println(c instanceof Student);
	System.out.println(s instanceof college);
}
}
