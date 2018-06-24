package roborally.program;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import roborally.program.command.*;
import roborally.program.condition.*;

/**
 * An enumeration of matchers for program statements.
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
public enum StatementMatcher {

	/*
	 * Commands
	 */

	MOVE("move") {
		@Override
		public Command create() {
			return new MoveCommand();
		}
	},
	TURN("turn") {
		@Override
		public Command create() {
			return new TurnCommand();
		}
	},
	SHOOT("shoot") {
		@Override
		public Command create() {
			return new ShootCommand();
		}
	},
	PICKUP_AND_USE("pickup-and-use") {
		@Override
		public Command create() {
			return new PickupUseCommand();
		}
	},
	SEQUENCE("seq") {
		@Override
		public Command create() {
			return new SequenceCommand();
		}
	},
	IF("if") {
		@Override
		public Command create() {
			return new IfCommand();
		}
	},
	WHILE("while") {
		@Override
		public Command create() {
			return new WhileCommand();
		}
	},

	/*
	 * Conditions
	 */

	TRUE("true") {
		@Override
		public Condition create() {
			return new TrueCondition();
		}
	},
	ENERGY_AT_LEAST("energy-at-least") {
		@Override
		public Condition create() {
			return new EnergyAtLeastCondition();
		}
	},
	AT_ITEM("at-item") {
		@Override
		public Condition create() {
			return new AtItemCondition();
		}
	},
	CAN_HIT_ROBOT("can-hit-robot") {
		@Override
		public Condition create() {
			return new CanHitRobotCondition();
		}
	},
	WALL("wall") {
		@Override
		public Condition create() {
			return new WallCondition();
		}
	},
	AND("and") {
		@Override
		public Condition create() {
			return new AndCondition();
		}
	},
	OR("or") {
		@Override
		public Condition create() {
			return new OrCondition();
		}
	},
	NOT("not") {
		@Override
		public Condition create() {
			return new NotCondition();
		}
	};

	private StatementMatcher(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the statement
	 * this matcher matches on.
	 */
	@Basic
	@Immutable
	public String getName() {
		return name;
	}

	/**
	 * Variable registering the name of the statement.
	 */
	private final String name;

	/**
	 * Check whether this statement matcher matches
	 * the given open token.
	 * 
	 * @param token
	 * 			The open token to match.
	 * @return	True if and only if this matcher's name
	 * 			matches the given token's name.
	 * 			| result == getName().equalsIgnoreCase(token.getName())
	 */
	public boolean matches(OpenToken token) {
		return getName().equalsIgnoreCase(token.getName());
	}

	/**
	 * Create a new statement from this matcher.
	 */
	public abstract Statement create();
}
