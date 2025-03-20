package beginner.poly.basic;

public class Main {
	public static void main(String[] args) {
		checkCasting();
	}

	public static ClassA checkCasting() {
		ClassA casting = new ClassB();
		((ClassB)casting).classBMethod();
		return casting ;
	}
}
