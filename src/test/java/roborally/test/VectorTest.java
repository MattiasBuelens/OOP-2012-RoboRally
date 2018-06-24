package roborally.test;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import roborally.Vector;

public class VectorTest {

	@Test
	public void getNeighbours_NormalCase() {
		testGetNeighbours(Vector.ZERO, 3);
		testGetNeighbours(new Vector(5, 5), 4);
		testGetNeighbours(new Vector(-10, -20), 20);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getNeighbours_NegativeDistance() throws IllegalArgumentException {
		new Vector(1, 1).getNeighbours(-1);
		fail();
	}

	@Test
	public void getNeighbours_ZeroDistance() {
		testGetNeighbours(new Vector(1, 1), 0);
	}

	@Test
	public void getNeighbours_MaxValue() {
		testGetNeighbours(new Vector(Long.MAX_VALUE, Long.MAX_VALUE), 5);
	}

	private void testGetNeighbours(Vector center, long distance) {
		Set<Vector> neighbours = center.getNeighbours(distance);

		if (distance == 0) {
			assertEquals(1, neighbours.size());
		} else {
			assertEquals(distance * 4, neighbours.size());
		}

		for (Vector neighbour : neighbours) {
			long neighbourDistance = neighbour.manhattanDistance(center);
			assertEquals(distance, neighbourDistance);
		}
	}

}
