package mid1.prac_enum1.ref1;

public class ClassGrade {

	public static final ClassGrade BASIC = new ClassGrade(10);
	public static final ClassGrade GOLD = new ClassGrade(20);
	public static final ClassGrade DIAMOND = new ClassGrade(30);

	private final int discountPercent;
	private ClassGrade(int discountPercent) {
		this.discountPercent = discountPercent;
	}
	public int getDiscountPercent() {
		return discountPercent;
	}
}
