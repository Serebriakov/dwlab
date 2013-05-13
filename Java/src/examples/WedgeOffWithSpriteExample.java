package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.visualizers.DebugVisualizer;

public class WedgeOffWithSpriteExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new WedgeOffWithSpriteExample() ).act();
	}
	
	
	ButtonAction up = ButtonAction.create( KeyboardKey.create( Key.UP ) );
	ButtonAction down = ButtonAction.create( KeyboardKey.create( Key.DOWN ) );
	ButtonAction left = ButtonAction.create( KeyboardKey.create( Key.LEFT ) );
	ButtonAction right = ButtonAction.create( KeyboardKey.create( Key.RIGHT ) );
	ButtonAction switchToDebugMode = ButtonAction.create( KeyboardKey.create( Key.D ) );

	int koloboksQuantity = 50;

	public static Layer koloboks = new Layer();
	public static Image kolobokImage;
	public static Kolobok player;
	public static boolean debugMode;
	public static SpriteCollisionHandler collisionHandler = new SpriteCollisionHandler(){
		@Override
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			sprite1.wedgeOffWithSprite( sprite2, Service.powerOf2( sprite1.getWidth() ), Service.powerOf2( sprite2.getWidth() ) );
		}
	};

	
	@Override
	public void init() {
		kolobokImage = new Image( "res/kolobok.png" );
		for( int n = 1; n <= koloboksQuantity; n++ ) {
			Kolobok.createKolobok();
		}
		player = Kolobok.createPlayer();
	}

	
	@Override
	public void logic() {
		koloboks.act();

		if( left.isDown() ) player.turn( -player.turningSpeed );
		if( right.isDown() ) player.turn( player.turningSpeed );
		if( up.isDown() ) player.moveForward();
		if( down.isDown() ) player.moveBackward();

		if( switchToDebugMode.wasPressed() ) debugMode = !debugMode;
	}

	
	@Override
	public void render() {
		if( debugMode ) {
			koloboks.drawUsingVisualizer( DebugVisualizer.instance );
			showDebugInfo();
		} else {
			koloboks.draw();
			printText( "Move white kolobok with arrow Keys and push other koloboks and press D for debug mode" );
			printText( "WedgeOffWithSprite example", Align.TO_CENTER, Align.TO_BOTTOM );
		}
	}
	
					
	
	public static class Kolobok extends Sprite {
		double turningSpeed = Math.PI;

		
		public static Kolobok createPlayer() {
			Kolobok player = create();
			player.setDiameter( 2 );
			player.velocity = 7;
			return player;
		}
		

		public static Kolobok createKolobok() {
			Kolobok kolobok = create();
			kolobok.setCoords( Service.random( -15, 15 ), Service.random( -11, 11 ) );
			kolobok.setDiameter( Service.random( 1, 3 ) );
			kolobok.angle = Service.random( Math.PI * 2 );
			kolobok.visualizer.setRandomColor();
			return kolobok;
		}
		

		public static Kolobok create() {
			Kolobok kolobok = new Kolobok();
			kolobok.shapeType = ShapeType.oval;
			kolobok.visualizer.image = kolobokImage;
			kolobok.visualizer.setVisualizerScale( 1.3, 1.3 );
			koloboks.addLast( kolobok );
			return kolobok;
		}
		

		@Override
		public void act() {
			collisionsWithLayer( koloboks, collisionHandler );
		}
	}
}