package examples;

import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.platform.LWJGL;
import static dwlab.platform.Functions.*;

public class DistanceExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new DistanceExample() ).act();
	}
	
	ButtonAction put = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	int x = 400;
	int y = 300;

	@Override
	public void logic() {
		if( put.wasPressed() ) {
			x = mouseX();
			y = mouseY();
		}
	}

	@Override
	public void render() {
		drawOval( x - 2, y - 2, 5, 5 );
		drawLine( x, y, mouseX(), mouseY() );
		printText( "Distance is " + Service.trim( Service.distance( y - mouseY(), x - mouseX() ) ) + " pixels" );
		printText( "L_Distance example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
