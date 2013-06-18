package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;

public class DistanceExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new DistanceExample() ).act();
	}
	
	ButtonAction put = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	int x = 400;
	int y = 300;

	@Override
	public void logic() {
		if( put.wasPressed() ) {
			x = Sys.mouseX();
			y = Sys.mouseY();
		}
	}

	@Override
	public void render() {
		Graphics.drawOval( x - 2, y - 2, 5, 5 );
		Graphics.drawLine( x, y, Sys.mouseX(), Sys.mouseY() );
		printText( "Distance is " + Service.trim( Service.distance( y - Sys.mouseY(), x - Sys.mouseX() ) ) + " pixels" );
		printText( "L_Distance example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
