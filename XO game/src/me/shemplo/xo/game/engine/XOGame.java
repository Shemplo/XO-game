package me.shemplo.xo.game.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import me.shemplo.xo.game.Main;

public class XOGame {
	
	private final double full = Main.CELL_SIZE;
	private final double half = full / 2;
	private final double sOffX = Main.WIDTH / 2;
	private final double sOffY = Main.HEIGHT / 2;
	
	private Point center, real, selector, previous;
	private Map <String, Cell> points;
	private List <Cell> cells, line;
	private boolean finished;
	private int turn = 0;
	
	public XOGame () {
		_init ();
	}
	
	public void clear () { _init (); }
	
	private void _init () {
		this.cells = new ArrayList <> ();
		this.points = new HashMap <> ();
		
		this.selector = new Point (0, 0);
		this.center = new Point (0, 0);
		this.real = new Point (0, 0);
		this.finished = false;
		this.previous = null;
	}
	
	public void moveCenter (double dx, double dy) {
		center.x += dx; center.y += dy;
	}
	
	public Point getCenter () {
		return new Point (center.x, center.y);
	}
	
	public void moveSelector (double dx, double dy) {
		if (finished) { return; }
		
		if (sOffX - half + (selector.x + dx) * full < 0) {
			moveCenter (+1, 0);
		} else if (sOffX +  half + (selector.x + dx) * full >= Main.WIDTH) {
			moveCenter (-1, 0);
		} else {
			selector.x += dx;
		}
		
		if (sOffY - half + (selector.y + dy) * full < 0) {
			moveCenter (0, +1);
		} else if (sOffY +  half + (selector.y + dy) * full >= Main.HEIGHT) {
			moveCenter (0, -1);
		} else {
			selector.y += dy;
		}
		
		real.x += dx;
		real.y += dy;
	}
	
	public Point getSelector () {
		return new Point (selector.x, selector.y);
	}
	
	public void setCell () {
		if (finished) { return; }
		
		String key = real.x + " " + real.y;
		boolean flag = points.containsKey (key);
		
		if (!flag) {
			Cell cell = new Cell (((turn & 1) == 0 ? "x" : "o"), real.x, real.y);
			cells.add (cell);
			turn ++;
			
			if (previous == null) { previous = new Point (real.x, real.y); }
			previous.x = real.x; previous.y = real.y;
			points.put (key, cell);
			_checkLength (cell);
		}
	}
	
	private void _checkLength (Cell cell) {
		int [][] vectors = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}};
		List <Cell> line = new ArrayList <> ();
		
		for (int i = 0; i < vectors.length; i ++) {
			line.clear ();
			
			int step = 1;
			int [] v = vectors [i];
			
			String key = (cell.location.x + v [0] * step) + " " + (cell.location.y + v [1] * step);
			while (points.containsKey (key) && points.get (key).value.equals (cell.value)) {
				line.add (points.get (key));
				
				step ++;
				key = (cell.location.x + v [0] * step) + " " 
						+ (cell.location.y + v [1] * step);
			}
			
			step = 1;
			v [0] = -v[0];
			v [1] = -v[1];
			
			key = (cell.location.x + v [0] * step) + " " + (cell.location.y + v [1] * step);
			while (points.containsKey (key) && points.get (key).value.equals (cell.value)) {
				line.add (points.get (key));
				
				step ++;
				key = (cell.location.x + v [0] * step) + " " 
						+ (cell.location.y + v [1] * step);
			}
			
			if (line.size () + 1 >= 5) {
				this.line = new ArrayList <> (line);
				finished = true;
			}
		}
	}
	
	public void drawField (GraphicsContext context) {
		context.clearRect (0, 0, Main.WIDTH, Main.HEIGHT);
		
		context.setFont (new Font (Main.CELL_SIZE));
		context.setTextAlign (TextAlignment.CENTER);
		context.setFill (Color.BLACK);
		
		for (Cell cell : cells) {
			Point point = cell.location;
			double x = sOffX + (center.x + point.x) * full,
					y = sOffY + 4 + (center.y + point.y) * full;
			context.fillText (cell.value, x, y);
		}
		
		context.setFill (Color.color (0.1, 0.1, 0.1, 0.1));
		context.fillRect (sOffX + selector.x * full - half, sOffY + selector.y * full - half, full, full);
		
		if (previous != null) {
			context.setFill (Color.color (0.1, 1, 0.1, 0.25));
			context.fillRect (sOffX + (center.x + previous.x) * full - half, 
								sOffY + (center.y + previous.y) * full - half, full, full);
		}
		
		if (finished) {
			context.setFill (Color.color (1, 0.1, 0.1, 0.25));

			for (Cell cell : line) {
				Point point = cell.location;
				context.fillRect (sOffX + (center.x + point.x) * full - half, 
									sOffY + (center.y + point.y) * full - half, full, full);
			}
		}
	}
	
	public static class Point {
		
		private double x, y;
		
		public Point (double x, double y) {
			this.x = x; this.y = y;
		}
		
		public double x () { return x; }
		
		public double y () { return y; }
		
	}
	
	private static class Cell {
		
		private String value;
		private Point location;
		
		public Cell (String value, double x, double y) {
			this.location = new Point (x, y);
			this.value = value;
		}
		
	}
	
}
