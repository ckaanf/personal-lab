package generic.test.upper;

public class Printer {

    public static <T extends Animal> void printAnimalNames(T[] animals) {
        for (T animal : animals) {
            System.out.println(animal.name);
        }
    }

}