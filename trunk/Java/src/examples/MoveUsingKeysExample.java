package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Visualizer;

public class MoveUsingKeysExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new MoveUsingKeysExample() ).act();
	}
	
	
	Sprite ball1 = new Sprite( ShapeType.oval, -8d, 0d, 1d, 1d, 0d, 7d );
	Sprite ball2 = new Sprite( ShapeType.oval, 0d, 0d, 2d, 2d, 0d, 3d );
	Sprite ball3 = new Sprite( ShapeType.oval, 8d, 0d, 1.5d, 1.5d, 0d, 5d );
	
	ButtonAction[] keys = { ButtonAction.create( KeyboardKey.create( Key.I ) ),
		ButtonAction.create( KeyboardKey.create( Key.K ) ),
		ButtonAction.create( KeyboardKey.create( Key.J ) ),
		ButtonAction.create( KeyboardKey.create( Key.L ) )
	};
	

	@Override
	public void init() {
		ball1.visualizer = new Visualizer( "FF0000", 1d, true );
		ball2.visualizer = new Visualizer( "00FF00", 1d, true );
		ball3.visualizer = new Visualizer( "0000FF", 1d, true );
	}
	

	@Override
	public void logic() {
		ball1.moveUsingWSAD( ball1.velocity );
		ball2.moveUsingArrows( ball2.velocity );
		ball3.moveUsingKeys( keys, ball3.velocity );
	}
	

	@Override
	public void render() {
		printText( "Move red ball using WSAD Keys" );
		printText( "Move green ball using arrow Keys", 1 );
		printText( "Move blue ball using IJKL Keys", 2 );
		ball1.draw();
		ball2.draw();
		ball3.draw();
		printText( "MoveUsingKeys example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
