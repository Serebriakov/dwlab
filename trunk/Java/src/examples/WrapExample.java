package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.*;

public class WrapExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new WrapExample() ).act();
	}
	

	@Override
	public void render() {
		Graphics.setCurrentColor( 1d, 0d, 0d );
		Graphics.drawEmptyRectangle( 50, 50, 102, 102 );
		Graphics.drawEmptyRectangle( 400, 300, 202, 102 );
		Graphics.setCurrentColor( 0d, 1d, 0d );
		Graphics.drawOval( Service.wrap( Sys.mouseX(), 100 ) - 2, Service.wrap( Sys.mouseY(), 100 ) - 2, 5, 5 );
		Graphics.drawText( "wrap(" + Sys.mouseX() + ", 100)=" + Service.wrap( Sys.mouseX(), 100 ), 0, 102 );
		Graphics.setCurrentColor( 0d, 0d, 1d );
		Graphics.drawOval( Service.wrap( Sys.mouseX(), 300, 500 ) - 2, Service.wrap( Sys.mouseY(), 250, 350 ) - 2, 5, 5 );
		Graphics.drawText( "wrap(" + Sys.mouseX() + ", 300, 500)=" + Service.wrap( Sys.mouseX(), 300, 500 ), 300, 352 );
		Graphics.setCurrentColor( 1d, 1d, 1d );
		printText( "Wrap example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
