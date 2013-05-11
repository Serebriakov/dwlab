package examples;

import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;

public class Test extends Project {
	static {
		Graphics.init();		
	}
	
	static ButtonAction keyExit = ButtonAction.create( KeyboardKey.create( Key.ESCAPE ) );
	
	public static void main(String[] argv) {
		( new Test() ).act();
	}
	
	@Override
	public void logic() {
		if ( keyExit.wasPressed() ) exiting = true;
	}
	
	@Override
	public void render() {
		//Graphics.drawRectangle( 400, 400, 200, 200 );
		Graphics.drawEmptyRectangle( 200, 200, 200, 200 );
		Graphics.drawEmptyLongOval( 400d, 400d, 200d, 100d );
	}
}
