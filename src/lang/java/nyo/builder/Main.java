package nyo.builder;

public class Main {
	public static void main(String[] args) {
		MakeBuilderPrac makeBuilderPrac = MakeBuilderPrac.builder(1, "name");
		System.out.println(makeBuilderPrac);
	}
}
