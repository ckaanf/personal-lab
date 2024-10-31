package mid1.prac_enum1.ref2;



public class DiscountService {

	public int discount(Grade grade, int price) {

		return price * grade.getDiscountPercent() / 100;
	}
}
