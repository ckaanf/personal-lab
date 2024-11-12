package effective_software_test;

import static org.assertj.core.api.Assertions.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

	@Test
	void simpleCase() {
		assertThat(
			StringUtils.substringsBetween("abcd","a","d")
		).isEqualTo(new String[] { "bc" });
	}

	@Test
	void manySubStrings() {
		assertThat(
			StringUtils.substringsBetween("abcdabcdab","a","d")
		).isEqualTo(new String[] { "bc", "bc" });
	}

	@Test
	void openAndCloseTagsThatAreLongerThan1Char() {
		assertThat(
			StringUtils.substringsBetween("aabcddaabfddaab","aa","dd")
		).isEqualTo(new String[] { "bc", "bf" });
	}
}
