package nyo.clazz;

public class Outer {
	Object[] data;
	public Outer() {}
	public Outer(int size) {
		data = new Object[size];
	}
	Inner getInnerObject() {
		return new Inner();
	}

	//LazyHolder 방식으로 구현한 싱글톤
	static class Inner {
		private static Outer INSTANCE = new Outer(100_000);


	}
	static Outer getOuterObject() {
		return Inner.INSTANCE;
	}
}