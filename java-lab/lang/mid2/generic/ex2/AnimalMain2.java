package mid2.generic.ex2;

import mid2.generic.animal.Animal;
import mid2.generic.animal.Cat;
import mid2.generic.animal.Dog;

public class AnimalMain2 {

    public static void main(String[] args) {
        Animal animal = new Animal("동물", 0);
        Dog dog = new Dog("멍멍이", 100);
        Cat cat = new Cat("냐옹이", 50);

        Box<Animal> animalBox = new Box<>();
        animalBox.set(animal);
        animalBox.set(dog); // Animal = Dog
        animalBox.set(cat); // Animal = Cat
        Animal findAnimal = animalBox.get();
        System.out.println("findAnimal = " + findAnimal);
    }
}