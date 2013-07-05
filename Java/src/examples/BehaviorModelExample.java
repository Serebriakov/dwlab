package examples;

import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.*;
import dwlab.visualizers.Color;
import dwlab.visualizers.MarchingAnts;
import examples.AwPossum.*;
import examples.Jelly.*;

public class BehaviorModelExample extends Project {
	static {
		Graphics.init();
	}
	
	static BehaviorModelExample instance = new BehaviorModelExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	public static int bricks = 1;
	public static double deathPeriod = 1.0;

	public static World world;
	public static Layer layer;
	public static TileMap tileMap;
	public static Sprite selectedSprite;
	public static MarchingAnts marchingAnts = new MarchingAnts();

	public static int score;

	public ButtonAction KeyExit = ButtonAction.create( KeyboardKey.create( Key.SPACE ) );
	
	
	@Override
	public void init() {
	 	world = World.fromFile( "res/jellys.lw" );

		Graphics.init();

		Sprite sprite = new Sprite( Camera.current );
		sprite.visualizer.image = new Image( "res/scheme2.png" );
		while ( !KeyExit.wasPressed() ) {
			sprite.draw();
			processEvents();
			Graphics.swapBuffers();
		}

		sprite.visualizer.image = new Image( "res/scheme1.png" );
		processEvents();
		while ( !KeyExit.wasPressed() ) {
			sprite.draw();
			processEvents();
			Graphics.swapBuffers();
		}

		processEvents();
		initLevel();
	}

	
	public void initLevel() {
		layer = world.findShape( "Level" ).load().toLayer();
		tileMap = layer.findShape( "Field" ).toTileMap();
		layer.init();
	}

	
	ButtonAction buttonSelect = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	@Override
	public void logic() {
		Camera.current.jumpTo( tileMap );
		if( buttonSelect.wasPressed() ) selectedSprite = cursor.lastCollidedSpriteOf( layer );
		layer.act();
		//if( KeyExit.wasPressed() ) exiting = true;
	}

	
	@Override
	public void render() {
		layer.draw();
		if( selectedSprite != null ) {
			selectedSprite.showModels( 100, "" );
			selectedSprite.drawUsingVisualizer( marchingAnts );
		}
		showDebugInfo();
		printText( "Guide AwesomePossum to exit from maze using arrow and space keys", Align.TO_RIGHT, Align.TO_TOP );
		printText( "you can view sprite behavior models by clicking left mouse button on it", Align.TO_RIGHT, Align.TO_TOP, 1 );
		printText( "Score" + Service.firstZeroes( score, 6 ), Align.TO_RIGHT, Align.TO_BOTTOM );
		printText( "LTBehaviorModel example", Align.TO_CENTER, Align.TO_BOTTOM );
	}


	public static class Score extends Sprite {
		static double speed = 2.0;
		static double period = 3.0;

		int amount;
		double startingTime;

		public static void create( Sprite sprite, int amount ) {
			Score scoreObject = new Score();
			scoreObject.setCoords( sprite.getX(), sprite.topY() );
			scoreObject.amount = amount;
			scoreObject.setDiameter( 0 );
			scoreObject.startingTime = BehaviorModelExample.instance.time;
			scoreObject.insertTo( layer );
			score += amount;
		}

		@Override
		public void act() {
			move( 0, -speed );
			if( BehaviorModelExample.instance.time > startingTime + period ) removeFrom( layer );
		}

		@Override
		public void draw( Color drawingColor ) {
			printText( "+" + amount, Align.TO_CENTER, Align.TO_BOTTOM );
		}
	}


	public static class Restart extends Project {
		public long startingTime2 = System.currentTimeMillis();
		public boolean initialized;

		@Override
		public void render() {
			if( System.currentTimeMillis() < startingTime2 + 2000 ) {
				render();
				Camera.current.darken( 0.0005 * ( System.currentTimeMillis() - startingTime2 ) );
			} else if( System.currentTimeMillis() < startingTime2 + 4000 ) {
				if( ! this.initialized ) {
					instance.initLevel();
					initialized = true;
				}
				render();
				Camera.current.darken( 0.0005 * ( 4000 - System.currentTimeMillis() + startingTime2 ) );
			} else {
				exiting = true;
			}
		}
	}
}