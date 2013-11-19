package dwlab.platform;

import dwlab.base.Project;
import dwlab.base.XMLObject;
import dwlab.base.images.ImageBuffer;
import dwlab.controllers.Button;
import dwlab.controllers.ButtonAction;
import dwlab.visualizers.Color;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.Surface;
import static playn.core.PlayN.*;
import static dwlab.platform.Functions.*;
import playn.core.Pointer;

public class PlayN extends Platform implements Keyboard.Listener, Mouse.Listener, Pointer.Listener {
	Surface surface;
	
	@Override
	public void drawLine( double x1, double y1, double x2, double y2, double width, Color color ) {
		surface.drawLine( (float) x1, (float) y1, (float) x2, (float) y2, (float) width );
	}
	
	
	@Override
	public void drawRectangle( double x, double y, double width, double height, double angle, Color color, boolean empty ){
		if( empty ) {
			width *= 0.5d ;
			height *= 0.5d ;
			startPolygon( 4, color, empty );
			addPolygonVertex( x - width, y - height );
			addPolygonVertex( x + width, y - height );
			addPolygonVertex( x + width, y + height );
			addPolygonVertex( x - width, y + height );
			drawPolygon();
		} else {
			surface.fillRect( (float) ( x - 0.5d * width ), (float) ( y - 0.5d * height ), (float) width, (float) height );
		}
	}
	

	private static int currentVertexNum;
	private static float[] xys;
	private static boolean emptyPolygon;
	
	@Override
	public void startPolygon( int vertexQuantity, Color color, boolean empty ) {
		currentVertexNum = -1;
		emptyPolygon = empty;
		xys = new float[ 2 * vertexQuantity ];
	}

	@Override
	public void addPolygonVertex( double x, double y ) {
		currentVertexNum ++;
		xys[ 2 * currentVertexNum ] = (float) x;
		xys[ 2 * currentVertexNum + 1 ] = (float) y;
	}

	@Override
	public void drawPolygon() {
		int vertexQuantity = xys.length / 2;
		if( emptyPolygon ) {
			for( int n1 = 0; n1 < vertexQuantity; n1++ ) {
				int n2 = ( n1 + 1 ) % vertexQuantity;
				surface.drawLine( xys[ n1 * 2 ], xys[ n1 * 2 + 1 ], xys[ n2 * 2 ], xys[ n2 * 2 + 1 ], (float) lineWidth );
			}
		} else {
			int[] indices = new int[ vertexQuantity * 3 ];
			for( int n = 0; n < vertexQuantity - 1; n++ ) {
				indices[ n * 3 ] = n;
				indices[ n * 3 + 1 ] = ( n + 1 ) % ( vertexQuantity - 1 );
				indices[ n * 3 + 2 ] = vertexQuantity - 1;
			}
			surface.fillTriangles( xys, indices );
		}
	}
	
	
	@Override
	public void clearScreen( Color color ) {
		surface.clear();
	}
	

	@Override
	public void setViewport( int vX, int vY, int vWidth, int vHeight ) {
		viewportX = vX;
		viewportY = vY;
		viewportWidth = vWidth;
		viewportHeight = vHeight;
	}

	@Override
	public Button createButton(Button.Name name) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void processEvents() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	

	public class KeyboardKey extends Button<KeyboardKey> {
		public Button.Name keyID;

		@Override
		public void xMLIO( XMLObject xMLObject ) {
			super.xMLIO( xMLObject );
			keyID = xMLObject.manageEnumAttribute( "id", keyID );
		}

		@Override
		public int compareTo( KeyboardKey key ) {
			return key.keyID == keyID ? 0 : 1;
		}
	}
	
	
	public Pushable createButton( Key keyID ) {
		switch( keyID ) {
			case LE
		}
		KeyboardKey key = new KeyboardKey();
		key.keyID = keyID;
		
		for( ButtonAction action: controllers ) {
			for( Pushable pushable: action.buttonList ) {
				if( pushable.equals( key ) ) return pushable;
			}
		}
		
		if( Platform.current != null ) key.init();

		return key;
	}
	
	
	
