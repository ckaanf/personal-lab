package well_grounded_java.Ch01;

import java.util.List;
import java.util.Map;

/**
 *  21p ### 1.5.1 컬렉션 팩토리(JEP213)
 */
public class CollectionFactory {
	List<String> list = List.of(
		"a",
		"a",
		"a",
		"a",
		"a",
		"a",
		"a",
		"a",
		"a",
		"a"
	) ;

	Map<String, String> map = Map.of(
		"key", "value"
	);

	Map<String, String> entriesMap = Map.ofEntries(
		Map.entry("key", "value")
	);
}
