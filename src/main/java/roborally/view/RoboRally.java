package roborally.view;

import static java.lang.System.out;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

public class RoboRally<Board, Robot, Wall, Battery> extends JFrame {

	private static final long serialVersionUID = 1949718792817670580L;
	private static final long BOARD_WIDTH = 2000;
	private static final long BOARD_HEIGHT = 1000;

	private Map<String, Robot> robots = new HashMap<String, Robot>();
	private Map<String, Battery> batteries = new HashMap<String, Battery>();
	private Board board;
	private final RoboRallyView<Board, Robot, Wall, Battery> view;
	private final IFacade<Board, Robot, Wall, Battery> facade;
	private final JLabel statusBar;

	private List<Theme> themes;

	public RoboRally(IFacade<Board, Robot, Wall, Battery> facade) {
		super("RoboRally");
		this.facade = facade;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		board = facade.createBoard(BOARD_WIDTH, BOARD_HEIGHT);
		this.setAlwaysOnTop(true);
		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		statusBar = new JLabel();
		statusBar.setAlignmentX(LEFT_ALIGNMENT);
		statusBar.setHorizontalTextPosition(SwingConstants.LEFT);
		view = new RoboRallyView<Board, Robot, Wall, Battery>(this);
		themes = getThemes();
		view.setTheme(themes.get(0));
		root.add(view);
		root.add(statusBar);
		this.add(root);
		this.setPreferredSize(new Dimension(400, 400));
		createMenu();
		this.pack();
	}

	void setStatus(String msg) {
		statusBar.setText(msg);
	}

	Board getBoard() {
		return board;
	}

	IFacade<Board, Robot, Wall, Battery> getFacade() {
		return facade;
	}

	String getRobotName(Robot robot) {
		for (Entry<String, Robot> entry : robots.entrySet()) {
			if (entry.getValue() == robot) {
				return entry.getKey();
			}
		}
		return null;
	}

	String getBatteryName(Battery battery) {
		for (Entry<String, Battery> entry : batteries.entrySet()) {
			if (entry.getValue() == battery) {
				return entry.getKey();
			}
		}
		return null;
	}

