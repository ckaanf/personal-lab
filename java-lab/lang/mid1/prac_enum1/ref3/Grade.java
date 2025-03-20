package mid1.prac_enum1.ref3;

public enum Grade {
	BASIC(10),
	GOLD(20),
	DIAMOND(30);

	private final int discountPercent;

	Grade(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public int discount(int price) {
		return price * discountPercent /100;
	}
}
