package mid1.prac_enum1.ref1;


public class DiscountService {

	public int discount(ClassGrade grade, int price) {
		return price * grade.getDiscountPercent() / 100;
	}
}
