package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;

public class LimitIntExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new LimitIntExample() ).act();
	}
	
	
	public void init() {
		initGraphics();
	}

	public void logic() {
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		Graphics.setColor( 255, 0, 0 );
		drawLine( 200, 0, 200, 599 );
		drawLine( 600, 0, 600, 599 );
		Graphics.setColor( 255, 255, 255 );
		int x = limitInt( mouseX(), 200, 600 );
		Graphics.drawOval( x - 2, mouseY() - 2, 5, 5 );
		printText( "LimitInt(MouseX(),200,600) = " + x );
		printText( "L_LimitInt example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
