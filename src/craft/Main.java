package craft;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	public boolean validateOrder (Order order) {
		if (order.getItems().size() == 0) {
			log.info("주문 항목이 없습니다.");
			return false;
		} else {
			if (order.getTotalPrice() > 0) {
				if (!order.hasCustomerInfo()) {
					log.info("사용자 정보가 없습니다.");
					return false;
				} else {
					return true;
				}
			} else if (!(order.getTotalPrice() > 0)) {
				log.info("올바르지 않은 총 가격입니다.");
				return false;
			}
		}
		return true;
	}

	public class Order {

		private final List<Objects> items;
		private final BigDecimal totalPrice;
		private final Objects customerInfo;

		public Order(List<Objects> items, BigDecimal totalPrice, Objects customerInfo) {
			this.items = items;
			this.totalPrice = totalPrice;
			this.customerInfo = customerInfo;
		}

		public List<Objects> getItems() {
			return items;
		}
		public int getTotalPrice() {
			return totalPrice.intValue();
		}
		public boolean hasCustomerInfo() {
			return customerInfo != null;
		}
	}
}
