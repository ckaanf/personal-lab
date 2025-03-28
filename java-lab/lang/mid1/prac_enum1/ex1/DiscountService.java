package mid1.prac_enum1.ex1;

public class DiscountService {

	public int discount(String grade, int price) {
		int discountPercent = 0;

		//if
		if (grade.equals(StringGrade.BASIC)) {
			discountPercent = 10;
		} else if (grade.equals(StringGrade.GOLD)) {
			discountPercent = 20;
		} else if (grade.equals(StringGrade.DIAMOND)) {
			discountPercent = 30;
		} else {
			System.out.println(grade + ": 할인X");
		}

		// switch
		// switch (grade) {
		// 	case "BASIC" -> discountPercent = 10;
		// 	case "GOLD" -> discountPercent = 20;
		// 	case "DIAMOND" -> discountPercent = 30;
		// 	default -> System.out.println(grade + ": 할인X");
		// }

		return price * discountPercent / 100;
	}
}
