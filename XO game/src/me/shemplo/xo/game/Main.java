package me.shemplo.xo.game;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.shemplo.xo.game.engine.XOGame;

public class Main extends Application {

	public static final double CELL_SIZE = 20,
								WIDTH = 600,
								HEIGHT = 400;
	
	public static void main (String [] args) { launch (args); }

	
	
	private GraphicsContext context;
	private XOGame engine;
	
	public void start (Stage stage) throws Exception {
		Canvas canvas = new Canvas (WIDTH, HEIGHT); // Canvas
		context = canvas.getGraphicsContext2D ();
		
		BorderPane pane = new BorderPane ();
		pane.setCenter (canvas);
		
		Scene scene = new Scene (pane);
		stage.setTitle ("XO Game");
		stage.setScene (scene);
		stage.show ();
		
		engine = new XOGame ();
		engine.drawField (context);
		
		scene.setOnKeyReleased (e -> {
			if (e.getCode ().equals (KeyCode.W) || e.getCode ().equals (KeyCode.UP)) {
				engine.moveSelector (0, -1);
			} else if (e.getCode ().equals (KeyCode.A) || e.getCode ().equals (KeyCode.LEFT)) {
				engine.moveSelector (-1, 0);
			} else if (e.getCode ().equals (KeyCode.S) || e.getCode ().equals (KeyCode.DOWN)) {
				engine.moveSelector (0, +1);
			} else if (e.getCode ().equals (KeyCode.D) || e.getCode ().equals (KeyCode.RIGHT)) {
				engine.moveSelector (+1, 0);
			} else if (e.getCode ().equals (KeyCode.C)) {
				engine.clear ();
			} else if (e.getCode ().equals (KeyCode.ENTER)) {
				engine.setCell ();
			}
			
			engine.drawField (context);
		});
	}
	
}
