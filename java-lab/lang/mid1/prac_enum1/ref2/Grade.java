package mid1.prac_enum1.ref2;

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
}
