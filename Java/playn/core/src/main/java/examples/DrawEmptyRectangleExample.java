package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.base.service.Service;
import dwlab.platform.LWJGL;

public class DrawEmptyRectangleExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		flipping = false;
		( new DrawEmptyRectangleExample() ).act();
		flipping = true;
	}
	
	
	@Override
	public void render() {
		for( int n = 1; n <= 10; n++ ) {
			double width = Service.random( 700 );
			double height = Service.random( 500 );
			Platform.setCurrentColor( Service.random( 0.5d, 1d ), Service.random( 0.5d, 1d ), Service.random( 0.5d, 1d ) );
			Platform.current.drawEmptyRectangle( Service.random( 0.5 * width, 800 - 0.5 * width ), Service.random( 0.5 * height, 600 - 0.5 * height ), width, height );
		}
		Platform.setCurrentColor( 0d, 0d, 0d, 0.04d );
		Platform.current.drawRectangle( 400, 300, 800, 600 );
		Platform.resetCurrentColor();
		printText( "Platform.current.drawEmptyRectangle example", Align.TO_CENTER, Align.TO_BOTTOM );
		Platform.current.swapBuffers();
	}
}