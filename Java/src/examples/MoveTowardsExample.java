package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Visualizer;

public class MoveTowardsExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new MoveTowardsExample() ).act();
	}
	
	
	public Sprite ball = new Sprite( ShapeType.oval, 0d, 0d, 3d, 3d, 0d, 5d );

	
	@Override
	public void init() {
		ball.visualizer = new Visualizer( "3F3F7F", 1d, true );
		cursor = new Sprite( 0d, 0d, 1d );
		cursor.visualizer = new Visualizer( "7FFF3F", 1d, true );
	}
	

	@Override
	public void logic() {
		ball.moveTowards( cursor, ball.velocity );
	}
	

	@Override
	public void render() {
		ball.draw();
		cursor.draw();
		if( ball.isAtPositionOf( cursor ) ) printText( "Ball is at position of cursor" );
		printText( "IsAtPositionOf, MoveTowards example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}