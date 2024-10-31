package generic.test.converter;

public class ConversionTest {
    public static void main(String[] args) {
        Converter<String, Integer> stringToInt = new StringToIntegerConverter();
        Converter<Integer, String> intToString = new IntegerToStringConverter();

        System.out.println("String to Int: " + stringToInt.convert("123")); // 출력: 123
        System.out.println("Int to String: " + intToString.convert(456)); // 출력: "456"
    }
}