package nyo.builder;

public class MakeBuilderPrac {
	private final int id;
	private final String name;
	private Integer age;
	private String gender;


	public MakeBuilderPrac Age(Integer age) {
		this.age = age;
		return this;
	}
	public MakeBuilderPrac Gender(String gender) {
		this.gender = gender;
		return this;
	}
	public MakeBuilderPrac(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public static MakeBuilderPrac builder(int id, String name){
		return new MakeBuilderPrac(id, name);
	}

	public MakeBuilderPrac build(){
		return this;
	}


	@Override
	public String toString() {
		return "MakeBuilderPrac{" +
			"id=" + id +
			", name='" + name + '\'' +
			", age=" + age +
			", gender='" + gender + '\'' +
			'}';
	}
}
