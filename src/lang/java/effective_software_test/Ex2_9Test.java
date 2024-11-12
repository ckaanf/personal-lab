package effective_software_test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Ex2_9Test {

	@ParameterizedTest
	@MethodSource("digitsOutOfRange")
	void shouldThrowExceptionWhenDigitsAreOutOfRange(List<Integer> left, List<Integer> right) {
		assertThatThrownBy(() -> new Ex2_9().add(left, right)).isInstanceOf(IllegalArgumentException.class);
	}

	@ParameterizedTest
	@MethodSource("testCases")
	void shouldReturnCorrectResult(List<Integer> left, List<Integer> right, List<Integer> expected) {
		assertThat(new Ex2_9().add(left, right)).isEqualTo(expected);
	}

	static Stream<Arguments> testCases() {
		return Stream.of(
			of(null, numbers(7, 2), null),
			of(numbers(), numbers(7, 2), numbers(7, 2)),
			of(numbers(9,8),null,null),
			of(numbers(9,8),numbers(),numbers(9,8)),

			of(numbers(1), numbers(2), numbers(3)),
			of(numbers(9),numbers(2),numbers(1,1)),

			of(numbers(2,2),numbers(3,3),numbers(5,5)),
			of(numbers(2,9),numbers(2,3),numbers(5,2)),
			of(numbers(2,9,3),numbers(1,8,3),numbers(4,7,6)),
			of(numbers(1,7,9),numbers(2,6,8),numbers(4,4,7)),
			of(numbers(1,9,1,7,1),numbers(1,8,1,6,1),numbers(3,7,3,3,2)),
			of(numbers(9,9,8),numbers(1,7,2),numbers(1,1,7,0)),

			of(numbers(2,2),numbers(3),numbers(2,5)),
			of(numbers(3),numbers(2,2),numbers(2,5)),
			of(numbers(2,2),numbers(9), numbers(3,1)),
			of(numbers(9),numbers(2,2), numbers(3,1)),
			of(numbers(1,7,3),numbers(9,2),numbers(2,6,5)),
			of(numbers(9,2), numbers(1,7,3),numbers(2,6,5)),
			of(numbers(3,1,7,9),numbers(2,6,8),numbers(3,4,4,7)),
			of(numbers(2,6,8),numbers(3,1,7,9),numbers(3,4,4,7)),
			of(numbers(1,9,1,7,1), numbers(2,1,8,1,6,1),numbers(2,3,7,3,3,2)),
			of(numbers(2,1,8,1,6,1),numbers(1,9,1,7,1),numbers(2,3,7,3,3,2)),
			of(numbers(9,9,8),numbers(9,1,7,2), numbers(1,0,1,7,0)),
			of(numbers(9,1,7,2), numbers(9,9,8),numbers(1,0,1,7,0)),
			of(numbers(0,0,0,1,2),numbers(0,2,3), numbers(3,5)),
			of(numbers(0,0,0,1,2),numbers(0,2,9), numbers(4,1)),

			of(numbers(9,9), numbers(1), numbers(1,0,0))
		);
	}

	static Stream<Arguments> digitsOutOfRange() {
		return Stream.of(
			of(numbers(1, -1, 1), numbers(1)),
			of(numbers(1), numbers(1, -1, 1)),
			of(numbers(1, 10, 1), numbers(1)),
			of(numbers(1), numbers(1, 11, 1))
		);
	}

	private static List<Integer> numbers(int... nums) {
		List<Integer> list = new ArrayList<>();
		for (int n : nums) list.add(n);
		return list;
	}
}