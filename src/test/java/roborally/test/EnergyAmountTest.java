package roborally.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import roborally.EnergyAmount;
import roborally.EnergyAmount.Unit;

public class EnergyAmountTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void getAmount_JtoKJ() {
		double joule = 5;
		EnergyAmount e = new EnergyAmount(5, Unit.JOULE);
		assertEquals(joule, e.getAmount(), 0.1);
		assertEquals(Unit.JOULE, e.getUnit());

		// Convert Joules to kilo Joules
		assertEquals(5 / 1000, e.getAmount(Unit.KILOJOULE), 0.1);
	}

}
