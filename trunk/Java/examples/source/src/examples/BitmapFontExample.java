package examples;

import static dwlab.platform.Functions.*;

import dwlab.base.Project;
import dwlab.base.images.Font;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.platform.LWJGL;

public class BitmapFontExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		Platform.setClearingColor( 0d, 0.5d, 0d, 1d );
		( new BitmapFontExample() ).act();
		Platform.setClearingColor( 0d, 0d, 0d, 1d );
	}
	
	
	private final Font font = Platform.currentFont;
	
	
	@Override
	public void render() {
		font.print( "Hello!", Service.random( -15d, 15d ), Service.random( -11d, 11d ), Service.random( 0.5d, 2d ) );
		printText( "LTBitmapFont example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
