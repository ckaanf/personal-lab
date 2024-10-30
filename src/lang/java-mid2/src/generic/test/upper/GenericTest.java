package generic.test.upper;

public class GenericTest {
    public static void main(String[] args) {
        Animal[] animals = {new Dog("Buddy"), new Cat("Whiskers"), new Dog("Rex")};
        Printer.printAnimalNames(animals);
    }
}