	@Override
	public void swapBuffers() {
		
	}	
	
	
	@Override
	public int getChar() {
		if( keys.isEmpty() ) return 0;
		int code = keys.getFirst();
		keys.removeFirst();
		return code;
	}
	
	
	int mouseX, mouseY;
	
	@Override
	public int mouseX() {
		return mouseX;
	}	
	
	@Override
	public int mouseY() {
		return mouseY;
	}
	
	
	@Override
	public void processEvents( Project project ) {
		
		;
	}
	

	@Override
	public void flushControllers() {
		boolean exiting = false;
		while( !exiting ) {
			exiting = true;
			
			processEvents( null );
			
			for( ButtonAction controller: controllers ) {
				if( controller.isDown() ) exiting = false;
			}
		}
	}
	

	@Override
	public boolean getPushable( ButtonAction action ) {
		
		return false;
	}
	

	@SuppressWarnings( "empty-statement" )
	@Override
	public void waitForKey() {
		ButtonAction action = new ButtonAction();
		while( !getPushable( action ) );
	}
	

	private final HashMap<Key, playn.core.Key> keymap = new HashMap<Key, playn.core.Key>();
	
	public void initKeys() {
		keymap.put( Key.ESCAPE, playn.core.Key.ESCAPE );
		keymap.put( Key._1, playn.core.Key.K1 );
		keymap.put( Key._2, playn.core.Key.K2 );
		keymap.put( Key._3, playn.core.Key.K3 );
		keymap.put( Key._4, playn.core.Key.K4 );
		keymap.put( Key._5, playn.core.Key.K5 );
		keymap.put( Key._6, playn.core.Key.K6 );
		keymap.put( Key._7, playn.core.Key.K7 );
		keymap.put( Key._8, playn.core.Key.K8 );
		keymap.put( Key._9, playn.core.Key.K9 );
		keymap.put( Key._0, playn.core.Key.K0 );
		keymap.put( Key.MINUS, playn.core.Key.MINUS );
		keymap.put( Key.EQUALS, playn.core.Key.EQUALS );
		keymap.put( Key.BACK, playn.core.Key.BACK );
		keymap.put( Key.TAB, playn.core.Key.TAB );
		keymap.put( Key.Q, playn.core.Key.Q );
		keymap.put( Key.W, playn.core.Key.W );
		keymap.put( Key.E, playn.core.Key.E );
		keymap.put( Key.R, playn.core.Key.R );
		keymap.put( Key.T, playn.core.Key.T );
		keymap.put( Key.Y, playn.core.Key.Y );
		keymap.put( Key.U, playn.core.Key.U );
		keymap.put( Key.I, playn.core.Key.I );
		keymap.put( Key.O, playn.core.Key.O );
		keymap.put( Key.P, playn.core.Key.P );
		keymap.put( Key.LBRACKET, playn.core.Key.LEFT_BRACKET );
		keymap.put( Key.RBRACKET, playn.core.Key.RIGHT_BRACKET );
		keymap.put( Key.RETURN, playn.core.Key.ENTER );
		keymap.put( Key.LCONTROL, playn.core.Key.CONTROL );
		keymap.put( Key.A, playn.core.Key.A );
		keymap.put( Key.S, playn.core.Key.S );
		keymap.put( Key.D, playn.core.Key.D );
		keymap.put( Key.F, playn.core.Key.F );
		keymap.put( Key.G, playn.core.Key.G );
		keymap.put( Key.H, playn.core.Key.H );
		keymap.put( Key.J, playn.core.Key.J );
		keymap.put( Key.K, playn.core.Key.K );
		keymap.put( Key.L, playn.core.Key.L );
		keymap.put( Key.SEMICOLON, playn.core.Key.SEMICOLON );
		keymap.put( Key.APOSTROPHE, playn.core.Key.DOUBLE_QUOTE );
		keymap.put( Key.GRAVE, playn.core.Key.TILDE );
		keymap.put( Key.LSHIFT, playn.core.Key.SHIFT );
		keymap.put( Key.BACKSLASH, playn.core.Key.BACKSLASH );
		keymap.put( Key.Z, playn.core.Key.Z );
		keymap.put( Key.X, playn.core.Key.X );
		keymap.put( Key.C, playn.core.Key.C );
		keymap.put( Key.V, playn.core.Key.V );
		keymap.put( Key.B, playn.core.Key.B );
		keymap.put( Key.N, playn.core.Key.N );
		keymap.put( Key.M, playn.core.Key.M );
		keymap.put( Key.COMMA, playn.core.Key.COMMA );
		keymap.put( Key.PERIOD, playn.core.Key.PERIOD );
		keymap.put( Key.SLASH, playn.core.Key.SLASH );
		keymap.put( Key.RSHIFT, playn.core.Key.SHIFT );
		keymap.put( Key.MULTIPLY, playn.core.Key.MULTIPLY );
		keymap.put( Key.LALT, playn.core.Key.ALT );
		keymap.put( Key.SPACE, playn.core.Key.SPACE );
		keymap.put( Key.CAPITAL, playn.core.Key.CAPS_LOCK );
		keymap.put( Key.F1, playn.core.Key.F1 );
		keymap.put( Key.F2, playn.core.Key.F2 );
		keymap.put( Key.F3, playn.core.Key.F3 );
		keymap.put( Key.F4, playn.core.Key.F4 );
		keymap.put( Key.F5, playn.core.Key.F5 );
		keymap.put( Key.F6, playn.core.Key.F6 );
		keymap.put( Key.F7, playn.core.Key.F7 );
		keymap.put( Key.F8, playn.core.Key.F8 );
		keymap.put( Key.F9, playn.core.Key.F9 );
		keymap.put( Key.F10, playn.core.Key.F10 );
		keymap.put( Key.NUMLOCK, playn.core.Key.NP_NUM_LOCK );
		keymap.put( Key.SCROLL, playn.core.Key.SCROLL_LOCK );
		keymap.put( Key.NUMPAD7, playn.core.Key.NP7 );
		keymap.put( Key.NUMPAD8, playn.core.Key.NP8 );
		keymap.put( Key.NUMPAD9, playn.core.Key.NP9 );
		keymap.put( Key.SUBTRACT, playn.core.Key.NP_SUBTRACT );
		keymap.put( Key.NUMPAD4, playn.core.Key.NP4 );
		keymap.put( Key.NUMPAD5, playn.core.Key.NP5 );
		keymap.put( Key.NUMPAD6, playn.core.Key.NP6 );
		keymap.put( Key.ADD, playn.core.Key.NP_ADD );
		keymap.put( Key.NUMPAD1, playn.core.Key.NP1 );
		keymap.put( Key.NUMPAD2, playn.core.Key.NP2 );
		keymap.put( Key.NUMPAD3, playn.core.Key.NP3 );
		keymap.put( Key.NUMPAD0, playn.core.Key.NP0 );
		keymap.put( Key.DECIMAL, playn.core.Key.NP_DECIMAL );
		keymap.put( Key.F11, playn.core.Key.F11 );
		keymap.put( Key.F12, playn.core.Key.F12 );
		keymap.put( Key.CIRCUMFLEX, playn.core.Key.CIRCUMFLEX );
		keymap.put( Key.AT, playn.core.Key.AT );
		keymap.put( Key.COLON, playn.core.Key.COLON );
		keymap.put( Key.UNDERLINE, playn.core.Key.UNDERSCORE );
		keymap.put( Key.NUMPADENTER, playn.core.Key.ENTER );
		keymap.put( Key.RCONTROL, playn.core.Key.CONTROL );
		keymap.put( Key.NUMPADCOMMA, playn.core.Key.NP_DELETE );
		keymap.put( Key.DIVIDE, playn.core.Key.SEMICOLON );
		keymap.put( Key.SYSRQ, playn.core.Key.SYSRQ );
		keymap.put( Key.RALT, playn.core.Key.ALT );
		keymap.put( Key.PAUSE, playn.core.Key.PAUSE );
		keymap.put( Key.HOME, playn.core.Key.HOME );
		keymap.put( Key.UP, playn.core.Key.UP );
		keymap.put( Key.PAGE_UP, playn.core.Key.PAGE_UP );
		keymap.put( Key.LEFT, playn.core.Key.LEFT );
		keymap.put( Key.RIGHT, playn.core.Key.RIGHT );
		keymap.put( Key.END, playn.core.Key.END );
		keymap.put( Key.DOWN, playn.core.Key.DOWN );
		keymap.put( Key.PAGE_DOWN, playn.core.Key.PAGE_DOWN );
		keymap.put( Key.INSERT, playn.core.Key.INSERT );
		keymap.put( Key.DELETE, playn.core.Key.DELETE );
	}

	
	@Override
	public int getKeyboardCode( Key keyID ) {
		return keymap.get( keyID );
	}
		

