package sample.cafekiosk.spring.api.service.order.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

	private List<String> productNumbers;

	@Builder
	private OrderCreateServiceRequest(final List<String> productNumbers) {
		this.productNumbers = productNumbers;
	}

}
