package dwlab.base;

import java.io.InputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class Font {
	TrueTypeFont font;
	
	
	private Font( java.awt.Font font ) {
		this.font = new TrueTypeFont( font, false );
	}
	
	
	static Font load( String fileName, float size ) {
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream( fileName );
			java.awt.Font font = java.awt.Font.createFont( java.awt.Font.TRUETYPE_FONT, inputStream );
			font = font.deriveFont( size ); // set font size
			return new Font( font );
		} catch (Exception e) {
		}
		return null;
	}
}
