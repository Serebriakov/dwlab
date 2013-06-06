package examples;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class Test  {
	public static void main(String[] argv) {
		try {
			Display.setDisplayMode( new DisplayMode( 800, 600 ) );
			Display.create();
		} catch ( LWJGLException ex ) {
			Logger.getLogger( Test.class.getName() ).log( Level.SEVERE, null, ex );
		}
		
		glShadeModel( GL_SMOOTH );
		glDisable( GL_DEPTH_TEST );
		glDisable( GL_LIGHTING ); 
		
		glEnable( GL_BLEND );
		glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		glEnable( GL_TEXTURE_2D );
		
		glMatrixMode( GL_PROJECTION ) ;
		glLoadIdentity();
		glOrtho( 0d, 800, 600, 0d, -1d, 1d );
		glMatrixMode( GL_MODELVIEW ) ;
		
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glClearDepth(1);
		
		
		ByteBuffer buffer = ByteBuffer.allocateDirect( 4 * 64 * 64 );
		for( int y = 0; y < 64; y++ ) {
			for( int x = 0; x < 64; x++ ) {
				byte color = (byte) 0x7F;
				if( ( y + x ) % 2 == 1 ) color = 0;
				int pos = 4 * ( x + y * 64 );
				buffer.put( pos, color );
				buffer.put( pos + 1, color );
				buffer.put( pos + 2, color );
				buffer.put( pos + 3, (byte) 0x7F );
			}
		}
		
		
		int textureID = glGenTextures();
		glBindTexture( GL_TEXTURE_2D, textureID );
		glTexImage2D( GL_TEXTURE_2D,  0,  GL_RGBA,  64, 64, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer );
		
		
		glBegin( GL_QUADS );
		
		glTexCoord2d( 0d, 0d );
		glVertex2d( 200, 100 );
		glTexCoord2d( 1d, 0d );
		glVertex2d( 600, 100 );
		glTexCoord2d( 1d, 1d );
		glVertex2d( 600, 500 );
		glTexCoord2d( 0d, 1d );
		glVertex2d( 200, 500 );
		
		glEnd();
		
		Display.update();
		
		while( !Display.isCloseRequested() ) Display.processMessages();
	}
}