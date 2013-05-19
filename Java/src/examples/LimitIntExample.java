package examples;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Sys;
import dwlab.base.service.Align;
import dwlab.base.service.Service;

public class LimitIntExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new LimitIntExample() ).act();
	}

	
	@Override
	public void render() {
		Graphics.setCurrentColor( 1d, 0d, 0d );
		Graphics.drawLine( 200, 0, 200, 599 );
		Graphics.drawLine( 600, 0, 600, 599 );
		Graphics.setCurrentColor( 1d, 1d, 1d );
		int x = Service.limit( Sys.mouseX(), 200, 600 );
		Graphics.drawOval( x, Sys.mouseY(), 5, 5 );
		printText( "LimitInt(Sys.mouseX(),200,600) = " + x );
		printText( "L_LimitInt example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
