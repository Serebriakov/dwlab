package examples;

import dwlab.platform.Platform;
import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.base.service.Service;

public class LimitIntExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new LimitIntExample() ).act();
	}

	
	@Override
	public void render() {
		Platform.current.setCurrentColor( 1d, 0d, 0d );
		Platform.current.drawLine( 200, 0, 200, 599 );
		Platform.current.drawLine( 600, 0, 600, 599 );
		Platform.current.setCurrentColor( 1d, 1d, 1d );
		int x = Service.limit( Platform.current.mouseX(), 200, 600 );
		Platform.current.drawOval( x, Platform.current.mouseY(), 5, 5 );
		printText( "LimitInt(Platform.current.mouseX(),200,600) = " + x );
		printText( "L_LimitInt example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
