package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;

public class TurnExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new TurnExample() ).act();
	}
	
	
	public final double turningSpeed = 90;

	public Sprite tank = Sprite.fromShape( 0, 0, 2, 2, Sprite.rectangle, 0, 5 );

	public void init() {
		initGraphics();
		tank.visualizer = Visualizer.fromFile( " incbintank .png" );
	}

	public void logic() {
		if( keyDown( key_Left ) ) tank.turn( -turningSpeed );
		if( keyDown( key_Right ) ) tank.turn( turningSpeed );
		if( keyDown( key_Up ) ) tank.moveForward();
		if( keyDown( key_Down ) ) tank.moveBackward();
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		tank.draw();
		printText( "Press arrow keys to move tank" );
		printText( "Turn, MoveForward, MoveBackward example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
