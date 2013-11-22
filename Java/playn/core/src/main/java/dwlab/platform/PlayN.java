package dwlab.platform;

import dwlab.base.AbstractSound;
import dwlab.base.images.AbstractTexture;
import dwlab.base.images.ImageBuffer;
import dwlab.controllers.Button;
import dwlab.controllers.ButtonAction;
import dwlab.visualizers.Color;
import java.util.HashMap;
import playn.core.Keyboard;
import playn.core.Mouse;
import playn.core.Surface;
import static playn.core.PlayN.*;
import static dwlab.platform.Functions.*;
import java.nio.Buffer;
import static playn.core.gl.GL20.*;
import java.util.Map.Entry;
import playn.core.Image;
import playn.core.Key;
import playn.core.Sound;
import playn.core.gl.GLBuffer;

public class PlayN extends Platform implements Keyboard.Listener, Mouse.Listener {
	Surface surface;

	@Override
	public void init(int newWidth, int newHeight, double unitSize, boolean loadFont) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
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

	@Override
	public void drawOval(double x, double y, double width, double height, double angle, Color color, boolean empty) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void drawLongOval(double x, double y, double width, double height, double angle, Color color, boolean empty) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
	public void processEvents() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
	@Override
	public Button createButton( Button.Name buttonName ) {
		Button newButton = new Button( buttonName );
		for( ButtonAction action: controllers ) {
			for( Button button: action.buttonList ) {
				if( button.equals( button ) ) return button;
			}
		}
		
		if( currentPlatform != null ) newButton.init();

		return newButton;
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
	

	@Override
	public void flushControllers() {
		boolean exiting = false;
		while( !exiting ) {
			exiting = true;
			
			for( ButtonAction controller: controllers ) {
				if( controller.isDown() ) exiting = false;
			}
		}
	}
	

	@SuppressWarnings( "empty-statement" )
	@Override
	public void waitForKey() {
	}
	

	private final HashMap<Button.Name, playn.core.Key> buttonMap = new HashMap<Button.Name, playn.core.Key>();
	
	public void initKeys() {
		buttonMap.put( Button.Name.ESCAPE, playn.core.Key.ESCAPE );
		buttonMap.put( Button.Name._1, playn.core.Key.K1 );
		buttonMap.put( Button.Name._2, playn.core.Key.K2 );
		buttonMap.put( Button.Name._3, playn.core.Key.K3 );
		buttonMap.put( Button.Name._4, playn.core.Key.K4 );
		buttonMap.put( Button.Name._5, playn.core.Key.K5 );
		buttonMap.put( Button.Name._6, playn.core.Key.K6 );
		buttonMap.put( Button.Name._7, playn.core.Key.K7 );
		buttonMap.put( Button.Name._8, playn.core.Key.K8 );
		buttonMap.put( Button.Name._9, playn.core.Key.K9 );
		buttonMap.put( Button.Name._0, playn.core.Key.K0 );
		buttonMap.put( Button.Name.MINUS, playn.core.Key.MINUS );
		buttonMap.put( Button.Name.EQUALS, playn.core.Key.EQUALS );
		buttonMap.put( Button.Name.BACK, playn.core.Key.BACK );
		buttonMap.put( Button.Name.TAB, playn.core.Key.TAB );
		buttonMap.put( Button.Name.Q, playn.core.Key.Q );
		buttonMap.put( Button.Name.W, playn.core.Key.W );
		buttonMap.put( Button.Name.E, playn.core.Key.E );
		buttonMap.put( Button.Name.R, playn.core.Key.R );
		buttonMap.put( Button.Name.T, playn.core.Key.T );
		buttonMap.put( Button.Name.Y, playn.core.Key.Y );
		buttonMap.put( Button.Name.U, playn.core.Key.U );
		buttonMap.put( Button.Name.I, playn.core.Key.I );
		buttonMap.put( Button.Name.O, playn.core.Key.O );
		buttonMap.put( Button.Name.P, playn.core.Key.P );
		buttonMap.put( Button.Name.LBRACKET, playn.core.Key.LEFT_BRACKET );
		buttonMap.put( Button.Name.RBRACKET, playn.core.Key.RIGHT_BRACKET );
		buttonMap.put( Button.Name.RETURN, playn.core.Key.ENTER );
		buttonMap.put( Button.Name.LCONTROL, playn.core.Key.CONTROL );
		buttonMap.put( Button.Name.A, playn.core.Key.A );
		buttonMap.put( Button.Name.S, playn.core.Key.S );
		buttonMap.put( Button.Name.D, playn.core.Key.D );
		buttonMap.put( Button.Name.F, playn.core.Key.F );
		buttonMap.put( Button.Name.G, playn.core.Key.G );
		buttonMap.put( Button.Name.H, playn.core.Key.H );
		buttonMap.put( Button.Name.J, playn.core.Key.J );
		buttonMap.put( Button.Name.K, playn.core.Key.K );
		buttonMap.put( Button.Name.L, playn.core.Key.L );
		buttonMap.put( Button.Name.SEMICOLON, playn.core.Key.SEMICOLON );
		buttonMap.put( Button.Name.APOSTROPHE, playn.core.Key.DOUBLE_QUOTE );
		buttonMap.put( Button.Name.GRAVE, playn.core.Key.TILDE );
		buttonMap.put( Button.Name.LSHIFT, playn.core.Key.SHIFT );
		buttonMap.put( Button.Name.BACKSLASH, playn.core.Key.BACKSLASH );
		buttonMap.put( Button.Name.Z, playn.core.Key.Z );
		buttonMap.put( Button.Name.X, playn.core.Key.X );
		buttonMap.put( Button.Name.C, playn.core.Key.C );
		buttonMap.put( Button.Name.V, playn.core.Key.V );
		buttonMap.put( Button.Name.B, playn.core.Key.B );
		buttonMap.put( Button.Name.N, playn.core.Key.N );
		buttonMap.put( Button.Name.M, playn.core.Key.M );
		buttonMap.put( Button.Name.COMMA, playn.core.Key.COMMA );
		buttonMap.put( Button.Name.PERIOD, playn.core.Key.PERIOD );
		buttonMap.put( Button.Name.SLASH, playn.core.Key.SLASH );
		buttonMap.put( Button.Name.RSHIFT, playn.core.Key.SHIFT );
		buttonMap.put( Button.Name.MULTIPLY, playn.core.Key.MULTIPLY );
		buttonMap.put( Button.Name.LALT, playn.core.Key.ALT );
		buttonMap.put( Button.Name.SPACE, playn.core.Key.SPACE );
		buttonMap.put( Button.Name.CAPITAL, playn.core.Key.CAPS_LOCK );
		buttonMap.put( Button.Name.F1, playn.core.Key.F1 );
		buttonMap.put( Button.Name.F2, playn.core.Key.F2 );
		buttonMap.put( Button.Name.F3, playn.core.Key.F3 );
		buttonMap.put( Button.Name.F4, playn.core.Key.F4 );
		buttonMap.put( Button.Name.F5, playn.core.Key.F5 );
		buttonMap.put( Button.Name.F6, playn.core.Key.F6 );
		buttonMap.put( Button.Name.F7, playn.core.Key.F7 );
		buttonMap.put( Button.Name.F8, playn.core.Key.F8 );
		buttonMap.put( Button.Name.F9, playn.core.Key.F9 );
		buttonMap.put( Button.Name.F10, playn.core.Key.F10 );
		buttonMap.put( Button.Name.NUMLOCK, playn.core.Key.NP_NUM_LOCK );
		buttonMap.put( Button.Name.SCROLL, playn.core.Key.SCROLL_LOCK );
		buttonMap.put( Button.Name.NUMPAD7, playn.core.Key.NP7 );
		buttonMap.put( Button.Name.NUMPAD8, playn.core.Key.NP8 );
		buttonMap.put( Button.Name.NUMPAD9, playn.core.Key.NP9 );
		buttonMap.put( Button.Name.SUBTRACT, playn.core.Key.NP_SUBTRACT );
		buttonMap.put( Button.Name.NUMPAD4, playn.core.Key.NP4 );
		buttonMap.put( Button.Name.NUMPAD5, playn.core.Key.NP5 );
		buttonMap.put( Button.Name.NUMPAD6, playn.core.Key.NP6 );
		buttonMap.put( Button.Name.ADD, playn.core.Key.NP_ADD );
		buttonMap.put( Button.Name.NUMPAD1, playn.core.Key.NP1 );
		buttonMap.put( Button.Name.NUMPAD2, playn.core.Key.NP2 );
		buttonMap.put( Button.Name.NUMPAD3, playn.core.Key.NP3 );
		buttonMap.put( Button.Name.NUMPAD0, playn.core.Key.NP0 );
		buttonMap.put( Button.Name.DECIMAL, playn.core.Key.NP_DECIMAL );
		buttonMap.put( Button.Name.F11, playn.core.Key.F11 );
		buttonMap.put( Button.Name.F12, playn.core.Key.F12 );
		buttonMap.put( Button.Name.CIRCUMFLEX, playn.core.Key.CIRCUMFLEX );
		buttonMap.put( Button.Name.AT, playn.core.Key.AT );
		buttonMap.put( Button.Name.COLON, playn.core.Key.COLON );
		buttonMap.put( Button.Name.UNDERLINE, playn.core.Key.UNDERSCORE );
		buttonMap.put( Button.Name.NUMPADENTER, playn.core.Key.ENTER );
		buttonMap.put( Button.Name.RCONTROL, playn.core.Key.CONTROL );
		buttonMap.put( Button.Name.NUMPADCOMMA, playn.core.Key.NP_DELETE );
		buttonMap.put( Button.Name.DIVIDE, playn.core.Key.SEMICOLON );
		buttonMap.put( Button.Name.SYSRQ, playn.core.Key.SYSRQ );
		buttonMap.put( Button.Name.RALT, playn.core.Key.ALT );
		buttonMap.put( Button.Name.PAUSE, playn.core.Key.PAUSE );
		buttonMap.put( Button.Name.HOME, playn.core.Key.HOME );
		buttonMap.put( Button.Name.UP, playn.core.Key.UP );
		buttonMap.put( Button.Name.PAGE_UP, playn.core.Key.PAGE_UP );
		buttonMap.put( Button.Name.LEFT, playn.core.Key.LEFT );
		buttonMap.put( Button.Name.RIGHT, playn.core.Key.RIGHT );
		buttonMap.put( Button.Name.END, playn.core.Key.END );
		buttonMap.put( Button.Name.DOWN, playn.core.Key.DOWN );
		buttonMap.put( Button.Name.PAGE_DOWN, playn.core.Key.PAGE_DOWN );
		buttonMap.put( Button.Name.INSERT, playn.core.Key.INSERT );
		buttonMap.put( Button.Name.DELETE, playn.core.Key.DELETE );
	}

	private Button.Name findButtonName( Key key ) {
		for( Entry<Button.Name, playn.core.Key> entry : buttonMap.entrySet() ) {
			if( entry.getValue() == key) return entry.getKey();
		}
		return null;
	}
	
	private void setButtonState( Button.Name buttonName, Button.State state ) {
		for( ButtonAction controller: controllers ) {
			for( Button button : controller.buttonList ) {
				if( button.name == buttonName ) button.state = state;
			}
		}
	}
	
	@Override
	public void onKeyDown( Keyboard.Event event ) {
		setButtonState( findButtonName( event.key() ), Button.State.JUST_PRESSED );
	}

	@Override
	public void onKeyTyped( Keyboard.TypedEvent event ) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void onKeyUp( Keyboard.Event event ) {
		setButtonState( findButtonName( event.key() ), Button.State.JUST_UNPRESSED );
	}

	private void setMouseButtonState( int buttonNum, Button.State state ) {
		switch( buttonNum ) {
			case Mouse.BUTTON_LEFT:
				setButtonState( Button.Name.LEFT_MOUSE_BUTTON, state );
				break;
			case Mouse.BUTTON_MIDDLE:
				setButtonState( Button.Name.MIDDLE_MOUSE_BUTTON, state );
				break;
			case Mouse.BUTTON_RIGHT:
				setButtonState( Button.Name.RIGHT_MOUSE_BUTTON, state );
				break;
		}
	}
	
	@Override
	public void onMouseDown( Mouse.ButtonEvent event ) {
		setMouseButtonState( event.button(), Button.State.JUST_PRESSED );
	}

	@Override
	public void onMouseUp( Mouse.ButtonEvent event ) {
		setMouseButtonState( event.button(), Button.State.JUST_UNPRESSED );
	}

	@Override
	public void onMouseMove( Mouse.MotionEvent event ) {
		mouseX = (int) event.x();
		mouseY = (int) event.y();
	}

	@Override
	public void onMouseWheelScroll( Mouse.WheelEvent event ) {
		if( event.velocity() < 0 ) {
			setButtonState( Button.Name.MOUSE_WHEEL_UP, Button.State.JUST_PRESSED );
		} else {
			setButtonState( Button.Name.MOUSE_WHEEL_DOWN, Button.State.JUST_PRESSED );
		}
	}
	
	
	
	private class TextureImplementation extends AbstractTexture {
		private Image image;

		@Override
		public final void init() {
			image = assets().getImage( fileName );
		}


		@Override
		public ImageBuffer getBuffer() {
			ImageBuffer buffer = new ImageBuffer( imageWidth, imageHeight );
			graphics().gl20().glBindTexture( GL_TEXTURE_2D, image.ensureTexture() );
			graphics().gl20().glReadPixels( GL_CW, GL_CW, GL_CW, imageHeight, mouseX, mouseX, null);
			//GetTexImage( GL_TEXTURE_2D,  0, GL_RGBA, GL_INT, buffer.buffer );
			return buffer;
		}

		@Override
		public void applyBuffer( ImageBuffer buffer ) {
			graphics().gl20().glBindTexture( GL_TEXTURE_2D, image.ensureTexture() );
			graphics().gl20().glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR ); 
			graphics().gl20().glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR ); 
			graphics().gl20().glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, buffer.getWidth(), buffer.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer.buffer );
			imageWidth = buffer.getWidth();
			imageHeight = buffer.getHeight();
		}

		@Override
		public void pasteBuffer( ImageBuffer buffer, int x, int y ) {
			graphics().gl20().glBindTexture( GL_TEXTURE_2D, image.ensureTexture() );
			graphics().gl20().glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
			graphics().gl20().glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
			graphics().gl20().glTexSubImage2D( GL_TEXTURE_2D,  0,  x,  y,  buffer.getWidth(), buffer.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE,  buffer.buffer );
		}


		private final int[] quadIndices = { 0, 1, 2, 1, 3, 2 };
		
		@Override
		public void draw( double x, double y, double width, double height, double tx1, double ty1, double tx2, double ty2, double angle, Color color ){
				if( angle == 0d ) {
					surface.drawImage( image, (float) ( x - 0.5 * width ), (float) ( y - 0.5 * height ), (float) width, (float) height );
				} else {
					surface.
					double sin = Math.sin( angle );
					double cos = Math.cos( angle );
					float[][] vertices =  { 
						{ (float) ( x + cos *  -width - sin * -height ), (float) ( y + sin *  -width + cos * -height ) },
						{ (float) ( x + cos *  width - sin * -height ), (float) ( y + sin *  width + cos * -height ) },
						{ (float) ( x  + cos *  width - sin * height ), (float) ( y + sin *  width + cos * height ) },
						{ (float) ( x  + cos *  -width - sin * height ), (float) ( y + sin *  -width + cos * height ) }
					};
					float[][] texCoords = { { (float) tx1, (float) ty1 }, { (float) tx2, (float) ty1 }, { (float) tx2, (float) ty2 }, { (float) tx1, (float) ty2 } };
					graphics().gl20().glBindTexture( GL_TEXTURE_2D, image.ensureTexture() );
					graphics().gl20().glBindBuffer(mouseX, mouseX);
					graphics().gl20().glVertexPointer( 3, GL_FLOAT, 0, vertices);
					graphics().gl20().glNormalPointer( GL_FLOAT, 0, normals );
					graphics().gl20().glTexCoordPointer( 2, GL_FLOAT, 0, texCoords );
					graphics().gl20().glDrawArrays( GL_TRIANGLE_STRIP, 0, 4 );
				}
			}
		}
	}
	
	@Override
	public dwlab.base.images.AbstractTexture createTexture( String fileName ) {
		TextureImplementation texture = new TextureImplementation();
		texture.fileName = fileName;
		TextureImplementation.textures.add( texture );
		texture.init();
		return texture;
	}
	
	

	private class SoundImplementation extends AbstractSound {
		private final Sound sound;


		public SoundImplementation( String fileName ) {
			sound = assets().getSound( fileName );
		}


		@Override
		public void play() {
			sound.setLooping( false );
			sound.play();
		}


		@Override
		public void playLooped() {
			sound.setLooping( true );
			sound.play();
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
	public dwlab.base.AbstractSound createSound( String filename ) {
		return new SoundImplementation( filename );
	}
}