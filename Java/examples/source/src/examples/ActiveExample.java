package examples;

import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.platform.LWJGL;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.ContourVisualizer;
import org.newdawn.slick.Font;

public class ActiveExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static ActiveExample instance;
	
	public static void main() {
		instance = new ActiveExample();
		instance.act();
	}
	
	int spritesQuantity = 50;

	public static Layer Layer = new Layer();
	public static Sprite rectangle = new Sprite( 0, 0, 30, 20 );

	
	@Override
	public void init() {
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Ball.create();
		}
		rectangle.visualizer = new ContourVisualizer( 0.1, "FF0000" );
	}

	
	private static ButtonAction reset = ButtonAction.create( KeyboardKey.create( Key.SPACE ) );
	
	@Override
	public void logic() {
		Layer.act();
		if( reset.wasPressed() ) {
			for( Shape shape : Layer.children ) {
				shape.active = true;
				shape.visible = true;
			}
		}
	}
	

	@Override
	public void render() {
		Layer.draw();
		rectangle.draw();
		printText( "Press left mouse button on circle to make it inactive, right button to make it invisible." );
		printText( "Press space to restore all back.", 1 );
		printText( "Active, BounceInside, CollisionsWisthSprite, HandleCollisionWithSprite, Visible example", Align.TO_CENTER, Align.TO_BOTTOM );
	}


	public static class Ball extends Sprite {
		public static SpriteCollisionHandler handler = new SpriteCollisionHandler(){
			ButtonAction makeInactive = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
			ButtonAction makeInvisible = ButtonAction.create( MouseButton.create( MouseButton.RIGHT_BUTTON ) );

			@Override
			public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
				if( makeInactive.wasPressed() ) sprite1.active = false;
				if( makeInvisible.wasPressed() ) sprite1.visible = false;
			}
		};

		public static Ball create() {
			Ball ball = new Ball();
			ball.setCoords( Service.random( -13, 13 ), Service.random( -8, 8 ) );
			ball.setDiameter( Service.random( 0.5, 1.5 ) );
			ball.angle = Service.random( 360 );
			ball.velocity = Service.random( 3, 7 );
			ball.shapeType = ShapeType.oval;
			ball.visualizer.setRandomColor();
			Layer.addLast( ball );
			return ball;
		}
		

		@Override
		public void act() {
			moveForward();
			bounceInside( rectangle );
			collisionsWith( cursor, handler );
		}
	}
}