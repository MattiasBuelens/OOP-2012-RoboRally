package roborally.test;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import roborally.*;

@RunWith(Parameterized.class)
public class RobotSurpriseBoxTest {

	Robot carrierBot, terminatedBot;
	Board board;
	SurpriseBox surprise;
	public double repairKitCapacity = 2000.0;

	// Amount of test runs
	private static final int nbRuns = 25;

	// Dummy constructor for parameterized test
	public RobotSurpriseBoxTest(Object o) {
	}

	// Create data points for parameterized test
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[nbRuns][1]);
	}

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);

		carrierBot = new Robot(Orientation.RIGHT, 500);
		carrierBot.placeOnBoard(board, Vector.ZERO);

		surprise = new SurpriseBox(100);
		surprise.placeOnBoard(carrierBot.getBoard(), carrierBot.getPosition());

		carrierBot.pickUp(surprise);
	}

	@Test
	public void use_SurpriseBox() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		SurpriseBox magic = new SurpriseBox(0);
		magic.placeOnBoard(carrierBot.getBoard(), carrierBot.getPosition());

		// Initial robot state
		EnergyAmount capacity = carrierBot.getCapacityAmount();
		int possessions = carrierBot.getNbPossessions();

		carrierBot.pickUp(magic);
		carrierBot.use(magic);

		boolean exploded = carrierBot.getCapacityAmount().isLessThan(capacity);
		boolean newitem = (!exploded) && (possessions == carrierBot.getNbPossessions());
		boolean teleported = (!exploded) && (!newitem) && (carrierBot.getNbPossessions() != possessions);

		assertTrue(exploded ^ newitem ^ teleported);
	}
}
