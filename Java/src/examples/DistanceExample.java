package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;

public class DistanceExample extends Project {
	static {
		Platform.current.init();
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
			x = Platform.current.mouseX();
			y = Platform.current.mouseY();
		}
	}

	@Override
	public void render() {
		Platform.current.drawOval( x - 2, y - 2, 5, 5 );
		Platform.current.drawLine( x, y, Platform.current.mouseX(), Platform.current.mouseY() );
		printText( "Distance is " + Service.trim( Service.distance( y - Platform.current.mouseY(), x - Platform.current.mouseX() ) ) + " pixels" );
		printText( "L_Distance example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
