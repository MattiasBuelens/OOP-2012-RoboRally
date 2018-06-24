package roborally.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class RoboRallyView<Board, Robot, Wall, Battery, RepairKit, SurpriseBox> extends JPanel {

	private static final long serialVersionUID = -5412856771873196193L;
	private final static int TILE_SIZE = 50;

	private final RoboRally<Board, Robot, Wall, Battery, RepairKit, SurpriseBox> roboRally;
	private boolean showGrid = true;

	private int prePressOriginX = 0;
	private int prePressOriginY = 0;
	private int clickX;
	private int clickY;
	private int originX;
	private int originY;

//	private Image robotImage;
//	private Image wallImage;
//	private Image batteryImage;

	public RoboRallyView(final RoboRally<Board, Robot, Wall, Battery, RepairKit, SurpriseBox> roboRally) {
		this.roboRally = roboRally;

//		try {
//			robotImage = ImageIO.read(getClass().getClassLoader().getResource("res/robot1.jpg"));
//			wallImage = ImageIO.read(getClass().getClassLoader().getResource("res/wall.jpg"));
//			batteryImage = ImageIO.read(getClass().getClassLoader().getResource("res/battery.png"));
//			repairkitImage = ImageIO.read(getClass().getClassLoader().getResource("res/repairkit.jpg"));
//			surpriseboxImage = ImageIO.read(getClass().getClassLoader().getResource("res/surprisebox.jpg"));
//		} catch (IOException e) {
//			System.out.println("error reading  images");
//			System.exit(ERROR);
//		}

		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					doPopup(e);
			}

			@Override
			public void mousePressed(MouseEvent e) {
				clickX = e.getX();
				clickY = e.getY();
				prePressOriginX = originX;
				prePressOriginY = originY;

				if (e.isPopupTrigger())
					doPopup(e);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				roboRally.setStatus("");
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {

			}

			private void doPopup(MouseEvent e) {
				Point point = e.getPoint();
				int x = (int) Math.floor((-originX + point.getX()) / (TILE_SIZE + 1));
				int y = (int) Math.floor((-originY + point.getY()) / (TILE_SIZE + 1));

				JPopupMenu contextMenu = roboRally.createContextMenu(x, y);
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point point = e.getPoint();
				int x = (int) Math.floor((-originX + point.getX()) / (TILE_SIZE + 1));
				int y = (int) Math.floor((-originY + point.getY()) / (TILE_SIZE + 1));
				roboRally.setStatus("(" + x + ", " + y + ")");
				IFacade<Board, Robot, Wall, Battery, RepairKit, SurpriseBox> facade = roboRally.getFacade();
				for (Robot robot : facade.getRobots(roboRally.getBoard())) {
					long xr, yr;
					xr = facade.getRobotX(robot);
					yr = facade.getRobotY(robot);
					if (xr == x && yr == y) {
						roboRally.setStatus(roboRally.getRobotName(robot) + ": " + robot.toString());
						return;
					}
				}
				for (Battery battery : facade.getBatteries(roboRally.getBoard())) {
					long xr, yr;
					xr = facade.getBatteryX(battery);
					yr = facade.getBatteryY(battery);
					if (xr == x && yr == y) {
						roboRally.setStatus(roboRally.getBatteryName(battery) + ": " + battery.toString());
						return;
					}
				}
				for (RepairKit repairKit : facade.getRepairKits(roboRally.getBoard())) {
					long xr, yr;
					xr = facade.getRepairKitX(repairKit);
					yr = facade.getRepairKitY(repairKit);
					if (xr == x && yr == y) {
						roboRally.setStatus(roboRally.getRepairKitName(repairKit) + ": " + repairKit.toString());
						return;
					}
				}
				for (SurpriseBox surpriseBox : facade.getSurpriseBoxes(roboRally.getBoard())) {
					long xr, yr;
					xr = facade.getSurpriseBoxX(surpriseBox);
					yr = facade.getSurpriseBoxY(surpriseBox);
					if (xr == x && yr == y) {
						roboRally.setStatus(roboRally.getSurpriseBoxName(surpriseBox) + ": " + surpriseBox.toString());
						return;
					}
				}
				// roboRally.setStatus("");
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				RoboRallyView.this.originX = RoboRallyView.this.prePressOriginX - (clickX - e.getX());
				RoboRallyView.this.originY = RoboRallyView.this.prePressOriginY - (clickY - e.getY());
				repaint();
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = this.getWidth();
		int height = this.getHeight();
		// draw background
		g.setColor(Color.WHITE);

		g.fillRect(0, 0, width, height);
		Image bg = getBackgroundImage();
		if (drawBackground && bg != null) {
			for (int x = originX % (TILE_SIZE + 1) - TILE_SIZE; x <= width + TILE_SIZE; x += TILE_SIZE + 1) {
				for (int y = originY % (TILE_SIZE + 1) - TILE_SIZE; y <= height + TILE_SIZE; y += TILE_SIZE + 1) {
					g.drawImage(bg, x, y, null);
				}
			}
		}
		// mark (0, 0)
		g.setColor(Color.BLACK);
		g.drawString("(0, 0)", originX + 9, originY + 30);
		if (showGrid) {
			// draw vertical grid lines
			for (int x = originX % (TILE_SIZE + 1); x < width; x += TILE_SIZE + 1) {
				g.drawLine(x, 0, x, height - 1);
			}
			// draw horizontal grid lines
			for (int y = originY % (TILE_SIZE + 1); y < height; y += TILE_SIZE + 1) {
				g.drawLine(0, y, width - 1, y);
			}
		}
		// draw batteries
		IFacade<Board, Robot, Wall, Battery, RepairKit, SurpriseBox> facade = roboRally.getFacade();
		for (Battery battery : facade.getBatteries(roboRally.getBoard())) {
			long x = facade.getBatteryX(battery);
			long y = facade.getBatteryY(battery);
			if (x < Integer.MIN_VALUE + 2 * TILE_SIZE || x > Integer.MAX_VALUE - 2 * TILE_SIZE
					|| y < Integer.MIN_VALUE + 2 * TILE_SIZE || y > Integer.MAX_VALUE - 2 * TILE_SIZE)
				continue;
			int tileXRoot = (int) (originX + x * (TILE_SIZE + 1) + 1);
			int tileYRoot = (int) (originY + y * (TILE_SIZE + 1) + 1);
			// draw item
			g.drawImage(getBatteryImage(battery), tileXRoot, tileYRoot, null);
			// draw name
			g.drawString(roboRally.getBatteryName(battery), tileXRoot + 2, tileYRoot + g.getFontMetrics().getAscent()
					- 2);
		}
		// draw repair kits
		for (RepairKit repairKit : facade.getRepairKits(roboRally.getBoard())) {
			long x = facade.getRepairKitX(repairKit);
			long y = facade.getRepairKitY(repairKit);
			if (x < Integer.MIN_VALUE + 2 * TILE_SIZE || x > Integer.MAX_VALUE - 2 * TILE_SIZE
					|| y < Integer.MIN_VALUE + 2 * TILE_SIZE || y > Integer.MAX_VALUE - 2 * TILE_SIZE)
				continue;
			int tileXRoot = (int) (originX + x * (TILE_SIZE + 1) + 1);
			int tileYRoot = (int) (originY + y * (TILE_SIZE + 1) + 1);
			// draw item
			g.drawImage(getRepairKitImage(repairKit), tileXRoot, tileYRoot, null);
			// draw name
			g.drawString(roboRally.getRepairKitName(repairKit), tileXRoot + 2, tileYRoot
					+ g.getFontMetrics().getAscent() - 2);
		}
		// draw surprise boxes
		for (SurpriseBox surpriseBox : facade.getSurpriseBoxes(roboRally.getBoard())) {
			long x = facade.getSurpriseBoxX(surpriseBox);
			long y = facade.getSurpriseBoxY(surpriseBox);
			if (x < Integer.MIN_VALUE + 2 * TILE_SIZE || x > Integer.MAX_VALUE - 2 * TILE_SIZE
					|| y < Integer.MIN_VALUE + 2 * TILE_SIZE || y > Integer.MAX_VALUE - 2 * TILE_SIZE)
				continue;
			int tileXRoot = (int) (originX + x * (TILE_SIZE + 1) + 1);
			int tileYRoot = (int) (originY + y * (TILE_SIZE + 1) + 1);
			// draw item
			g.drawImage(getSurpriseBoxImage(surpriseBox), tileXRoot, tileYRoot, null);
			// draw name
			g.drawString(roboRally.getSurpriseBoxName(surpriseBox), tileXRoot + 2, tileYRoot
					+ g.getFontMetrics().getAscent() - 2);
		}
		// draw robots
		for (Robot robot : facade.getRobots(roboRally.getBoard())) {
			long x = facade.getRobotX(robot);
			long y = facade.getRobotY(robot);
			if (x < Integer.MIN_VALUE + 2 * TILE_SIZE || x > Integer.MAX_VALUE - 2 * TILE_SIZE
					|| y < Integer.MIN_VALUE + 2 * TILE_SIZE || y > Integer.MAX_VALUE - 2 * TILE_SIZE)
				continue;
			int tileXRoot = (int) (originX + x * (TILE_SIZE + 1) + 1);
			int tileYRoot = (int) (originY + y * (TILE_SIZE + 1) + 1);
			// draw robot
			g.drawImage(getRobotImage(robot), tileXRoot, tileYRoot, null);
			// draw name
			g.drawString(roboRally.getRobotName(robot), tileXRoot + 2, tileYRoot + g.getFontMetrics().getAscent() - 2);
			// draw orientation
			int[] xPoints;
			int[] yPoints;
			int orientationXRoot = tileXRoot + TILE_SIZE / 2 - 3;
			int orientationYRoot = tileYRoot + TILE_SIZE / 2 + 8;
			if (roboRally.getFacade().getOrientation(robot) == 0) {
				xPoints = new int[] { orientationXRoot - 6, orientationXRoot, orientationXRoot + 6 };
				yPoints = new int[] { orientationYRoot, orientationYRoot - 6, orientationYRoot };
			} else if (roboRally.getFacade().getOrientation(robot) == 1) {
				xPoints = new int[] { orientationXRoot, orientationXRoot + 6, orientationXRoot };
				yPoints = new int[] { orientationYRoot - 6, orientationYRoot, orientationYRoot + 6 };
			} else if (roboRally.getFacade().getOrientation(robot) == 2) {
				xPoints = new int[] { orientationXRoot - 6, orientationXRoot, orientationXRoot + 6 };
				yPoints = new int[] { orientationYRoot, orientationYRoot + 6, orientationYRoot };
			} else {
				xPoints = new int[] { orientationXRoot, orientationXRoot - 6, orientationXRoot };
				yPoints = new int[] { orientationYRoot - 6, orientationYRoot, orientationYRoot + 6 };
			}
			g.setColor(Color.RED);
			g.fillPolygon(xPoints, yPoints, 3);
			g.setColor(Color.BLACK);
		}
		// draw walls
		for (Wall wall : facade.getWalls(roboRally.getBoard())) {
			long x = facade.getWallX(wall);
			long y = facade.getWallY(wall);
			if (x < Integer.MIN_VALUE + 2 * TILE_SIZE || x > Integer.MAX_VALUE - 2 * TILE_SIZE
					|| y < Integer.MIN_VALUE + 2 * TILE_SIZE || y > Integer.MAX_VALUE - 2 * TILE_SIZE)
				continue;
			int tileXRoot = (int) (originX + x * (TILE_SIZE + 1) + 1);
			int tileYRoot = (int) (originY + y * (TILE_SIZE + 1) + 1);
			g.drawImage(getWallImage(wall), tileXRoot, tileYRoot, null);
		}
	}

	public boolean isGridVisible() {
		return showGrid;
	}

	public void setGridVisible(boolean showGrid) {
		this.showGrid = showGrid;
		repaint();
	}

	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
		repaint();
	}

	private Theme theme;

	public boolean isBackgroundDrawn() {
		return drawBackground;
	}

	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
		repaint();
	}

	private boolean drawBackground = true;

	private Image getRobotImage(Robot robot) {
		return theme.getRobotImage(robot.hashCode());
	}

	private Image getBatteryImage(Battery battery) {
		return theme.getBatteryImage(battery.hashCode());
	}

	private Image getWallImage(Wall wall) {
		return theme.getWallImage(wall.hashCode());
	}

	private Image getRepairKitImage(RepairKit repairKit) {
		return theme.getRepairKitImage(repairKit.hashCode());
	}

	private Image getSurpriseBoxImage(SurpriseBox surpriseBox) {
		return theme.getSurpriseBoxImage(surpriseBox.hashCode());
	}

	private Image getBackgroundImage() {
		return theme.getBackgroundImage();
	}
}
