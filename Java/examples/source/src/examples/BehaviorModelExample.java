package examples;

import static dwlab.platform.Functions.*;

import dwlab.base.Project;
import static dwlab.base.Project.exitButton;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.platform.LWJGL;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.*;
import dwlab.visualizers.Color;
import dwlab.visualizers.MarchingAnts;
import examples.AwPossum.*;
import examples.Jelly.*;

public class BehaviorModelExample extends Project {
	static BehaviorModelExample instance;
	
	public static void main(String[] argv) {
		Classes.register();
		LWJGL.init();
		main();
	}
	
	public static void main() {
		instance = new BehaviorModelExample();
		instance.act();
	}
	
	public int bricks = 1;
	public double deathPeriod = 1.0;

	public World world;
	public Layer layer;
	public TileMap tileMap;
	public Sprite selectedSprite;
	public MarchingAnts marchingAnts = new MarchingAnts();

	public int score;
	
	
	@Override
	public void init() {
	 	world = World.fromFile( "res/jellys.lw" );

		//LWJGL.init();

		Sprite sprite = new Sprite( Camera.current );
		sprite.visualizer.image = new Image( "res/scheme2.png" );
		while ( !exitButton.wasPressed() ) {
			sprite.draw();
			processEvents( this );
			swapBuffers();
		}

		sprite.visualizer.image = new Image( "res/scheme1.png" );
		processEvents();
		while ( !exitButton.wasPressed() ) {
			sprite.draw();
			processEvents( this );
			swapBuffers();
		}

		processEvents( this );
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
		printText( "Score: " + Service.firstZeroes( score, 6 ), Align.TO_RIGHT, Align.TO_BOTTOM );
		printText( "LTBehaviorModel example", Align.TO_CENTER, Align.TO_BOTTOM );
	}


	public static class Score extends Sprite {
		static double speed = 2d;
		static double period = 3d;

		int amount;
		double startingTime;

		public static void create( Sprite sprite, int amount ) {
			Score scoreObject = new Score();
			scoreObject.setCoords( sprite.getX(), sprite.topY() ).setDiameter( 0d );
			scoreObject.amount = amount;
			scoreObject.startingTime = BehaviorModelExample.instance.time;
			scoreObject.insertTo( instance.layer );
			instance.score += amount;
		}

		@Override
		public void act() {
			move( 0, -speed );
			if( BehaviorModelExample.instance.time > startingTime + period ) removeFrom( instance.layer );
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
			if( System.currentTimeMillis() < startingTime2 + 2000d ) {
				super.render();
				Camera.current.darken( 0.0005d * ( System.currentTimeMillis() - startingTime2 ) );
			} else if( System.currentTimeMillis() < startingTime2 + 4000d ) {
				if( !this.initialized ) {
					instance.initLevel();
					initialized = true;
				}
				super.render();
				Camera.current.darken( 0.0005d * ( 4000d - System.currentTimeMillis() + startingTime2 ) );
			} else {
				exiting = true;
			}
		}
	}
}