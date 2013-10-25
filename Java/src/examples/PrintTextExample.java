package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;

public class PrintTextExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new PrintTextExample() ).act();
	}
	
	
	public Sprite rectangle = new Sprite( 0, 0, 16, 12 );


	@Override
	public void logic() {
		rectangle.setMouseCoords();
	}
	

	@Override
	public void render() {
		rectangle.drawContour( 2 );

		rectangle.print( "topleft corner", Align.TO_LEFT, Align.TO_TOP );
		rectangle.print( "top", Align.TO_CENTER, Align.TO_TOP );
		rectangle.print( "topright corner", Align.TO_RIGHT, Align.TO_TOP );
		rectangle.print( "left side", Align.TO_LEFT, Align.TO_CENTER );
		rectangle.print( "center", Align.TO_CENTER, Align.TO_CENTER );
		rectangle.print( "right side", Align.TO_RIGHT, Align.TO_CENTER );
		rectangle.print( "bottomleft corner", Align.TO_LEFT, Align.TO_BOTTOM );
		rectangle.print( "bottom", Align.TO_CENTER, Align.TO_BOTTOM );
		rectangle.print( "bottomright corner", Align.TO_RIGHT, Align.TO_BOTTOM );
		printText( "PrintText example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
