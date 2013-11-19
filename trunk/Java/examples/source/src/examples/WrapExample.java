package examples;

import static dwlab.platform.Functions.*;
import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.platform.LWJGL;

public class WrapExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new WrapExample() ).act();
	}
	

	@Override
	public void render() {
		Platform.setCurrentColor( 1d, 0d, 0d );
		drawEmptyRectangle( 50, 50, 102, 102 );
		drawEmptyRectangle( 400, 300, 202, 102 );
		Platform.setCurrentColor( 0d, 1d, 0d );
		drawOval( Service.wrap( mouseX(), 100 ) - 2, Service.wrap( mouseY(), 100 ) - 2, 5, 5 );
		drawText( "wrap(" + mouseX() + ", 100)=" + Service.wrap( mouseX(), 100 ), 0, 102 );
		Platform.setCurrentColor( 0d, 0d, 1d );
		drawOval( Service.wrap( mouseX(), 300, 500 ) - 2, Service.wrap( mouseY(), 250, 350 ) - 2, 5, 5 );
		drawText( "wrap(" + mouseX() + ", 300, 500)=" + Service.wrap( mouseX(), 300, 500 ), 300, 352 );
		Platform.setCurrentColor( 1d, 1d, 1d );
		printText( "Wrap example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
