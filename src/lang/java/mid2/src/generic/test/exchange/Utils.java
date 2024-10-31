package generic.test.exchange;

public class Utils {

    // 위치를 교환하는 메소드
    public static <T> void exchange(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

}