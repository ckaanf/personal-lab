package mid1.prac_enum1.ex2;

public class DiscountService {

	public int discount(ClassGrade grade, int price) {
		int discountPercent = 0;

		//if
		if (grade == ClassGrade.BASIC) {
			discountPercent = 10;
		} else if (grade.equals(ClassGrade.GOLD)) {
			discountPercent = 20;
		} else if (grade.equals(ClassGrade.DIAMOND)) {
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
