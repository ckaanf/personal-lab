package mid2.collection.array;

public class MyArrayListV4Main {
	public static void main(String[] args) {
		MyArrayListV4<String> stringList = new MyArrayListV4<>();
		stringList.add("a");
		stringList.add("b");
		stringList.add("c");
		stringList.add("d");

		String string = stringList.get(0);
		System.out.println("String = " + string);

		MyArrayListV4<Integer> intList = new MyArrayListV4<>();
		intList.add(1);
		intList.add(2);
		intList.add(3);

		Integer integer = intList.get(0);
		System.out.println("integer = " + integer);

	}
}
