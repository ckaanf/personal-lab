package mid1.prac_enum1.ex0;

public class StringGradeEx0_1 {

	public static void main(String[] args) {
		int price = 10000;

		DiscountService discountService = new DiscountService();


		System.out.println("BASIC 등급의 할인 금액: " + discountService.discount("BASIC", price));
		System.out.println("GOLD 등급의 할인 금액: " + discountService.discount("GOLD", price));
		System.out.println("DIAMOND 등급의 할인 금액: " + discountService.discount("DIAMOND", price));
	}
}
