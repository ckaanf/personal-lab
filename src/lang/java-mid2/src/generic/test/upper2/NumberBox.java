package generic.test.upper2;

public class NumberBox<T extends Number> {

    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public double getSquaredValue() {
        return value.doubleValue() * 2;
    }

}