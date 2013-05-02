package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.Sprite.ShapeType;
import org.lwjgl.input.Keyboard;

public class ButtonActionExample extends Project {
	static {
		Graphics.init();
	}
	
	private static ButtonActionExample instance = new ButtonActionExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	
	public static double velocity = 5.0;

	private static final ButtonAction moveLeft = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_LEFT ), "move left" );
	private static final ButtonAction moveRight = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_RIGHT ), "move right" );
	private static final ButtonAction moveUp = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_UP ), "move up" );
	private static final ButtonAction moveDown = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_DOWN ), "move down" );
	private static final ButtonAction fire = ButtonAction.create( MouseButton.create( 0 ), "fire" );
	private static final ButtonAction defineKeys = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_D ), "define keys", "ctrl" );
	private static final ButtonAction[] actions = { moveLeft, moveRight, moveUp, moveDown, fire };

	public Sprite player = new Sprite( ShapeType.OVAL, 0, 0, 1, 1 );
	

	@Override
	public void init() {
		initGraphics();
		player.visualizer.set( "7FBFFF" );
	}
	

	@Override
	public void logic() {
		if( moveLeft.isDown() ) player.move( -velocity, 0 );
		if( moveRight.isDown() ) player.move( velocity, 0 );
		if( moveUp.isDown() ) player.move( 0, -velocity );
		if( moveDown.isDown() ) player.move( 0, velocity );
		if( fire.isDown() ) Bullet.create();

		Bullet.bullets.act();

		if( defineKeys.wasPressed() ) switchTo( new DefineKeys() );
	}

	
	@Override
	public void render() {
		Bullet.bullets.draw();
		player.draw();
		Graphics.drawText( "Press Ctrl-D to define keys", 0, 0 );
		Graphics.drawText( "LTButtonAction, SwitchTo, Move example", 0, 12, Align.TO_CENTER, Align.TO_BOTTOM );
	}
	


	public static class Bullet extends Sprite {
		public static double bulletVelocity = 10.0;
		public static Layer bullets = new Layer();
		
		
		public static Bullet create() {
			Shape player = ButtonActionExample.instance.player;
			Bullet bullet = new Bullet();
			bullet.jumpTo( player );
			bullet.setDiameter( 0.25 );
			bullet.shapeType = ShapeType.OVAL;
			bullet.angle = player.directionTo( cursor );
			bullet.velocity = bulletVelocity;
			bullet.visualizer.set( "7FFFBF" );
			bullets.addLast( bullet );
			return bullet;
		}
		

		@Override
		public void act() {
			moveForward();
		}
	}



	public class DefineKeys extends Project {
		public int actionNum = 0;
		public int z;
		

		@Override
		public void logic() {
			if( ButtonActionExample.actions[ actionNum ].define() ) {
				actionNum += 1;
				if( actionNum == ButtonActionExample.actions.length ) exiting = true;
			}
		}
		

		@Override
		public void render() {
			ButtonActionExample.instance.render();
			Graphics.drawText( "Press key for " + ButtonActionExample.actions[ actionNum ].name, 0, 16 );
		}
	}
}