package mid1.prac_enum1.ref3;



public class EnumRefMain3_1 {
	public static void main(String[] args) {
		int price = 10000;
		
		System.out.println("BASIC 등급의 할인 금액: " + Grade.BASIC.discount(price));
		System.out.println("GOLD 등급의 할인 금액: " + Grade.GOLD.discount(price));
		System.out.println("DIAMOND 등급의 할인 금액: " + Grade.DIAMOND.discount(price));
	}
}