	private void processCommand(String command) {
		String[] words = command.split(" ");
		if (words[0].equals("addrobot") && 4 <= words.length && words.length <= 5) {
			String name = words[1];
			if (robots.containsKey(name)) {
				out.println("robot named " + name + " already exists");
				return;
			}
			long x, y;
			try {
				x = Long.parseLong(words[2]);
				y = Long.parseLong(words[3]);
			} catch (NumberFormatException ex) {
				out.println("position expected but found " + words[2] + " " + words[3]);
				return;
			}
			double initialEnergy = 10000;
			if (5 <= words.length) {
				try {
					initialEnergy = Double.parseDouble(words[4]);
				} catch (NumberFormatException ex) {
					out.println("double expected but found " + words[4]);
					return;
				}
			}
			Robot newRobot = facade.createRobot(1, initialEnergy);
			if (newRobot != null) {
				robots.put(words[1], newRobot);
				facade.putRobot(board, x, y, newRobot);
			}
		} else if (words[0].equals("addbattery") && 4 <= words.length && words.length <= 6) {
			String name = words[1];
			if (batteries.containsKey(name)) {
				out.println("battery named " + name + " already exists");
				return;
			}
			long x, y;
			try {
				x = Long.parseLong(words[2]);
				y = Long.parseLong(words[3]);
			} catch (NumberFormatException ex) {
				out.println("position expected but found " + words[2] + " " + words[3]);
				return;
			}
			double initialEnergy = 1000;
			if (5 <= words.length) {
				try {
					initialEnergy = Double.parseDouble(words[4]);
				} catch (NumberFormatException ex) {
					out.println("double expected but found " + words[4]);
					return;
				}
			}
			int weight = 1500;
			if (6 <= words.length) {
				try {
					weight = Integer.parseInt(words[5]);
				} catch (NumberFormatException ex) {
					out.println("integer expected but found " + words[5]);
					return;
				}
			}
			Battery newBattery = facade.createBattery(initialEnergy, weight);
			if (newBattery != null) {
				batteries.put(words[1], newBattery);
				facade.putBattery(board, x, y, newBattery);
			}
		} else if (words[0].equals("addwall") && words.length == 3) {
			int x, y;
			try {
				x = Integer.parseInt(words[1]);
				y = Integer.parseInt(words[2]);
			} catch (NumberFormatException ex) {
				out.println("position expected but found " + words[1] + " " + words[2]);
				return;
			}
			Wall wall = facade.createWall();
			if (wall != null) {
				facade.putWall(board, x, y, wall);
			}
		} else if (words[0].equals("move") && words.length == 2) {
			String name = words[1];
			if (!robots.containsKey(name)) {
				out.println("robot named " + name + " does not exist");
				return;
			}
			facade.move(robots.get(name));
		} else if (words[0].equals("turn") && words.length == 2) {
			String name = words[1];
			if (!robots.containsKey(name)) {
				out.println("robot named " + name + " does not exist");
				return;
			}
			facade.turn(robots.get(name));
		} else if (words[0].equals("pickup") && words.length == 3) {
			String rname = words[1];
			if (!robots.containsKey(rname)) {
				out.println("robot named " + rname + " does not exist");
				return;
			}
			String iname = words[2];
			if (!batteries.containsKey(iname)) {
				out.println("battery named " + iname + " does not exist");
				return;
			}
			facade.pickUp(robots.get(rname), batteries.get(iname));
		} else if (words[0].equals("use") && words.length == 3) {
			String rname = words[1];
			if (!robots.containsKey(rname)) {
				out.println("robot named " + rname + " does not exist");
				return;
			}
			String iname = words[2];
			if (!batteries.containsKey(iname)) {
				out.println("battery named " + iname + " does not exist");
				return;
			}
			facade.use(robots.get(rname), batteries.get(iname));
		} else if (words[0].equals("drop") && words.length == 3) {
			String rname = words[1];
			if (!robots.containsKey(rname)) {
				out.println("robot named " + rname + " does not exist");
				return;
			}
			String iname = words[2];
			if (!batteries.containsKey(iname)) {
				out.println("battery named " + iname + " does not exist");
				return;
			}
			facade.drop(robots.get(rname), batteries.get(iname));
		} else if (words[0].equals("moveto") && words.length == 3) {
			String rname = words[1];
			if (!robots.containsKey(rname)) {
				out.println("robot named " + rname + " does not exist");
				return;
			}
			String rname2 = words[2];
			if (!robots.containsKey(rname2)) {
				out.println("robot named " + rname2 + " does not exist");
				return;
			}
			facade.moveNextTo(robots.get(rname), robots.get(rname2));
		} else if (words[0].equals("shoot") && words.length == 2) {
			String rname = words[1];
			if (!robots.containsKey(rname)) {
				out.println("robot named " + rname + " does not exist");
				return;
			}
			facade.shoot(robots.get(rname));
		} else if (words[0].equals("canreach") && words.length == 4) {
			String name = words[1];
			if (!robots.containsKey(name)) {
				out.println("robot named " + name + " does not exist");
				return;
			}
			int x, y;
			try {
				x = Integer.parseInt(words[2]);
				y = Integer.parseInt(words[3]);
			} catch (NumberFormatException ex) {
				out.println("position expected but found " + words[2] + " " + words[3]);
				return;
			}

			double required = facade.getMinimalCostToReach(robots.get(name), x, y);
			if (required == -1) {
				out.println("no (blocked by obstacles)");
			} else if (required == -2) {
				out.println("no (insufficient energy)");
			} else {
				out.println("yes (consuming " + required + " ws)");
			}
		} else if (words[0].equals("help") && words.length == 1) {
			out.println("commands:");
			out.println("\taddbattery <bname> <long> <long> [<double>] [<int>]");
			out.println("\taddwall <long> <long>");
			out.println("\taddrobot <rname> <long> <long> [<double>]");
			out.println("\tmove <rname>");
			out.println("\tturn <rname>");
			out.println("\tshoot <rname>");
			out.println("\tpickup <rname> <bname>");
			out.println("\tuse <rname> <bname>");
			out.println("\tdrop <rname> <bname>");
			out.println("\tcanreach <rname> <long> <long>");
			out.println("\tmoveto <rname> <long> <long>");
			out.println("\texit");
		} else {
			out.println("unknown command");
		}
	}

