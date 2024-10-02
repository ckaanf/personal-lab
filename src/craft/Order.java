package craft;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order {
	private static final Logger log = LoggerFactory.getLogger(Order.class);

	private final List<String> items;
	private final BigDecimal totalPrice;
	private final String customerInfo;

	public Order(List<String> items, BigDecimal totalPrice, String customerInfo) {
		this.items = items;
		this.totalPrice = totalPrice;
		this.customerInfo = customerInfo;
	}

	public boolean isValidOrder() {
		if (itemListIsEmpty()) return false;

		if (isAvailablePrice()) return false;

		return !isCustomerExist();
	}

	private boolean itemListIsEmpty() {
		return validateOutFormatter(items.isEmpty(),"주문 항목이 없습니다.");
	}

	private boolean isAvailablePrice() {
		return validateOutFormatter(totalPrice.compareTo(BigDecimal.ZERO) <= 0, "올바르지 않은 총 가격입니다.");
	}

	private boolean isCustomerExist() {
		return validateOutFormatter(customerInfo == null, "사용자 정보가 없습니다.");
	}

	private boolean validateOutFormatter(boolean condition, String errorMessage) {
		if (condition) {
			log.info(errorMessage);
			return true;
		}
		return false;
	}
}
