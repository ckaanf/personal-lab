package lang.java_mid1.prac_enum1.ex0;

public class DiscountService {

	public int discount(String grade, int price) {
		int discountPercent = 0;

		switch (grade) {
			case "BASIC" -> discountPercent = 10;
			case "GOLD" -> discountPercent = 20;
			case "DIAMOND" -> discountPercent = 30;
			default -> System.out.println(grade + ": 할인X");
		}

		return price * discountPercent / 100;
	}
}
