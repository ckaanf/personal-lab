package craft;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
	public static void main(String[] args) {
		Order order = new Order(List.of("자동차"), BigDecimal.ONE, "TEST");
		boolean result = validateOrder(order);

	}
	public static boolean validateOrder(Order order) {
		return order.isValidOrder();
	}
}
