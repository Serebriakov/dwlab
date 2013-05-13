package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;

public class LimitIntExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new LimitIntExample() ).act();
	}

	
	@Override
	public void render() {
		Graphics.setColor( 255, 0, 0 );
		Graphics.drawLine( 200, 0, 200, 599 );
		Graphics.drawLine( 600, 0, 600, 599 );
		Graphics.setColor( 255, 255, 255 );
		int x = Service.limit( Sys.mouseX(), 200, 600 );
		Graphics.drawOval( x - 2, Sys.mouseY() - 2, 5, 5 );
		printText( "LimitInt(Sys.mouseX(),200,600) = " + x );
		printText( "L_LimitInt example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
