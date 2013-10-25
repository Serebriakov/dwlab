package examples;

import dwlab.platform.Platform;

import dwlab.base.Project;
import dwlab.base.images.BitmapFont;
import dwlab.base.service.Align;
import dwlab.base.service.Service;

public class BitmapFontExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		Platform.current.setClearingColor( 0d, 0.5d, 0d, 1d );
		( new BitmapFontExample() ).act();
		Platform.current.setClearingColor( 0d, 0d, 0d, 1d );
	}
	
	
	private BitmapFont font = new BitmapFont( "res/font.png", 32, 127, 16, true );
	
	
	@Override
	public void render() {
		font.print( "Hello!", Service.random( -15d, 15d ), Service.random( -11d, 11d ), Service.random( 0.5d, 2d ) );
		printText( "LTBitmapFont example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
