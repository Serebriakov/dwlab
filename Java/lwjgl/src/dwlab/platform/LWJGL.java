package dwlab.platform;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

public class LWJGL extends Platform {
	private Font currentFont;
	private org.newdawn.slick.Color currentTextColor = org.newdawn.slick.Color.white;
	private org.newdawn.slick.Color currentContourColor = null;
	
	public static void init() {
		current = new LWJGL();
		current.init( 800, 600, 25d, true );
	}
	
	/**
	* Sets graphics mode.
	* Provide width and height of screen in pixels and unit size in pixels for camera.

	* @see #parallax example
	*/
	@Override
	public void init( int newWidth, int newHeight, double unitSize, boolean loadFont ) {
		if( Display.isCreated() ) return;
		
		width =newWidth;
		height = newHeight;
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create();
		} catch ( LWJGLException ex ) {
			Logger.getLogger( Platform.class.getName() ).log( Level.SEVERE, null, ex );
		}

		glShadeModel( GL_SMOOTH );
		glDisable( GL_DEPTH_TEST );
		glDisable( GL_LIGHTING ); 
		
		glEnable( GL_BLEND );
		glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA );
		glEnable( GL_TEXTURE_2D );
		
		glMatrixMode( GL_PROJECTION ) ;
		glLoadIdentity();
		glOrtho( 0d, width, height, 0d, -1d, 1d );
		glMatrixMode( GL_MODELVIEW ) ;
		
		resetViewport();

		try {
			Mouse.create();
		} catch ( LWJGLException ex ) {
			Logger.getLogger( Platform.class.getName() ).log( Level.SEVERE, null, ex );
		}
		
		if( loadFont ) setCurrentFont( Font.load( "res/font.ttf", 16 ) );
	}
	
	@Override
	public void initCamera( double unitSize ) {
		Camera.current.viewport.setCoords( 0.5d * width, 0.5d * height );
		Camera.current.viewport.setSize( width, height );
		Camera.current.setSize( width / unitSize, height / unitSize );
		Camera.current.setCoords( 0d, 0d );
	}
	
	
	@Override
	public double getTextWidth( String text ) {
		return currentFont.font.getWidth( text );
	}

	@Override
	public double getTextHeight() {
		return currentFont.font.getHeight();
	}
	
	@Override
	public Color getContourColor() {
		if( currentContourColor == null ) return null;
		return new Color( currentContourColor.r, currentContourColor.g, currentContourColor.b, currentContourColor.a );
	}
	
	@Override
	public void setContourColor( double red, double green, double blue, double alpha ) {
		currentContourColor = new org.newdawn.slick.Color( (float) red, (float) green, (float) blue, (float) alpha );
	}
	
	@Override
	public void setContourColor( Color color ) {
		if( color == null ) {
			currentContourColor = null;
		} else {
			currentContourColor = new org.newdawn.slick.Color( (float) color.red, (float) color.green, (float) color.blue, (float) color.alpha );
		}
	}
	
	
	@Override
	public void setCurrentFont( Font font ) {
		currentFont = font;
	}
	
	@Override
	public Color getTextColor() {
		return new Color( currentTextColor.r, currentTextColor.g, currentTextColor.b, currentTextColor.a );
	}
	
	@Override
	public void setTextColor( double red, double green, double blue, double alpha ) {
		currentTextColor = new org.newdawn.slick.Color( (float) red, (float) green, (float) blue, (float) alpha );
	}
	
	@Override
	public void setTextColor( Color color ) {
		currentTextColor = new org.newdawn.slick.Color( (float) color.red, (float) color.green, (float) color.blue, (float) color.alpha );
	}

	

	@Override
	public void drawLine( double x1, double y1, double x2, double y2, double width, Color color ) {
		glDisable( GL_TEXTURE_2D );
		
		glColor4d( color.red, color.green, color.blue, color.alpha );
		glLineWidth( (float) width );
		glBegin( GL_LINES );
			glVertex2d( x1, y1 );
			glVertex2d( x2, y2 );
		glEnd();		
		
		glEnable( GL_TEXTURE_2D ); 
	}
	
	
	@Override
	public void drawRectangle( double x, double y, double width, double height, double angle, Color color, boolean empty ){
		width *= 0.5d ;
		height *= 0.5d ;
		if( empty ) {
			startPolygon( 4, color, empty );
			addPolygonVertex( x - width, y - height );
			addPolygonVertex( x + width, y - height );
			addPolygonVertex( x + width, y + height );
			addPolygonVertex( x - width, y + height );
			drawPolygon();
		} else {
			glDisable(GL_TEXTURE_2D);
			
			glColor4d( color.red, color.green, color.blue, color.alpha );
			glRectd( x - width, y - height, x + width, y + height );
		
			glEnable( GL_TEXTURE_2D ); 
		}
	}
	
		
	@Override
	public void drawOval( double x, double y, double width, double height, double angle, Color color, boolean empty ){
		width *= 0.5d ;
		height *= 0.5d ;
		double vertexQuantity = Math.ceil( 2 * Math.PI / Math.acos( 1d - 1d / Math.max( width, height ) ) );
		startPolygon( (int) vertexQuantity, color, empty );
		for( double n = 0; n < vertexQuantity; n++ ) {
			double ang = Math.PI * 2 * n / vertexQuantity;
			addPolygonVertex( x + width * Math.cos( ang ), y + height * Math.sin( ang ) );
		}
		drawPolygon();
	}
	

	@Override
	public void drawLongOval( double x, double y, double width, double height, double angle, Color color, boolean empty ) {
		width *= 0.5d ;
		height *= 0.5d ;
		double vertexQuantity = Math.ceil( Math.PI / Math.acos( 1d - 1d / Math.max( width, height ) ) );
		startPolygon( (int) vertexQuantity * 2 + 2, color, empty );
		if( width > height ) {
			for( int side = 0; side < 2; side++ ) {
				double dsize = ( side == 0 ? width - height : height - width );
				for( double n = 0; n <= vertexQuantity; n++ ) {
					double ang = Math.PI * ( side - 0.5 + n / vertexQuantity );
					addPolygonVertex( x + height * Math.cos( ang ) + dsize, y + height * Math.sin( ang ) );
				}
			}
		} else {
			for( int side = 0; side < 2; side++ ) {
				double dsize = ( side == 0 ? height - width: width - height );
				for( double n = 0; n <= vertexQuantity; n++ ) {
					double ang = Math.PI * ( side + n / vertexQuantity );
					addPolygonVertex( x + width * Math.cos( ang ), y + width * Math.sin( ang ) + dsize );
				}
			}
		}
		drawPolygon();
	}
	

	@Override
	public void startPolygon( int vertexQuantity, Color color, boolean empty ) {
		glDisable( GL_TEXTURE_2D );
		
		if( empty ) glBegin( GL_LINE_LOOP ); else glBegin( GL_POLYGON );
		glColor4d( color.red, color.green, color.blue, color.alpha );
	}

	@Override
	public void addPolygonVertex( double x, double y ) {
		glVertex2d( x, y );
	}

	@Override
	public void drawPolygon() {
		glEnd();
		
		glEnable( GL_TEXTURE_2D ); 
	}
	

	public void drawText( String string, float x, float y, org.newdawn.slick.Color color ) {
		glBindTexture( GL_TEXTURE_2D, currentFont.textureID );
		if( currentContourColor != null ) {
			for( int dY = -1; dY <= 1; dY++ ) {
				for( int dX = Math.abs( dY ) - 1; dX <= 1 - Math.abs( dY ); dX++ ) {
					currentFont.font.drawString( x +dX, y + dY, string, currentContourColor );
				}
			}
		}
		currentFont.font.drawString( x, y, string, color );
	}
	
	@Override
	public void drawText( String string, double x, double y, Color color ) {
		drawText( string, (float) x, (float) y, new org.newdawn.slick.Color( (float) color.red, (float) color.green, (float) color.blue, (float) color.alpha ) );
	}
	
	@Override
	public void drawText( String string, double x, double y ) {
		drawText( string, (float) x, (float) y, currentTextColor );
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
				action.addButton( KeyboardKey.create( Keyboard.getEventKey() ) );
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
	

	@Override
	public int getKeyboardCode( Key keyID ) {
		switch( keyID ){
			case ESCAPE: return Keyboard.KEY_ESCAPE;
			case _1: return Keyboard.KEY_1;
			case _2: return Keyboard.KEY_2;
			case _3: return Keyboard.KEY_3;
			case _4: return Keyboard.KEY_4;
			case _5: return Keyboard.KEY_5;
			case _6: return Keyboard.KEY_6;
			case _7: return Keyboard.KEY_7;
			case _8: return Keyboard.KEY_8;
			case _9: return Keyboard.KEY_9;
			case _0: return Keyboard.KEY_0;
			case MINUS: return Keyboard.KEY_MINUS;
			case EQUALS: return Keyboard.KEY_EQUALS;
			case BACK: return Keyboard.KEY_BACK;
			case TAB: return Keyboard.KEY_TAB;
			case Q: return Keyboard.KEY_Q;
			case W: return Keyboard.KEY_W;
			case E: return Keyboard.KEY_E;
			case R: return Keyboard.KEY_R;
			case T: return Keyboard.KEY_T;
			case Y: return Keyboard.KEY_Y;
			case U: return Keyboard.KEY_U;
			case I: return Keyboard.KEY_I;
			case O: return Keyboard.KEY_O;
			case P: return Keyboard.KEY_P;
			case LBRACKET: return Keyboard.KEY_LBRACKET;
			case RBRACKET: return Keyboard.KEY_RBRACKET;
			case RETURN: return Keyboard.KEY_RETURN;
			case LCONTROL: return Keyboard.KEY_LCONTROL;
			case A: return Keyboard.KEY_A;
			case S: return Keyboard.KEY_S;
			case D: return Keyboard.KEY_D;
			case F: return Keyboard.KEY_F;
			case G: return Keyboard.KEY_G;
			case H: return Keyboard.KEY_H;
			case J: return Keyboard.KEY_J;
			case K: return Keyboard.KEY_K;
			case L: return Keyboard.KEY_L;
			case SEMICOLON: return Keyboard.KEY_SEMICOLON;
			case APOSTROPHE: return Keyboard.KEY_APOSTROPHE;
			case GRAVE: return Keyboard.KEY_GRAVE;
			case LSHIFT: return Keyboard.KEY_LSHIFT;
			case BACKSLASH: return Keyboard.KEY_BACKSLASH;
			case Z: return Keyboard.KEY_Z;
			case X: return Keyboard.KEY_X;
			case C: return Keyboard.KEY_C;
			case V: return Keyboard.KEY_V;
			case B: return Keyboard.KEY_B;
			case N: return Keyboard.KEY_N;
			case M: return Keyboard.KEY_M;
			case COMMA: return Keyboard.KEY_COMMA;
			case PERIOD: return Keyboard.KEY_PERIOD;
			case SLASH: return Keyboard.KEY_SLASH;
			case RSHIFT: return Keyboard.KEY_RSHIFT;
			case MULTIPLY: return Keyboard.KEY_MULTIPLY;
			case LALT: return Keyboard.KEY_LMENU;
			case SPACE: return Keyboard.KEY_SPACE;
			case CAPITAL: return Keyboard.KEY_CAPITAL;
			case F1: return Keyboard.KEY_F1;
			case F2: return Keyboard.KEY_F2;
			case F3: return Keyboard.KEY_F3;
			case F4: return Keyboard.KEY_F4;
			case F5: return Keyboard.KEY_F5;
			case F6: return Keyboard.KEY_F6;
			case F7: return Keyboard.KEY_F7;
			case F8: return Keyboard.KEY_F8;
			case F9: return Keyboard.KEY_F9;
			case F10: return Keyboard.KEY_F10;
			case NUMLOCK: return Keyboard.KEY_NUMLOCK;
			case SCROLL: return Keyboard.KEY_SCROLL;
			case NUMPAD7: return Keyboard.KEY_NUMPAD7;
			case NUMPAD8: return Keyboard.KEY_NUMPAD8;
			case NUMPAD9: return Keyboard.KEY_NUMPAD9;
			case SUBTRACT: return Keyboard.KEY_SUBTRACT;
			case NUMPAD4: return Keyboard.KEY_NUMPAD4;
			case NUMPAD5: return Keyboard.KEY_NUMPAD5;
			case NUMPAD6: return Keyboard.KEY_NUMPAD6;
			case ADD: return Keyboard.KEY_ADD;
			case NUMPAD1: return Keyboard.KEY_NUMPAD1;
			case NUMPAD2: return Keyboard.KEY_NUMPAD2;
			case NUMPAD3: return Keyboard.KEY_NUMPAD3;
			case NUMPAD0: return Keyboard.KEY_NUMPAD0;
			case DECIMAL: return Keyboard.KEY_DECIMAL;
			case F11: return Keyboard.KEY_F11;
			case F12: return Keyboard.KEY_F12;
			case F13: return Keyboard.KEY_F13;
			case F14: return Keyboard.KEY_F14;
			case F15: return Keyboard.KEY_F15;
			case KANA: return Keyboard.KEY_KANA;
			case CONVERT: return Keyboard.KEY_CONVERT;
			case NOCONVERT: return Keyboard.KEY_NOCONVERT;
			case YEN: return Keyboard.KEY_YEN;
			case NUMPADEQUALS: return Keyboard.KEY_NUMPADEQUALS;
			case CIRCUMFLEX: return Keyboard.KEY_CIRCUMFLEX;
			case AT: return Keyboard.KEY_AT;
			case COLON: return Keyboard.KEY_COLON;
			case UNDERLINE: return Keyboard.KEY_UNDERLINE;
			case KANJI: return Keyboard.KEY_KANJI;
			case STOP: return Keyboard.KEY_STOP;
			case AX: return Keyboard.KEY_AX;
			case UNLABELED: return Keyboard.KEY_UNLABELED;
			case NUMPADENTER: return Keyboard.KEY_NUMPADENTER;
			case RCONTROL: return Keyboard.KEY_RCONTROL;
			case NUMPADCOMMA: return Keyboard.KEY_NUMPADCOMMA;
			case DIVIDE: return Keyboard.KEY_DIVIDE;
			case SYSRQ: return Keyboard.KEY_SYSRQ;
			case RALT: return Keyboard.KEY_RMENU;
			case PAUSE: return Keyboard.KEY_PAUSE;
			case HOME: return Keyboard.KEY_HOME;
			case UP: return Keyboard.KEY_UP;
			case PAGE_UP: return Keyboard.KEY_PRIOR;
			case LEFT: return Keyboard.KEY_LEFT;
			case RIGHT: return Keyboard.KEY_RIGHT;
			case END: return Keyboard.KEY_END;
			case DOWN: return Keyboard.KEY_DOWN;
			case PAGE_DOWN: return Keyboard.KEY_NEXT;
			case INSERT: return Keyboard.KEY_INSERT;
			case DELETE: return Keyboard.KEY_DELETE;
			case LMETA: return Keyboard.KEY_LMETA;
			default: return 0;
		}
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

		public Texture() {
		}

		public Texture( String fileName ) {
			this.fileName = fileName;
			this.textureID = glGenTextures();
			this.init();
		}

		/**
		 * Initializes image.
		 * Splits image by XCells x YCells grid. Will be executed after loading image object from XML file.
		 */
		@Override
		public final void init() {
			if( !fileName.isEmpty() ) {
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
	public dwlab.base.images.Texture createTexture( String filename ) {
		return new Texture( filename );
	}
	
	
	
	public class Font extends dwlab.base.Font {
		TrueTypeFont font;
		int textureID;


		private Font( java.awt.Font font ) {
			this.font = new TrueTypeFont( font, false );
			this.textureID = glGetInteger( GL_TEXTURE_BINDING_2D );
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
	
	@Override
	public dwlab.base.Font createFont( String fileName, float size ) {
		return new Font( fileName, size );
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