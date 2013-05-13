package examples;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Visualizer;

public class TurnExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new TurnExample() ).act();
	}
	
	
	double turningSpeed = 0.5 * Math.PI;

	public Sprite tank = new Sprite( ShapeType.rectangle, 0, 0, 2, 2, 0, 5 );

	ButtonAction left = ButtonAction.create( KeyboardKey.create( Key.LEFT ) );
	ButtonAction right = ButtonAction.create( KeyboardKey.create( Key.RIGHT ) );
	ButtonAction up = ButtonAction.create( KeyboardKey.create( Key.UP ) );
	ButtonAction down = ButtonAction.create( KeyboardKey.create( Key.DOWN ) );

	
	@Override
	public void init() {
		tank.visualizer = new Visualizer( "res/tank.png" );
	}
	

	@Override
	public void logic() {
		if( left.isDown() ) tank.turn( -turningSpeed );
		if( right.isDown() ) tank.turn( turningSpeed );
		if( up.isDown() ) tank.moveForward();
		if( down.isDown() ) tank.moveBackward();
	}
	

	@Override
	public void render() {
		tank.draw();
		printText( "Press arrow Keys to move tank" );
		printText( "Turn, MoveForward, MoveBackward example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
