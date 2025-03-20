package org.object.generic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.object.v1.generic.Money;

class MoneyTest {

	@Test
	public void plus() {
		Money won1000 = Money.wons(1000);
		Money won2000 = Money.wons(2000);
		Money won3000 = won1000.plus(won2000);

		assertEquals(Money.wons(3000), won3000);
		assertEquals(Money.wons(1000), won1000);
		assertEquals(Money.wons(2000), won2000);
	}

	@Test
	public void minus() {
		Money won3000 = Money.wons(3000);
		Money won2000 = Money.wons(2000);
		Money won1000 = won3000.minus(won2000);

		assertEquals(Money.wons(1000), won1000);
		assertEquals(Money.wons(3000), won3000);
		assertEquals(Money.wons(2000), won2000);
	}

}