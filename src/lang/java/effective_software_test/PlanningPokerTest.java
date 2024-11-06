package craft._20241027;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class PlanningPokerTest {

	@Test
	void rejectNullInput() {
		assertThatThrownBy(
			() -> new PlanningPoker().identifyExtremes(null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void rejectEmptyList() {
		assertThatThrownBy(() -> {
			List<Estimate> emptyList = Collections.emptyList();
			new PlanningPoker().identifyExtremes(emptyList);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void rejectSingleEstimate() {
		assertThatThrownBy(() -> {
			List<Estimate> list = List.of(new Estimate("Eleanor", 1));
			new PlanningPoker().identifyExtremes(list);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void twoEstimates() {
		List<Estimate> list = List.of(
			new Estimate("Maurico", 10),
			new Estimate("Frank", 5)
		);

		List<String> devs = new PlanningPoker().identifyExtremes(list);

		assertThat(devs).containsExactlyInAnyOrder("Maurico", "Frank");

	}

	@Test
	void manyEstimates() {
		List<Estimate> list = List.of(
			new Estimate("Maurico", 10),
			new Estimate("Arie", 5),
			new Estimate("Frank", 7)
		);

		List<String> devs = new PlanningPoker().identifyExtremes(list);

		assertThat(devs).containsExactlyInAnyOrder("Maurico", "Arie");
	}

	@Test
	void developersWithSameEstimates() {
		List<Estimate> list = List.of(
			new Estimate("Maurico", 10),
			new Estimate("Arie", 5),
			new Estimate("Andy", 10),
			new Estimate("Frank", 7),
			new Estimate("Annibale", 5)
		);

		List<String> devs = new PlanningPoker().identifyExtremes(list);

		assertThat(devs).containsExactlyInAnyOrder("Maurico", "Arie");
	}

	@Test
	void allDevelopersWithSameEstimates() {
		List<Estimate> list = List.of(
			new Estimate("Maurico", 10),
			new Estimate("Arie", 10),
			new Estimate("Andy", 10),
			new Estimate("Frank", 10),
			new Estimate("Annibale", 10)
		);

		List<String> devs = new PlanningPoker().identifyExtremes(list);

		assertThat(devs).isEmpty();
	}

	// 속성 기반 테스트
	@Property
	void inAnyOrder(@ForAll("estimates") List<Estimate> estimates) {
		estimates.add(new Estimate("MrLowEstimate", 1));
		estimates.add(new Estimate("MsHighEstimate", 100));

		Collections.shuffle(estimates);

		List<String> devs = new PlanningPoker().identifyExtremes(estimates);

		assertThat(devs).containsExactlyInAnyOrder("MrLowEstimate", "MsHighEstimate");
	}

	@Provide
	Arbitrary<List<Estimate>> estimates() {

		Arbitrary<String> names = Arbitraries.strings()
			.withCharRange('a', 'z').ofLength(5);

		Arbitrary<Integer> values = Arbitraries.integers().between(2, 99);

		Arbitrary<Estimate> estimates = Combinators.combine(names, values)
			.as(Estimate::new);

		return estimates.list().ofMinSize(1);
	}
}