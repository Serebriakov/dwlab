package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;

public class PrintTextExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new PrintTextExample() ).act();
	}
	
	
	public Sprite rectangle = Sprite.fromShape( 0, 0, 16, 12 );

	public void init() {
		initGraphics();
	}

	public void logic() {
		rectangle.setMouseCoords();
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public final double textSize = 0.7;

	public void render() {
		rectangle.drawContour( 2 );

		rectangle.printText( "topleft corner", textSize, Align.toLeft, Align.toTop );
		rectangle.printText( "top", textSize, Align.TO_CENTER, Align.toTop );
		rectangle.printText( "topright corner", textSize, Align.toRight, Align.toTop );
		rectangle.printText( "left side", textSize, Align.toLeft, Align.TO_CENTER );
		rectangle.printText( "center", textSize, Align.TO_CENTER, Align.TO_CENTER );
		rectangle.printText( "right side", textSize, Align.toRight, Align.TO_CENTER );
		rectangle.printText( "bottomleft corner", textSize, Align.toLeft, Align.TO_BOTTOM );
		rectangle.printText( "bottom", textSize, Align.TO_CENTER, Align.TO_BOTTOM );
		rectangle.printText( "bottomright corner", textSize, Align.toRight, Align.TO_BOTTOM );
		printText( "PrintText example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
