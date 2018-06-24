package roborally.test;

import org.junit.Test;
import static org.junit.Assert.*;

import roborally.IRobot;
//import roborally.model.Facade;
import roborally.view.Facade;
import roborally.view.IFacade;

public class FacadeTest {

	private IFacade facade;
	
	@org.junit.Before
	public void setUp() {
		facade = new Facade();
	}
	
	@Test
	public void createRobot_NormalCase() {
		IRobot robot = facade.createRobot(5, 7, 0, 5000);
		assertNotNull(robot);
		assertEquals(5, facade.getX(robot));
		assertEquals(7, facade.getY(robot));
		assertEquals(0, facade.getOrientation(robot));
		assertEquals(5000, facade.getEnergy(robot), 0);
	}
	
	@Test
	public void move_NormalCase() {
		IRobot robot = facade.createRobot(5, 7, 1, 5000);
		facade.move(robot);
		assertEquals(6, facade.getX(robot));
		assertEquals(7, facade.getY(robot));
		assertEquals(1, facade.getOrientation(robot));
		assertEquals(5000 - 500, facade.getEnergy(robot), 0.1);
	}
	
	@Test
	public void move_InsufficientEnergy() {
		IRobot robot = facade.createRobot(0, 0, 1, 250);
		facade.move(robot);
		assertEquals(0, facade.getX(robot));
		assertEquals(0, facade.getY(robot));
		assertEquals(1, facade.getOrientation(robot));
		assertEquals(250, facade.getEnergy(robot), 0.1);
	}
}