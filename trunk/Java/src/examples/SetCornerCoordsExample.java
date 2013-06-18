package examples;

import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.shapes.sprites.Sprite;

public class SetCornerCoordsExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new SetCornerCoordsExample() ).act();
	}
	
	
	Sprite rectangle = new Sprite( 0, 0, 8, 6 );

	
	@Override
	public void logic() {
		rectangle.setCornerCoords( cursor.getX(), cursor.getY() );
	}
	

	@Override
	public void render() {
		rectangle.draw();
		printText( "SetCornerCoords example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
