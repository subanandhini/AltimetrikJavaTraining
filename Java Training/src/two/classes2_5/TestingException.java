package two.classes2_5;

public class TestingException {
	static int m(int i) {
		try {
			i++;
			if (i == 1)
				throw new Exception();
		}
		catch (Exception e) {
			i += 10;
			return i;// here return is not effective because the finally will be executed
		} finally {
			i += 5;
			return i;// if you do not return i then the output will be 11
		}
//	i++; Unreachable exception will be thrown because the code has been ended
		// return i;
	}

	public static void main(String[] args) {
		//System.out.println(m(0));
		TestingException []t=new TestingException[-1];
	}

	public TestingException() {
		// TODO Auto-generated constructor stub
	}

}
