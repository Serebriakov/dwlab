package examples;

import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.platform.LWJGL;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;

// First sprite is moving at constant speed regardles of LogicFPS because it use delta-timing PerSecond() method to determine
// on which distance to move in particular logic frame.Second sprite use simple coordinate addition.

public class LogicFPSExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new LogicFPSExample() ).act();
	}
	
	
	Sprite sprite1 = new Sprite( -10, -2, 2 );
	Sprite sprite2 = new Sprite( -10, 2, 2 );

	ButtonAction increment = ButtonAction.create( KeyboardKey.create( Key.ADD ) );
	ButtonAction decrement = ButtonAction.create( KeyboardKey.create( Key.SUBTRACT ) );

	
	@Override
	public void init() {
		sprite1.visualizer = new Visualizer( 1d, 1d, 0d );
		sprite2.visualizer = new Visualizer( 0d, 0.5d, 1d );
		logicFPS = 100;
	}

	
	@Override
	public void logic() {
		sprite1.move( 8d, 0d );
		if( sprite1.getX() > 10 ) sprite1.alterCoords( -20d, 0d );

		sprite2.alterCoords( 0.08d, 0d );
		if( sprite2.getX() > 10 ) sprite2.alterCoords( -20d, 0d );

		if( increment.isDown() ) logicFPS += perSecond( 100 );
		if( decrement.isDown() && logicFPS > 20 ) logicFPS -= perSecond( 100 );
	}
	
	
	@Override
	public void render() {
		sprite1.draw();
		sprite2.draw();
		printText( "Logic  FPS" + Service.trim( logicFPS ) + ", press num+ / num- to change" );
		printText( "L_LogicFPS example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
