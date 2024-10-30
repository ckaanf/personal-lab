package lang.java_mid1.prac_enum1.ex0;

public class StringGradeEx0_2 {

	public static void main(String[] args) {
		int price = 10000;

		DiscountService discountService = new DiscountService();

		// 존재 x
		System.out.println("VIP 등급의 할인 금액: " + discountService.discount("VIP", price));

		//오타 or 소문자
		System.out.println("DIAMOND 등급의 할이 금액: " + discountService.discount("DAIMOND", price));
	}
}
