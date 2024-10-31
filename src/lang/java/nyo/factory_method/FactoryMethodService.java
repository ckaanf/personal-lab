package nyo.factory_method;

import java.util.HashMap;
import java.util.Map;

public class FactoryMethodService {
	Map<String, String> map = new HashMap<>();

	public void factoryMethod(String query) {

		map.put("title", queryTitle(query));
		map.put("author", queryAuthor(query));

		System.out.println(map.get(query));
	}

	private String queryTitle(String query) {
		return query;
	}

	private String queryAuthor(String query) {
		return query;
	}
}
