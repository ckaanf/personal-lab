package lang.java_beginner.poly.ex;

public class Animal {
	public static void main(String[] args) {
		Cat cat = new Cat();
		Cow cow = new Cow();
		Dog dog = new Dog();

		cat.soundAnimal(cat);
		cow.soundAnimal(cow);
		dog.soundAnimal(dog);


	}
	public void sound(){
		System.out.println("sound");
	}

	public void soundAnimal(Animal animal) {
		System.out.println("soundAnimal");
		animal.sound();
	}
}
