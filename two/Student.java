package two;

public class Student extends Marks {
	
String studentName;
int regNo;
public String getStudentName() {
	return studentName;
}
public void setStudentName(String student) {
	if(student.startsWith("n"))
	this.studentName = student;
}
public int getRegNo() {
	return regNo;
}
public void setRegNo(int regNo) {
	
	this.regNo = regNo;

}
@Override
public String toString() {
	return "Student [studentName=" + studentName + ", regNo=" + regNo + ", markInMaths=" + markInMaths
			+ ", markInScience=" + markInScience + ", markInEng=" + markInEng + ", getStudentName()=" + getStudentName()
			+ ", getRegNo()=" + getRegNo() + ", hashCode()=" + hashCode() + ", getMarkInMaths()=" + getMarkInMaths()
			+ ", getMarkInScience()=" + getMarkInScience() + ", getMarkInEng()=" + getMarkInEng() + ", getClass()="
			+ getClass() + ", toString()=" + super.toString() + "]";
}
public static void main(String[] args) {
	Student s= new Student();
	System.out.println(s);
	s.setStudentName("nandhini");


}
@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + regNo;
	result = prime * result + ((studentName == null) ? 0 : studentName.hashCode());
	return result;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Student other = (Student) obj;
	if (regNo != other.regNo)
		return false;
	if (studentName == null) {
		if (other.studentName != null)
			return false;
	} else if (!studentName.equals(other.studentName))
		return false;
	return true;
}



}