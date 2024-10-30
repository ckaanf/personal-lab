package lang.java1.clazz;

public class Singleton {
	private Singleton(){}

	private class LazyHolder {
		private static Singleton INSTANCE = new Singleton();
	}
	public static Singleton getInstance() {
		System.out.println("싱글톤");
		return LazyHolder.INSTANCE;
	}

}
