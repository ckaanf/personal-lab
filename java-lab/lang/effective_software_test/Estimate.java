package effective_software_test;

public class Estimate {
	private String developer;
	private int estimate;

	public Estimate(String developer, int estimate) {
		this.developer = developer;
		this.estimate = estimate;
	}

	public int getEstimate() {
		return estimate;
	}

	public String getDeveloper() {
		return developer;
	}
}
