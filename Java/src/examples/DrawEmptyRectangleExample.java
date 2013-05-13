package examples;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.service.Service;

public class DrawEmptyRectangleExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		flipping = false;
		( new DrawEmptyRectangleExample() ).act();
	}
	
	
	@Override
	public void render() {
		for( int n = 1; n <= 10; n++ ) {
			double width = Service.random( 700 );
			double height = Service.random( 500 );
			Graphics.setColor( Service.random( 0.5d, 1d ), Service.random( 0.5d, 1d ), Service.random( 0.5d, 1d ) );
			Graphics.drawEmptyRectangle( Service.random( 0.5 * width, 800 - 0.5 * width ), Service.random( 0.5 * height, 600 - 0.5 * height ), width, height );
		}
		Graphics.setColor( 0d, 0d, 0d, 0.04d );
		Graphics.drawRectangle( 400, 300, 800, 600 );
		Graphics.resetColor();
		printText( "Graphics.drawEmptyRectangle example", Align.TO_CENTER, Align.TO_BOTTOM );
		Graphics.swapBuffers();
	}
}