package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;

public class IntInLimitsExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new IntInLimitsExample() ).act();
	}
	
	
	String word;

	
	@Override
	public void logic() {
		if( Service.inLimits( Platform.current.mouseX(), 200, 600 ) ) word = ""; else word = "not ";
	}
	

	@Override
	public void render() {
		Platform.current.setCurrentColor( 1d, 0d, 0d );
		Platform.current.drawLine( 200, 0, 200, 599 );
		Platform.current.drawLine( 600, 0, 600, 599 );
		Platform.current.setCurrentColor( 1d, 1d, 1d );
		printText( Platform.current.mouseX() + " is " + word + "in limits of [ 200, 600 ]" );
		Platform.current.drawOval( Platform.current.mouseX(), Platform.current.mouseY(), 5, 5 );
		printText( "L_IntInLimits example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
