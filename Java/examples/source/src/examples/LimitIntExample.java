package examples;

import static dwlab.platform.Functions.*;
import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.platform.LWJGL;

public class LimitIntExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new LimitIntExample() ).act();
	}

	
	@Override
	public void render() {
		Platform.setCurrentColor( 1d, 0d, 0d );
		drawLine( 200, 0, 200, 599 );
		drawLine( 600, 0, 600, 599 );
		Platform.setCurrentColor( 1d, 1d, 1d );
		int x = Service.limit( mouseX(), 200, 600 );
		drawOval( x, mouseY(), 5, 5 );
		printText( "LimitInt(mouseX(),200,600) = " + x );
		printText( "L_LimitInt example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
