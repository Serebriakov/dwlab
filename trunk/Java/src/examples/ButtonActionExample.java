package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class ButtonActionExample extends Project {
	static {
		Graphics.init();
	}
	
	private static ButtonActionExample instance = new ButtonActionExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	
	public static double velocity = 5.0;

	private static final ButtonAction moveLeft = ButtonAction.create( KeyboardKey.create( Key.LEFT ), "move left" );
	private static final ButtonAction moveRight = ButtonAction.create( KeyboardKey.create( Key.RIGHT ), "move right" );
	private static final ButtonAction moveUp = ButtonAction.create( KeyboardKey.create( Key.UP ), "move up" );
	private static final ButtonAction moveDown = ButtonAction.create( KeyboardKey.create( Key.DOWN ), "move down" );
	private static final ButtonAction fire = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ), "fire" );
	private static final ButtonAction defineKeys = ButtonAction.create( KeyboardKey.create( Key.D ), "define Keys", "ctrl" );
	private static final ButtonAction[] actions = { moveLeft, moveRight, moveUp, moveDown, fire };

	public Sprite pLayer = new Sprite( ShapeType.oval, 0, 0, 1, 1 );
	

	@Override
	public void init() {
		pLayer.visualizer.set( "7FBFFF" );
	}
	

	@Override
	public void logic() {
		if( moveLeft.isDown() ) pLayer.move( -velocity, 0 );
		if( moveRight.isDown() ) pLayer.move( velocity, 0 );
		if( moveUp.isDown() ) pLayer.move( 0, -velocity );
		if( moveDown.isDown() ) pLayer.move( 0, velocity );
		if( fire.isDown() ) Bullet.create();

		Bullet.bullets.act();

		if( defineKeys.wasPressed() ) {
			switchTo( new DefineKeys() );
		}
	}

	
	@Override
	public void render() {
		Bullet.bullets.draw();
		pLayer.draw();
		printText( "Press Ctrl-D to define Keys" );
		printText( "LTButtonAction, SwitchTo, Move example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	


	public static class Bullet extends Sprite {
		public static double bulletvelocity = 10.0;
		public static Layer bullets = new Layer();
		
		
		public static Bullet create() {
			Shape pLayer = ButtonActionExample.instance.pLayer;
			Bullet bullet = new Bullet();
			bullet.jumpTo( pLayer );
			bullet.setDiameter( 0.25 );
			bullet.shapeType = ShapeType.oval;
			bullet.angle = pLayer.directionTo( cursor );
			bullet.velocity = bulletvelocity;
			bullet.visualizer.set( "7FFFBF" );
			bullets.addLast( bullet );
			return bullet;
		}
		

		@Override
		public void act() {
			moveForward();
		}
	}



	public static class DefineKeys extends Project {
		public int actionNum = 0;
		public int z;
		
		
		@Override
		public void init() {
			for( ButtonAction controller : ButtonActionExample.actions ) controller.clear();
		}
		

		@Override
		public void logic() {
			if( ButtonActionExample.actions[ actionNum ].define() ) {
				actionNum += 1;
				if( actionNum == ButtonActionExample.actions.length ) exiting = true;
			}
		}
		
		
		@Override
		public void processEvents() {
		}
		

		@Override
		public void render() {
			ButtonActionExample.instance.render();
			printText( "Press Key for " + ButtonActionExample.actions[ actionNum ].name, 1 );
		}
	}
}