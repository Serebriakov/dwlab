package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;

public class SetCornerCoordsExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new SetCornerCoordsExample() ).act();
	}
	
	
	public Sprite rectangle = Sprite.fromShape( 0, 0, 8, 6 );

	public void init() {
		initGraphics();
	}

	public void logic() {
		rectangle.setCornerCoords( cursor.x, cursor.y );
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		rectangle.draw();
		printText( "SetCornerCoords example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
