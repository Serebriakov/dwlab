package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.images.BitmapFont;
import dwlab.base.*;

public class BitmapFontExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		Graphics.setClearingColor( 0d, 0.5d, 0d, 1d );
		( new BitmapFontExample() ).act();
		Graphics.setClearingColor( 0d, 0d, 0d, 1d );
	}
	
	
	private BitmapFont font = new BitmapFont( "res/font.png", 32, 127, 16, false );
	
	
	@Override
	public void render() {
		font.print( "Hello!", Service.random( -15d, 15d ), Service.random( -11d, 11d ), Service.random( 0.5d, 2d ) );
		printText( "LTBitmapFont example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
