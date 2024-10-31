package beginner.super1;

public class ClassB extends ClassA {
	public ClassB() {
	}
	public ClassB (int a) {
		super();
		System.out.println("a: " + a);
		System.out.println("생성자");
	}
}
