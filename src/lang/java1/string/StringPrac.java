package lang.java1.string;

public class StringPrac {
	public static void main(String[] args) {

		String str = "abc";

		System.out.println(System.identityHashCode(str));

		str = "abc" + "def";

		System.out.println(System.identityHashCode(str));
	}
}
