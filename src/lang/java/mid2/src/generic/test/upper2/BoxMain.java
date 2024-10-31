package generic.test.upper2;

public class BoxMain {
    public static void main(String[] args) {
        NumberBox<Integer> intBox = new NumberBox<>();
        intBox.set(10);
        System.out.println("Stored value: " + intBox.get());
        System.out.println("Squared value: " + intBox.getSquaredValue());

        NumberBox<Double> doubleBox = new NumberBox<>();
        doubleBox.set(3.14);
        System.out.println("Stored value: " + doubleBox.get());
        System.out.println("Squared value: " + doubleBox.getSquaredValue());
    }

}
