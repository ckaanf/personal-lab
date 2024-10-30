package lang.java_mid1.prac_enum1.ex1;

import lang.java_mid1.prac_enum1.ex0.DiscountService;

public class StringGradeEx1_1 {

	public static void main(String[] args) {
		int price = 10000;

		DiscountService discountService = new DiscountService();

		System.out.println("BASIC 등급의 할인 금액: " + discountService.discount(StringGrade.BASIC, price));
		System.out.println("GOLD 등급의 할인 금액: " + discountService.discount(StringGrade.GOLD, price));
		System.out.println("DIAMOND 등급의 할인 금액: " + discountService.discount(StringGrade.DIAMOND, price));
	}
}
