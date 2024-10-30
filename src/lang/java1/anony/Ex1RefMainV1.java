package lang.java1.anony;

import java.util.Random;

public class Ex1RefMainV1 {
	public static void hello(Process process){
		process.run1();
	}
	static class Dice implements Process {
		@Override
		public void run1() {
			//코드 조각 시작
			int randomValue = new Random().nextInt(6) + 1;
			System.out.println("주사위 = " + randomValue);
			//코드 조각 종료
		}
	}

	static class Sum implements Process {
		@Override
		public void run1() {
			for (int i = 1; i <= 3; i++) {
				System.out.println("i = " + i);
			}
		}
	}

	public static void main(String[] args) {
		hello(new Dice());
		hello(new Sum());
	}
}