	@Override
	public void processKeyboardKeyEvent( KeyboardKey key ) {
		if( Keyboard.getEventKey() == key.code ) {
			if( Keyboard.getEventKeyState() ) {
				key.state = Pushable.State.JUST_PRESSED;
			} else {
				key.state = Pushable.State.JUST_UNPRESSED;
			}
		}
	}

	@Override
	public void processMouseButtonEvent( MouseButton button ) {
		if( Mouse.getEventButton() == button.num ) {
			if( Mouse.getEventButtonState() ) {
				button.state = Pushable.State.JUST_PRESSED;
			} else {
				button.state = Pushable.State.JUST_UNPRESSED;
			}
		}
	}

	@Override
	public void processMouseWheelEvent( MouseWheel wheel ) {
		int dWheel =Mouse.getEventDWheel();
		if( dWheel != 0 ) {
			if( wheel.direction == dWheel ) wheel.state = Pushable.State.JUST_PRESSED;
		}
	}

	@Override
	public void init(int newWidth, int newHeight, double unitSize, boolean loadFont) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void drawOval(double x, double y, double width, double height, double angle, Color color, boolean empty) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void drawLongOval(double x, double y, double width, double height, double angle, Color color, boolean empty) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onKeyDown( Keyboard.Event event ) {
		for( ButtonAction controller: controllers ) {
			for( Pushable pushable : controller.buttonList ) {
				pushable.processKeyboardEvent();
				event.key().
			}
		}
	}

