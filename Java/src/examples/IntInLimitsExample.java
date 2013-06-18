package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;

public class IntInLimitsExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new IntInLimitsExample() ).act();
	}
	
	
	String word;

	
	@Override
	public void logic() {
		if( Service.inLimits( Sys.mouseX(), 200, 600 ) ) word = ""; else word = "not ";
	}
	

	@Override
	public void render() {
		Graphics.setCurrentColor( 1d, 0d, 0d );
		Graphics.drawLine( 200, 0, 200, 599 );
		Graphics.drawLine( 600, 0, 600, 599 );
		Graphics.setCurrentColor( 1d, 1d, 1d );
		printText( Sys.mouseX() + " is " + word + "in limits of [ 200, 600 ]" );
		Graphics.drawOval( Sys.mouseX(), Sys.mouseY(), 5, 5 );
		printText( "L_IntInLimits example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
