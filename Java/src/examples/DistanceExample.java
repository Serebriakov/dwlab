package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;

public class DistanceExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DistanceExample() ).act();
	}
	
	
	int x = 400;
	int y = 300;

	@Override
	public void logic() {
		if( mouseHit( 1 ) ) {
			x = mouseX();
			y = mouseY();
		}
	}

	@Override
	public void render() {
		Graphics.drawOval( x - 2, y - 2, 5, 5 );
		drawLine( x, y, mouseX(), mouseY() );
		printText( "Distance is " + Service.trim( distance( y - mouseY(), x - mouseX() ) ) + " pixels" );
		printText( "L_Distance example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
