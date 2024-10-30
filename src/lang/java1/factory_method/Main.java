package lang.java1.factory_method;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {

		FactoryMethodService factoryMethodService = new FactoryMethodService();

		Scanner scanner = new Scanner(System.in);

		factoryMethodService.factoryMethod(scanner.nextLine());
	}
}
