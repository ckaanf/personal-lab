package generic.test.converter;

public class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String input) {
        return Integer.valueOf(input);
    }
}