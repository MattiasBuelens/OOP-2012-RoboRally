package roborally;

import java.util.Random;

import roborally.EnergyAmount.Unit;
import be.kuleuven.cs.som.annotate.Model;

/**
 * A surprise box in a game of RoboRally.
 * 
 * <p>A surprise box is an item that can hit, teleport or give a new item
 * to a robot when it is used by a robot. When a surprise box is hit
 * by a laser or explosion, it explodes and hits all adjacent pieces.</p>
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
public class SurpriseBox extends Item {

	/**
	 * Create a new surprise box with the weight.
	 * 
	 * @param weight
	 * 			The weight for this new surprise box.

	 * @post	The maximum amount of energy of the new battery equals 5000 Ws.
	 * 			| new.getMaximumEnergy() == 5000.0
	 * @effect	The new battery is initialized as a new item
	 * 			with the given weight.
	 * 			| super(weight)
	 */
	public SurpriseBox(int weight) {
		super(weight);
	}

	/*
	 * Using
	 */

	/**
	 * Use this surprise box on a given robot.
	 * 
	 * @effect	A random surprise action is selected and
	 * 			executed on the given robot.
	 * 			| getAction().act()
	 * @effect	This surprise box is removed as one of the possessions
	 * 			of the given robot.
	 * 			| robot.removeAsPossession(this)
	 * @effect	This surprise box is terminated.
	 * 			| terminate()
	 */
	@Override
	public void use(Robot robot) throws IllegalStateException, IllegalArgumentException {
		if (isTerminated())
			throw new IllegalStateException("Cannot use terminated items.");
		if (robot == null)
			throw new IllegalArgumentException("Robot must be effective.");
		if (!robot.hasAsPossession(this))
			throw new IllegalArgumentException("Robot must have this item as one of its possessions.");

		getRandomAction().act(robot);
		robot.removeAsPossession(this);
		this.terminate();
	}

	/**
	 * Get a random surprise action.
	 *  
	 * @return	The returned action is effective.
	 * 			| result != null
	 */
	@Model
	Action getRandomAction() {
		return Action.getRandom();
	}

	/*
	 * Shooting
	 */

	/**
	 * @effect	The surprise box explodes when it is hit.
	 * 			| explode()
	 */
	@Override
	public void hit() {
		if (isTerminated())
			throw new IllegalStateException("Surprise box must not be terminated.");

		this.explode();
	}

	/**
	 * Make this surprise box explode and hit all adjacent pieces.
	 * 
	 * @effect	All pieces adjacent to this surprise box on the board
	 * 			are hit by the explosion.
	 * 			| let 
	 * 			| 	neighbours = getPosition().getNeighbours()
	 * 			| 	targets = {t:Piece | t.getBoard() == this.getBoard()
	 * 			|      && for some neighbour in neighbours :
	 * 			|           t.getPosition().equals(neighbour)
	 * 			|   }
	 * 			|
	 * 			| for each target in targets :
	 * 			|   target.hit()
	 * 			
	 * @post 	This surprise box is terminated.
	 * 			| new.isTerminated()
	 * 
	 * @throws	IllegalStateException
	 * 			If this surprise box is not placed on any board.
	 * 			| !isPlaced()
	 */
	@Model
	void explode() throws IllegalStateException {
		if (!isPlaced())
			throw new IllegalStateException("Surprise box must be placed on a board.");

		// Store position and board before terminating
		Board board = getBoard();
		Vector pos = getPosition();

		// Terminate this surprise box
		terminate();

		// All pieces on all adjacent position are hit
		for (Vector target : pos.getNeighbours()) {
			for (Piece piece : board.getPiecesAt(target)) {
				piece.hit();
			}
		}
	}

	/**
	 * An enumeration of possible actions that a surprise box
	 * can perform when it is used by a robot.
	 */
	protected enum Action {
		/**
		 * The surprise action where the surprise box explodes.
		 */
		EXPLODE {
			/**
			 * @effect	The robot responds to the hit from the explosion.
			 * 			| robot.hit()
			 */
			@Override
			public void act(Robot robot) {
				robot.hit();
			}
		},
		/**
		 * The surprise action where the robot is teleported.
		 */
		TELEPORT {
			/**
			 * @effect	The robot moves to a random valid position on the board.
			 * 			| let
			 * 			|   randomPosition = robot.getBoard().getRandomPosition(robot)
			 * 			| robot.moveOnBoard(randomPosition)
			 */
			@Override
			public void act(Robot robot) {
				Vector randomPosition = robot.getBoard().getRandomPosition(robot);
				robot.moveOnBoard(randomPosition);
			}
		},
		/**
		 * The surprise action where a new item is given to the robot.
		 */
		ITEM {
			/**
			 * @effect	The robot receives a new random item.
			 * 			| let
			 * 			|   randomItem = ItemFactory.getRandom().create()
			 * 			| robot.addAsPossession(item)
			 */
			@Override
			public void act(Robot robot) {
				Item item = ItemFactory.getRandom().create();
				robot.addAsPossession(item);
			}
		};

		/**
		 * Perform this action on the given robot.
		 * 
		 * @param robot
		 * 			The robot to perform this action on.
		 */
		public abstract void act(Robot robot);

		/**
		 * Get a random surprise action.
		 */
		public static Action getRandom() {
			Action[] actions = Action.values();
			return actions[new Random().nextInt(actions.length)];
		}
	}

	/**
	 * An enumeration of factories for new items
	 * that can be created by a surprise box.
	 */
	protected enum ItemFactory {
		/**
		 * The factory for new batteries.
		 */
		BATTERY {
			/**
			 * Create a new battery.
			 * 
			 * @return	The created battery weighs 1500 grams.
			 * 			| result.getWeight() == 1500
			 * @return	The created battery has an energy amount of 1000 Ws. 
			 * 			| result.getEnergyAmount(Unit.WATTSECOND) == 1000.0
			 */
			@Override
			public Item create() {
				return new Battery(1500, 1000.0, Unit.WATTSECOND);
			}
		},
		/**
		 * The factory for new repair kits.
		 */
		REPAIR_KIT {
			/**
			 * Create a new repair kit.
			 * 
			 * @return	The created repair kit weighs 1000 grams.
			 * 			| result.getWeight() == 1000
			 * @return	The created repair kit has a capacity of 1000 Ws.
			 * 			| result.getCapacityAmount(Unit.WATTSECOND) == 1000.0
			 */
			@Override
			public Item create() {
				return new RepairKit(1000, 1000.0, Unit.WATTSECOND);
			}
		},
		/**
		 * The factory for new surprise boxes.
		 */
		SURPRISE_BOX {
			/**
			 * Create a new surprise box.
			 * 
			 * @return	The created surprise box weighs 2000 grams.
			 * 			| result.getWeight() == 2000
			 */
			@Override
			public Item create() {
				SurpriseBox item = new SurpriseBox(2000);
				return item;
			}
		};

		/**
		 * Create a new item.
		 */
		public abstract Item create();

		/**
		 * Get a random item factory.
		 */
		public static ItemFactory getRandom() {
			ItemFactory[] factories = ItemFactory.values();
			return factories[new Random().nextInt(factories.length)];
		}
	}

}
