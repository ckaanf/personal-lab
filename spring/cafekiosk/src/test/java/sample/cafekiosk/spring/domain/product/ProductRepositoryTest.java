package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

// Repository Test는 통합보다 단위 테스트에 조금 더 가까움
// @SpringBootTest
@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@DisplayName("원하는 판매상태를 가진 상품들을 조회한다")
	@Test
	void findAllBySellingStatusIn() {
		Product product1 = Product.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		Product product3 = Product.builder()
			.productNumber("002")
			.type(HANDMADE)
			.sellingStatus(HOLD)
			.name("카페라떼")
			.price(4000)
			.build();

		Product product2 = Product.builder()
			.productNumber("003")
			.type(HANDMADE)
			.sellingStatus(STOP_SELLING)
			.name("팥빙수")
			.price(7000)
			.build();

		productRepository.saveAll(List.of(product1,product2,product3));

		//when
		List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING,HOLD));

		//then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001","아메리카노", SELLING),
				tuple("002","카페라떼", HOLD)
			);
	}

	@DisplayName("상품 번호리스트로 상품들을 조회한다.")
	@Test
	void findAllByProductNumberIn() {
		Product product1 = Product.builder()
			.productNumber("001")
			.type(HANDMADE)
			.sellingStatus(SELLING)
			.name("아메리카노")
			.price(4000)
			.build();

		Product product3 = Product.builder()
			.productNumber("002")
			.type(HANDMADE)
			.sellingStatus(HOLD)
			.name("카페라떼")
			.price(4000)
			.build();

		Product product2 = Product.builder()
			.productNumber("003")
			.type(HANDMADE)
			.sellingStatus(STOP_SELLING)
			.name("팥빙수")
			.price(7000)
			.build();

		productRepository.saveAll(List.of(product1,product2,product3));

		//when
		List<Product> products = productRepository.findAllByProductNumberIn(List.of("001","002"));

		//then
		assertThat(products).hasSize(2)
			.extracting("productNumber", "name", "sellingStatus")
			.containsExactlyInAnyOrder(
				tuple("001","아메리카노", SELLING),
				tuple("002","카페라떼", HOLD)
			);
	}

}