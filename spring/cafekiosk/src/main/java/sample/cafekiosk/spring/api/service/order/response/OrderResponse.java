package sample.cafekiosk.spring.api.service.order.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.order.Order;

@Getter
public class OrderResponse {

	private Long id;
	private int totalPrice;
	private LocalDateTime registeredDateTime;
	private List<ProductResponse> products;

	@Builder
	private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.registeredDateTime = registeredDateTime;
		this.products = products;
	}

	public static OrderResponse of(Order order) {
		return OrderResponse.builder()
			.id(order.getId())
			.totalPrice(order.getTotalPrice())
			.registeredDateTime(order.getRegisteredDateTime())
			.products(order.getOrderProducts()
				.stream()
				.map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
				.toList())
			.build();
	}
}