	@Override
	public void onKeyTyped(Keyboard.TypedEvent event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onKeyUp(Keyboard.Event event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onMouseDown(Mouse.ButtonEvent event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onMouseUp(Mouse.ButtonEvent event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onMouseMove(Mouse.MotionEvent event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onMouseWheelScroll(Mouse.WheelEvent event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onPointerStart(Pointer.Event event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onPointerEnd(Pointer.Event event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onPointerDrag(Pointer.Event event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onPointerCancel(Pointer.Event event) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
	
	public class Texture extends dwlab.base.images.Texture {
		private int textureID;

		@Override
		public final void init() {
			if( Platform.current == null ) return;
			if( !fileName.isEmpty() ) {
				textureID = glGenTextures();
				try {
					org.newdawn.slick.opengl.Texture texture = TextureLoader.getTexture( fileName.substring( fileName.length() - 3 ).toUpperCase(), 
							ResourceLoader.getResourceAsStream( fileName ) );
					textureID = texture.getTextureID();
					width = texture.getTextureWidth();
					height = texture.getTextureHeight();
				} catch ( IOException ex ) {
					Logger.getLogger( Texture.class.getName() ).log( Level.SEVERE, null, ex );
				}
			}
		}


		@Override
		public ImageBuffer getBuffer() {
			ImageBuffer buffer = new ImageBuffer( width, height );
			glBindTexture( GL_TEXTURE_2D, textureID );
			glGetTexImage( GL_TEXTURE_2D,  0, GL_RGBA, GL_INT, buffer.buffer );
			return buffer;
		}

		@Override
		public void applyBuffer( ImageBuffer buffer ) {
			glBindTexture( GL_TEXTURE_2D, textureID );
			glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR ); 
			glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR ); 
			glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, buffer.getWidth(), buffer.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer.buffer );
			width = buffer.getWidth();
			height = buffer.getHeight();
		}

		@Override
		public void pasteBuffer( ImageBuffer buffer, int x, int y ) {
			glBindTexture( GL_TEXTURE_2D, textureID );
			glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
			glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
			glTexSubImage2D( GL_TEXTURE_2D,  0,  x,  y,  buffer.getWidth(), buffer.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE,  buffer.buffer );
		}


		@Override
		public void draw( double x, double y, double width, double height, double tx1, double ty1, double tx2, double ty2, double angle, Color color ){
			width *= 0.5d;
			height *= 0.5d;
			glBindTexture( GL_TEXTURE_2D, textureID );
			glBegin( GL_QUADS );
				glColor4d( color.red, color.green, color.blue, color.alpha );

				if( angle == 0d ) {
					glTexCoord2d( tx1, ty1 );
					glVertex2d( x - width, y - height );
					glTexCoord2d( tx2, ty1 );
					glVertex2d( x + width, y - height );
					glTexCoord2d( tx2, ty2 );
					glVertex2d( x + width, y + height );
					glTexCoord2d( tx1, ty2 );
					glVertex2d( x - width, y + height );
				} else {
					double sin = Math.sin( angle );
					double cos = Math.cos( angle );
					glTexCoord2d( tx1, ty1 );
					glVertex2d( x + cos *  -width - sin * -height, y + sin *  -width + cos * -height );
					glTexCoord2d( tx2, ty1 );
					glVertex2d( x + cos *  width - sin * -height, y + sin *  width + cos * -height );
					glTexCoord2d( tx2, ty2 );
					glVertex2d( x  + cos *  width - sin * height, y + sin *  width + cos * height );
					glTexCoord2d( tx1, ty2 );
					glVertex2d( x  + cos *  -width - sin * height, y + sin *  -width + cos * height );
				}
			glEnd();
		}
	}
	
	@Override
	public dwlab.base.images.Texture createTexture( String fileName ) {
		Texture texture = new Texture();
		texture.fileName = fileName;
		Texture.textures.add( texture );
		texture.init();
		return texture;
	}
	
	

	public class Sound extends dwlab.base.Sound {
		Audio sound;


		public Sound( String fileName ) {
			String format;
			fileName = fileName.toLowerCase();
			if( fileName.endsWith( ".wav" ) ) {
				format = "WAV";
			} else if( fileName.endsWith( ".ogg" ) ) {
				format = "OGG";
			} else {
				return;
			}

			try {
				sound = AudioLoader.getAudio( format, ResourceLoader.getResourceAsStream( fileName ) );
			} catch ( IOException ex ) {
				Logger.getLogger( Sound.class.getName() ).log( Level.SEVERE, null, ex );
			}
		}


		@Override
		public void play() {
			sound.playAsSoundEffect( 1f, 1f, false );
		}


		@Override
		public void playLooped() {
			sound.playAsSoundEffect( 1f, 1f, true );
		}


		@Override
		public void stop() {
			sound.stop();
		}


		@Override
		public boolean isPlaying() {
			return sound.isPlaying();
		}	
	}
	
	@Override
	public dwlab.base.Sound createSound( String filename ) {
		return new Sound( filename );
	}
}