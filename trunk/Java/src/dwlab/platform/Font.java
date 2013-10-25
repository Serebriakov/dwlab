/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.platform;

import java.io.InputStream;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class Font {
	TrueTypeFont font;
	int textureID;
	
	
	private Font( java.awt.Font font ) {
		this.font = new TrueTypeFont( font, false );
		this.textureID = GL11.glGetInteger( GL11.GL_TEXTURE_BINDING_2D );
	}
	
	
	public static Font load( String fileName, float size ) {
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
