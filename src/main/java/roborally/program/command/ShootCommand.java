package roborally.program.command;

import roborally.Robot;

/**
 * A simple command which makes the robot
 * shoot in the direction it is facing.
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
public class ShootCommand extends SimpleCommand {

	@Override
	public void execute(Robot robot) {
		if (robot.canShoot()) {
			robot.shoot();
		}
	}

	@Override
	public String toSource() {
		return "(shoot)";
	}

}
