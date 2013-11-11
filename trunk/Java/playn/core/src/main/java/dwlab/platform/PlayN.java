package dwlab.platform;

import dwlab.base.Obj;
import dwlab.base.Project;
import dwlab.base.images.ImageBuffer;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.controllers.MouseWheel;
import dwlab.controllers.Pushable;
import dwlab.shapes.sprites.Camera;
import dwlab.visualizers.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import playn.core.Graphics;
import playn.core.Surface;

public class PlayN extends Platform {
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
			surface.drawLine( (float) ( x - width ), (float) ( y - height ), (float) ( x + width ), (float) ( y - height ), (float) lineWidth );
			surface.drawLine( (float) ( x + width ), (float) ( y - height ), (float) ( x + width ), (float) ( y + height ), (float) lineWidth );
			surface.drawLine( (float) ( x + width ), (float) ( y + height ), (float) ( x - width ), (float) ( y + height ), (float) lineWidth );
			surface.drawLine( (float) ( x - width ), (float) ( y + height ), (float) ( x - width ), (float) ( y - height ), (float) lineWidth );
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
		glClearColor( (float) color.red, (float) color.green, (float) color.blue, 1.0f );
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glClearDepth(1);
	}
	

	@Override
	public void setViewport( int vX, int vY, int vWidth, int vHeight ) {
		viewportX = vX;
		viewportY = vY;
		viewportWidth = vWidth;
		viewportHeight = vHeight;
		glViewport( vX - vWidth / 2, height - ( vY + vHeight / 2 ), vWidth, vHeight );
		glMatrixMode( GL_PROJECTION ) ;
		glLoadIdentity();
		glOrtho( vX - vWidth / 2, vX + vWidth / 2, vY + vHeight / 2, vY - vHeight / 2, -1d, 1d );
		glMatrixMode( GL_MODELVIEW ) ;
	}
	

	@Override
	public void swapBuffers() {
		Display.update();
	}	
	
	
	@Override
	public int getChar() {
		if( keys.isEmpty() ) return 0;
		int code = keys.getFirst();
		keys.removeFirst();
		return code;
	}
	
	
	@Override
	public int mouseX() {
		return Mouse.getX();
	}	
	
	@Override
	public int mouseY() {
		return getScreenHeight() - Mouse.getY();
	}
	
	
	@Override
	public void processEvents( Project project ) {
		Display.processMessages();
			
		for( ButtonAction controller: controllers ) {
			for( Pushable pushable : controller.buttonList ) {
				pushable.reset();
			}
		}
		
		while ( Keyboard.next() ) {
			for( ButtonAction controller: controllers ) {
				for( Pushable pushable : controller.buttonList ) {
					pushable.processKeyboardEvent();
					if( project != null ) project.onKeyboardEvent();
					if( Keyboard.getEventKeyState() ) keys.add( Keyboard.getEventKey() );
				}
			}
		}
		
		while ( Mouse.next() ) {
			for( ButtonAction controller: controllers ) {
				for( Pushable pushable : controller.buttonList ) {
					pushable.processMouseEvent();
					if( project != null ) project.onMouseEvent();
				}
			}
		}
		
		if( Display.isCloseRequested() ) if( project != null ) project.onCloseButton();
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
		Display.processMessages();
		
		while ( Keyboard.next() ) {
			if( Keyboard.getEventKeyState() ) {
				int key = Keyboard.getEventKey();
				for( Entry<Key,Integer> entry : keymap.entrySet() ) {
					if( entry.getValue() == key ) {
						action.addButton( KeyboardKey.create( entry.getKey() ) );
						break;
					}
				}
				
				return true;
			}
		}
		
		while ( Mouse.next() ) {
			if( Mouse.getEventButtonState() ) {
				action.addButton( MouseButton.create( Mouse.getEventButton() ) );
				return true;
			}
			
			int dWheel = Mouse.getEventDWheel();
			if( dWheel !=0 ) {
				action.addButton( MouseWheel.create( dWheel ) );
				return true;
			}
		}
		
		return false;
	}
	

	@SuppressWarnings( "empty-statement" )
	@Override
	public void waitForKey() {
		ButtonAction action = new ButtonAction();
		while( !getPushable( action ) );
	}
	

	private final HashMap<Key, Integer> keymap = new HashMap<Key, Integer>();
	
	public void initKeys() {
		keymap.put( Key.ESCAPE, Keyboard.KEY_ESCAPE );
		keymap.put( Key._1, Keyboard.KEY_1 );
		keymap.put( Key._2, Keyboard.KEY_2 );
		keymap.put( Key._3, Keyboard.KEY_3 );
		keymap.put( Key._4, Keyboard.KEY_4 );
		keymap.put( Key._5, Keyboard.KEY_5 );
		keymap.put( Key._6, Keyboard.KEY_6 );
		keymap.put( Key._7, Keyboard.KEY_7 );
		keymap.put( Key._8, Keyboard.KEY_8 );
		keymap.put( Key._9, Keyboard.KEY_9 );
		keymap.put( Key._0, Keyboard.KEY_0 );
		keymap.put( Key.MINUS, Keyboard.KEY_MINUS );
		keymap.put( Key.EQUALS, Keyboard.KEY_EQUALS );
		keymap.put( Key.BACK, Keyboard.KEY_BACK );
		keymap.put( Key.TAB, Keyboard.KEY_TAB );
		keymap.put( Key.Q, Keyboard.KEY_Q );
		keymap.put( Key.W, Keyboard.KEY_W );
		keymap.put( Key.E, Keyboard.KEY_E );
		keymap.put( Key.R, Keyboard.KEY_R );
		keymap.put( Key.T, Keyboard.KEY_T );
		keymap.put( Key.Y, Keyboard.KEY_Y );
		keymap.put( Key.U, Keyboard.KEY_U );
		keymap.put( Key.I, Keyboard.KEY_I );
		keymap.put( Key.O, Keyboard.KEY_O );
		keymap.put( Key.P, Keyboard.KEY_P );
		keymap.put( Key.LBRACKET, Keyboard.KEY_LBRACKET );
		keymap.put( Key.RBRACKET, Keyboard.KEY_RBRACKET );
		keymap.put( Key.RETURN, Keyboard.KEY_RETURN );
		keymap.put( Key.LCONTROL, Keyboard.KEY_LCONTROL );
		keymap.put( Key.A, Keyboard.KEY_A );
		keymap.put( Key.S, Keyboard.KEY_S );
		keymap.put( Key.D, Keyboard.KEY_D );
		keymap.put( Key.F, Keyboard.KEY_F );
		keymap.put( Key.G, Keyboard.KEY_G );
		keymap.put( Key.H, Keyboard.KEY_H );
		keymap.put( Key.J, Keyboard.KEY_J );
		keymap.put( Key.K, Keyboard.KEY_K );
		keymap.put( Key.L, Keyboard.KEY_L );
		keymap.put( Key.SEMICOLON, Keyboard.KEY_SEMICOLON );
		keymap.put( Key.APOSTROPHE, Keyboard.KEY_APOSTROPHE );
		keymap.put( Key.GRAVE, Keyboard.KEY_GRAVE );
		keymap.put( Key.LSHIFT, Keyboard.KEY_LSHIFT );
		keymap.put( Key.BACKSLASH, Keyboard.KEY_BACKSLASH );
		keymap.put( Key.Z, Keyboard.KEY_Z );
		keymap.put( Key.X, Keyboard.KEY_X );
		keymap.put( Key.C, Keyboard.KEY_C );
		keymap.put( Key.V, Keyboard.KEY_V );
		keymap.put( Key.B, Keyboard.KEY_B );
		keymap.put( Key.N, Keyboard.KEY_N );
		keymap.put( Key.M, Keyboard.KEY_M );
		keymap.put( Key.COMMA, Keyboard.KEY_COMMA );
		keymap.put( Key.PERIOD, Keyboard.KEY_PERIOD );
		keymap.put( Key.SLASH, Keyboard.KEY_SLASH );
		keymap.put( Key.RSHIFT, Keyboard.KEY_RSHIFT );
		keymap.put( Key.MULTIPLY, Keyboard.KEY_MULTIPLY );
		keymap.put( Key.LALT, Keyboard.KEY_LMENU );
		keymap.put( Key.SPACE, Keyboard.KEY_SPACE );
		keymap.put( Key.CAPITAL, Keyboard.KEY_CAPITAL );
		keymap.put( Key.F1, Keyboard.KEY_F1 );
		keymap.put( Key.F2, Keyboard.KEY_F2 );
		keymap.put( Key.F3, Keyboard.KEY_F3 );
		keymap.put( Key.F4, Keyboard.KEY_F4 );
		keymap.put( Key.F5, Keyboard.KEY_F5 );
		keymap.put( Key.F6, Keyboard.KEY_F6 );
		keymap.put( Key.F7, Keyboard.KEY_F7 );
		keymap.put( Key.F8, Keyboard.KEY_F8 );
		keymap.put( Key.F9, Keyboard.KEY_F9 );
		keymap.put( Key.F10, Keyboard.KEY_F10 );
		keymap.put( Key.NUMLOCK, Keyboard.KEY_NUMLOCK );
		keymap.put( Key.SCROLL, Keyboard.KEY_SCROLL );
		keymap.put( Key.NUMPAD7, Keyboard.KEY_NUMPAD7 );
		keymap.put( Key.NUMPAD8, Keyboard.KEY_NUMPAD8 );
		keymap.put( Key.NUMPAD9, Keyboard.KEY_NUMPAD9 );
		keymap.put( Key.SUBTRACT, Keyboard.KEY_SUBTRACT );
		keymap.put( Key.NUMPAD4, Keyboard.KEY_NUMPAD4 );
		keymap.put( Key.NUMPAD5, Keyboard.KEY_NUMPAD5 );
		keymap.put( Key.NUMPAD6, Keyboard.KEY_NUMPAD6 );
		keymap.put( Key.ADD, Keyboard.KEY_ADD );
		keymap.put( Key.NUMPAD1, Keyboard.KEY_NUMPAD1 );
		keymap.put( Key.NUMPAD2, Keyboard.KEY_NUMPAD2 );
		keymap.put( Key.NUMPAD3, Keyboard.KEY_NUMPAD3 );
		keymap.put( Key.NUMPAD0, Keyboard.KEY_NUMPAD0 );
		keymap.put( Key.DECIMAL, Keyboard.KEY_DECIMAL );
		keymap.put( Key.F11, Keyboard.KEY_F11 );
		keymap.put( Key.F12, Keyboard.KEY_F12 );
		keymap.put( Key.F13, Keyboard.KEY_F13 );
		keymap.put( Key.F14, Keyboard.KEY_F14 );
		keymap.put( Key.F15, Keyboard.KEY_F15 );
		keymap.put( Key.KANA, Keyboard.KEY_KANA );
		keymap.put( Key.CONVERT, Keyboard.KEY_CONVERT );
		keymap.put( Key.NOCONVERT, Keyboard.KEY_NOCONVERT );
		keymap.put( Key.YEN, Keyboard.KEY_YEN );
		keymap.put( Key.NUMPADEQUALS, Keyboard.KEY_NUMPADEQUALS );
		keymap.put( Key.CIRCUMFLEX, Keyboard.KEY_CIRCUMFLEX );
		keymap.put( Key.AT, Keyboard.KEY_AT );
		keymap.put( Key.COLON, Keyboard.KEY_COLON );
		keymap.put( Key.UNDERLINE, Keyboard.KEY_UNDERLINE );
		keymap.put( Key.KANJI, Keyboard.KEY_KANJI );
		keymap.put( Key.STOP, Keyboard.KEY_STOP );
		keymap.put( Key.AX, Keyboard.KEY_AX );
		keymap.put( Key.UNLABELED, Keyboard.KEY_UNLABELED );
		keymap.put( Key.NUMPADENTER, Keyboard.KEY_NUMPADENTER );
		keymap.put( Key.RCONTROL, Keyboard.KEY_RCONTROL );
		keymap.put( Key.NUMPADCOMMA, Keyboard.KEY_NUMPADCOMMA );
		keymap.put( Key.DIVIDE, Keyboard.KEY_DIVIDE );
		keymap.put( Key.SYSRQ, Keyboard.KEY_SYSRQ );
		keymap.put( Key.RALT, Keyboard.KEY_RMENU );
		keymap.put( Key.PAUSE, Keyboard.KEY_PAUSE );
		keymap.put( Key.HOME, Keyboard.KEY_HOME );
		keymap.put( Key.UP, Keyboard.KEY_UP );
		keymap.put( Key.PAGE_UP, Keyboard.KEY_PRIOR );
		keymap.put( Key.LEFT, Keyboard.KEY_LEFT );
		keymap.put( Key.RIGHT, Keyboard.KEY_RIGHT );
		keymap.put( Key.END, Keyboard.KEY_END );
		keymap.put( Key.DOWN, Keyboard.KEY_DOWN );
		keymap.put( Key.PAGE_DOWN, Keyboard.KEY_NEXT );
		keymap.put( Key.INSERT, Keyboard.KEY_INSERT );
		keymap.put( Key.DELETE, Keyboard.KEY_DELETE );
		keymap.put( Key.LMETA, Keyboard.KEY_LMETA );
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
	
	
	
	public class Font extends dwlab.base.Font {
		String fileName;
		TrueTypeFont font;
		int textureID;
		float size;


		private Font( String fileName, float size ) {
			this.fileName = fileName;
			this.size = size;
		}


		public void init() {
			try {
				InputStream inputStream	= ResourceLoader.getResourceAsStream( fileName );
				java.awt.Font awtFont = java.awt.Font.createFont( java.awt.Font.TRUETYPE_FONT, inputStream );
				awtFont = awtFont.deriveFont( size ); // set font size
				font = new TrueTypeFont( awtFont, false );
				this.textureID = glGetInteger( GL_TEXTURE_BINDING_2D );
			} catch (Exception e) {
				int a = 0;
			}
		}

		
		@Override
		public void drawString( String string, float x, float y ) {
			glBindTexture( GL_TEXTURE_2D, textureID );
			if( currentContourColor != null ) {
				for( int dY = -1; dY <= 1; dY++ ) {
					for( int dX = Math.abs( dY ) - 1; dX <= 1 - Math.abs( dY ); dX++ ) {
						font.drawString( x + dX, y + dY, string, currentContourColor );
					}
				}
			}
			font.drawString( x, y, string, currentTextColor );
		}

		@Override
		public double getTextWidth( String text ) {
			return font.getWidth( text );
		}

		@Override
		public double getTextHeight() {
			return font.getHeight();
		}
	}
	
	@Override
	public dwlab.base.Font createFont( String fileName, float size ) {
		Font font = new Font( fileName, size );
		font.init();
		return font;
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
}*/