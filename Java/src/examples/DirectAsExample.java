package examples;

import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.Sprite.ShapeType;
import dwlab.shapes.sprites.SpriteCollisionHandler;

public class DirectAsExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DirectAsExample() ).act();
	}
	

	int spritesQuantity = 50;

	Layer layer = new Layer();
	Cursor shapeCursor = new Cursor();
	Image spriteImage = new Image( "res/kolobok.png" );
	Sprite selected;

	
	@Override
	public void init() {
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( ShapeType.OVAL, Service.random( -15d, 15d ), Service.random( -11d, 11d ), 1d, 1d, Service.random( 360 ), 0d );
			sprite.setDiameter( Service.random( 0.7, 2 ) );
			sprite.visualizer.setRandomColor();
			sprite.visualizer.setVisualizerScales( 1.3 );
			sprite.visualizer.image = spriteImage;
			layer.addLast( sprite );
		}

		shapeCursor.setDiameter( 1d );
		shapeCursor.visualizer.image = spriteImage;
		shapeCursor.visualizer.setVisualizerScales( 1.3 );
		shapeCursor.shapeType = ShapeType.OVAL;
		shapeCursor.angle = Math.PI;
	}
	

	@Override
	public void logic() {
		shapeCursor.act();
	}

	
	@Override
	public void render() {
		layer.draw();
		shapeCursor.draw();
		printText( "Click left mouse button on sprite to direct cursor sprite as it" );
		printText( "and right button to set size equal to sprite's", 1 );
		printText( "DirectAs, SetSizeAs example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	
	
	
	public class Cursor extends Sprite {
		ButtonAction setDirection = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ), "set direction" );
		ButtonAction setSize = ButtonAction.create( MouseButton.create( MouseButton.RIGHT_BUTTON ), "set size" );
		
		public SpriteCollisionHandler sizeHandler = new SpriteCollisionHandler(){
			@Override
			public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
				sprite1.setSizeAs( sprite2 );
			}
		};

		public SpriteCollisionHandler directionHandler = new SpriteCollisionHandler(){
			@Override
			public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
				sprite1.directAs( sprite2 );
			}
		};


		@Override
		public void act() {
			setMouseCoords();
			if( setDirection.wasPressed() ) collisionsWithLayer( layer, directionHandler );
			if( setSize.wasPressed() ) collisionsWithLayer( layer, sizeHandler );
		}
	}
}



