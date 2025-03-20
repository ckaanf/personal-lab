package mid1.prac_enum1.ex3;



public class ClassGradeEx3_1 {
	public static void main(String[] args) {
		int price = 10000;

		DiscountService discountService = new DiscountService();
		discountService.discount(Grade.BASIC, price);
		discountService.discount(Grade.GOLD, price);
		discountService.discount(Grade.DIAMOND, price);

		System.out.println("BASIC 등급의 할인 금액: " + discountService.discount(Grade.BASIC, price));
		System.out.println("GOLD 등급의 할인 금액: " + discountService.discount(Grade.GOLD, price));
		System.out.println("DIAMOND 등급의 할인 금액: " + discountService.discount(Grade.DIAMOND, price));
	}
}
