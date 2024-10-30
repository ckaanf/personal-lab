package generic.test.exchange;

import java.util.Arrays;

public class GenericTest {
    public static void main(String[] args) {
        Integer[] numbers = {1, 3, 4, 2};
        Utils.exchange(numbers, 0, 1);
        System.out.println(Arrays.toString(numbers)); // 출력: [3, 1, 4, 2]

        String[] words = {"apple", "pear", "orange", "banana"};
        Utils.exchange(words, 0, 3);
        System.out.println(Arrays.toString(words)); // 출력: [banana, pear, orange, apple]
    }
}