	private String readCommand(BufferedReader reader) {
		try {
			out.print(">");
			out.flush();
			return reader.readLine();
		} catch (IOException e) {
			out.println("error reading from standard in");
			System.exit(ERROR);
			return null;
		}
	}

	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = readCommand(reader);
		while (command != null) {
			if (command.equals("exit")) {
				break;
			} else {
				processCommand(command);
				view.repaint();
			}
			command = readCommand(reader);
		}
		out.println("bye");
	}

	public static void main(String[] args) {
		// modify the code between <begin> and <end> (substitute the generic
		// arguments with your classes and replace
		// roborally.model.Facade with your facade implementation)
		/* <begin> */
		RoboRally<roborally.Board, roborally.Robot, roborally.Wall, roborally.Battery> roboRally = new RoboRally<roborally.Board, roborally.Robot, roborally.Wall, roborally.Battery>(
				new roborally.view.Facade());
		/* <end> */
		roboRally.setVisible(true);
		roboRally.run();
	}

	/*
	 * Themes
	 */
	private void switchTheme(Theme theme) {
		view.setTheme(theme);
	}

	private List<Theme> getThemes() {
		URL url = ClassLoader.getSystemClassLoader().getResource("res");
		return Theme.getThemes(url, "default");
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(createViewMenu());
		menuBar.add(createThemeMenu());
		this.setJMenuBar(menuBar);
	}

	private JMenu createViewMenu() {
		JMenu viewMenu = new JMenu("View");
		final JCheckBoxMenuItem showGridItem = new JCheckBoxMenuItem("Show Grid");
		showGridItem.setSelected(view.isGridVisible());
		showGridItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.setGridVisible(showGridItem.isSelected());
			}
		});
		viewMenu.add(showGridItem);
		final JCheckBoxMenuItem alwaysOnTopItem = new JCheckBoxMenuItem("Always on top");
		alwaysOnTopItem.setSelected(true);
		alwaysOnTopItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RoboRally.this.setAlwaysOnTop(alwaysOnTopItem.isSelected());
			}
		});
		viewMenu.add(alwaysOnTopItem);
		final JCheckBoxMenuItem drawBackgrondItem = new JCheckBoxMenuItem("Draw background");
		drawBackgrondItem.setSelected(view.isBackgroundDrawn());
		drawBackgrondItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.setDrawBackground(drawBackgrondItem.isSelected());
			}
		});
		viewMenu.add(drawBackgrondItem);
		return viewMenu;
	}

	private JMenu createThemeMenu() {
		JMenu themeMenu = new JMenu("Theme");

		ListIterator<Theme> it = themes.listIterator();
		ButtonGroup themeGroup = new ButtonGroup();
		while (it.hasNext()) {
			int i = it.nextIndex();
			Theme theme = it.next();

			JRadioButtonMenuItem themeItem = createThemeMenuItem(theme);
			themeGroup.add(themeItem);
			themeMenu.add(themeItem);
			themeItem.setSelected(i == 0);
		}

		return themeMenu;
	}

	private JRadioButtonMenuItem createThemeMenuItem(final Theme theme) {
		final JRadioButtonMenuItem themeItem = new JRadioButtonMenuItem(theme.getName());
		themeItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (themeItem.isSelected())
					RoboRally.this.switchTheme(theme);
			}
		});
		return themeItem;
	}

	/*
	 * Context menu
	 */

	public JPopupMenu createContextMenu(final int x, final int y) {
		final JPopupMenu contextMenu = new JPopupMenu();

		boolean hasPieceAt = false;
		for (Robot robot : getRobotsAt(x, y)) {
			contextMenu.add(createContextRobotMenu(robot));
			hasPieceAt = true;
		}
		for (Battery battery : getBatteriesAt(x, y)) {
			contextMenu.add(createContextBatteryMenu(battery));
			hasPieceAt = true;
		}
		for (Wall wall : getWallsAt(x, y)) {
			contextMenu.add(createContextWallMenu(wall));
			hasPieceAt = true;
		}

		if (hasPieceAt)
			contextMenu.addSeparator();

		createContextAddMenu(contextMenu, x, y);
		return contextMenu;
	}

	private JMenu createContextRobotMenu(final Robot robot) {
		final long x = facade.getRobotX(robot);
		final long y = facade.getRobotY(robot);
		final JMenu robotMenu = new JMenu("Robot " + getRobotName(robot));

		final JMenuItem moveItem = new JMenuItem("Move");
		moveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				facade.move(robot);
				view.repaint();
			}
		});
		robotMenu.add(moveItem);

		final JMenuItem turnItem = new JMenuItem("Turn");
		turnItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				facade.turn(robot);
				view.repaint();
			}
		});
		robotMenu.add(turnItem);

		final JMenuItem shootItem = new JMenuItem("Shoot");
		shootItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				facade.shoot(robot);
				view.repaint();
			}
		});
		robotMenu.add(shootItem);

		final JMenu moveToMenu = new JMenu("Move to");
		for (final Robot otherRobot : facade.getRobots(board)) {
			if (robot == otherRobot)
				continue;

			final JMenuItem moveToItem = new JMenuItem(getRobotName(otherRobot));
			moveToItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					facade.moveNextTo(robot, otherRobot);
				}
			});
			moveToMenu.add(moveToItem);
		}
		robotMenu.add(moveToMenu);
		robotMenu.addSeparator();

		final JMenu pickUpMenu = new JMenu("Pick up");
		for (final Battery battery : getBatteriesAt(x, y)) {
			final JMenuItem pickUpItem = new JMenuItem(getBatteryName(battery));
			pickUpItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					facade.pickUp(robot, battery);
					view.repaint();
				}
			});
			pickUpMenu.add(pickUpItem);
		}
		robotMenu.add(pickUpMenu);

		final JMenu useMenu = new JMenu("Use");
		final JMenu dropMenu = new JMenu("Drop");
		for (final Battery battery : facade.getPossessions(robot)) {
			final JMenuItem useItem = new JMenuItem(getBatteryName(battery));
			useItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					facade.use(robot, battery);
				}
			});
			useMenu.add(useItem);

			final JMenuItem dropItem = new JMenuItem(getBatteryName(battery));
			dropItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					facade.drop(robot, battery);
					view.repaint();
				}
			});
			dropMenu.add(dropItem);
		}
		robotMenu.add(useMenu);
		robotMenu.add(dropMenu);

		final JMenuItem terminateItem = new JMenuItem("Terminate");
		terminateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				facade.terminateRobot(robot);
				view.repaint();
			}
		});
		robotMenu.addSeparator();
		robotMenu.add(terminateItem);

		return robotMenu;
	}

	private JMenu createContextBatteryMenu(final Battery battery) {
		final JMenu batteryMenu = new JMenu("Battery " + getBatteryName(battery));

		final JMenuItem terminateItem = new JMenuItem("Terminate");
		terminateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				facade.terminateBattery(battery);
				view.repaint();
			}
		});
		batteryMenu.add(terminateItem);

		return batteryMenu;
	}

	private JMenu createContextWallMenu(final Wall wall) {
		final JMenu wallMenu = new JMenu("Wall");

		final JMenuItem terminateItem = new JMenuItem("Terminate");
		terminateItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				facade.terminateWall(wall);
				view.repaint();
			}
		});
		wallMenu.add(terminateItem);

		return wallMenu;
	}

	public void createContextAddMenu(final JPopupMenu menu, final int x, final int y) {
		final JMenuItem addRobotItem = new JMenuItem("Add robot");
		addRobotItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = promptName("Enter a name for the new robot");
				if (name == null || name.trim().isEmpty())
					return;

				StringBuilder cmd = new StringBuilder();
				cmd.append("addrobot").append(' ').append(name).append(' ');
				cmd.append(x).append(' ').append(y);
				processCommand(cmd.toString());
				view.repaint();
			}
		});
		menu.add(addRobotItem);

		final JMenuItem addBatteryItem = new JMenuItem("Add battery");
		addBatteryItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = promptName("Enter a name for the new battery");
				if (name == null || name.trim().isEmpty())
					return;

				StringBuilder cmd = new StringBuilder();
				cmd.append("addbattery").append(' ').append(name).append(' ');
				cmd.append(x).append(' ').append(y);
				processCommand(cmd.toString());
				view.repaint();
			}
		});
		menu.add(addBatteryItem);

		final JMenuItem addWallItem = new JMenuItem("Add wall");
		addWallItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder cmd = new StringBuilder();
				cmd.append("addwall").append(' ').append(x).append(' ').append(y);
				processCommand(cmd.toString());
				view.repaint();
			}
		});
		menu.add(addWallItem);
	}

	private String promptName(String message) {
		return JOptionPane.showInputDialog(this.getMostRecentFocusOwner(), message);
	}

	private Set<Robot> getRobotsAt(long x, long y) {
		Set<Robot> robots = new HashSet<Robot>();
		for (Robot robot : facade.getRobots(board)) {
			if (facade.getRobotX(robot) == x && facade.getRobotY(robot) == y)
				robots.add(robot);
		}
		return robots;
	}

	private Set<Battery> getBatteriesAt(long x, long y) {
		Set<Battery> batteries = new HashSet<Battery>();
		for (Battery battery : facade.getBatteries(board)) {
			if (facade.getBatteryX(battery) == x && facade.getBatteryY(battery) == y)
				batteries.add(battery);
		}
		return batteries;
	}

	private Set<Wall> getWallsAt(long x, long y) {
		Set<Wall> walls = new HashSet<Wall>();
		for (Wall wall : facade.getWalls(board)) {
			if (facade.getWallX(wall) == x && facade.getWallY(wall) == y)
				walls.add(wall);
		}
		return walls;
	}
}
