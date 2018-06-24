package roborally.program.command;

import java.util.Set;

import roborally.Item;
import roborally.Robot;

/**
 * A simple command which makes the robot
 * pick up and use a random item at its position.
 * 
 * @author	Mattias Buelens
 * @author	Thomas Goossens
 * @version	3.0
 * 
 * @note	This class is part of the 2012 project for
 * 			the course Object Oriented Programming in
 * 			the second phase of the Bachelor of Engineering
 * 			at KU Leuven, Belgium.
 */
public class PickupUseCommand extends SimpleCommand {

	@Override
	public void execute(Robot robot) {
		if (!robot.isPlaced())
			return;

		Set<Item> items = robot.getBoard().getPiecesAt(robot.getPosition(), Item.class);
		if (items.isEmpty())
			return;
		Item item = items.iterator().next();

		robot.pickUp(item);
		robot.use(item);
	}

	@Override
	public String toSource() {
		return "(pickup-and-use)";
	}

}
