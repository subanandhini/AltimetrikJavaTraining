package two.classes2_5;

import java.util.Comparator;

import two.Student;

public class ComparatorExample {
        public static void sort(Student[] s){
		class NameSort implements Comparator{
		Arrays.sort(
		s,new Comparator<Student>(){
		public int compare(Student s1, Student s2){
		return s1.getName(s1).compareTo(s2.getName( s2));
		}}
		);

		for(Student s1:s){
		System.out.println(s1);
		}
		}

}
