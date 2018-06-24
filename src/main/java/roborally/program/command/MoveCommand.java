package roborally.program.command;

import roborally.Robot;

/**
 * A simple command which makes the robot
 * move one step forward.
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
public class MoveCommand extends SimpleCommand implements Command {

	@Override
	public void execute(Robot robot) {
		if (robot.canMove()) {
			try {
				robot.move();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public String toSource() {
		return "(move)";
	}

}
