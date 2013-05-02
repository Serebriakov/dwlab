package examples;
import dwlab.base.*;

public class BitmapFontExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new BitmapFontExample() ).act();
	}
	
	private BitmapFont font = new BitmapFont( "res/font.png", 32, 127, 16, false );
	
	@Override
	public void init() {
		initGraphics();
		Graphics.setClearingColor( 0d, 0.5d, 0d, 1d );
	}

	@Override
	public void render() {
		font.print( "Hello!", Service.random( -15, 15 ), Service.random( -11, 11 ), Service.random( 0.5, 2.0 ) );
		Graphics.drawText( "LTBitmapFont example", 0, 12, Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
