package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;

public class IntInLimitsExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new IntInLimitsExample() ).act();
	}
	
	
	public String word;

	public void logic() {
		if( intInLimits( mouseX(), 200, 600 ) ) word = ""; else word = "not ";
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		Graphics.setColor( 255, 0, 0 );
		drawLine( 200, 0, 200, 599 );
		drawLine( 600, 0, 600, 599 );
		Graphics.setColor( 255, 255, 255 );
		printText( mouseX() + " is " + word + "in limits of [ 200, 600 ]" );
		Graphics.drawOval( mouseX() - 2, mouseY() - 2, 5, 5 );
		printText( "L_IntInLimits example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
