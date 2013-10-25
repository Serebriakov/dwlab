package examples;

import dwlab.platform.Platform;

import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.base.service.Service;

public class WrapExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new WrapExample() ).act();
	}
	

	@Override
	public void render() {
		Platform.current.setCurrentColor( 1d, 0d, 0d );
		Platform.current.drawEmptyRectangle( 50, 50, 102, 102 );
		Platform.current.drawEmptyRectangle( 400, 300, 202, 102 );
		Platform.current.setCurrentColor( 0d, 1d, 0d );
		Platform.current.drawOval( Service.wrap( Platform.current.mouseX(), 100 ) - 2, Service.wrap( Platform.current.mouseY(), 100 ) - 2, 5, 5 );
		Platform.current.drawText( "wrap(" + Platform.current.mouseX() + ", 100)=" + Service.wrap( Platform.current.mouseX(), 100 ), 0, 102 );
		Platform.current.setCurrentColor( 0d, 0d, 1d );
		Platform.current.drawOval( Service.wrap( Platform.current.mouseX(), 300, 500 ) - 2, Service.wrap( Platform.current.mouseY(), 250, 350 ) - 2, 5, 5 );
		Platform.current.drawText( "wrap(" + Platform.current.mouseX() + ", 300, 500)=" + Service.wrap( Platform.current.mouseX(), 300, 500 ), 300, 352 );
		Platform.current.setCurrentColor( 1d, 1d, 1d );
		printText( "Wrap example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
