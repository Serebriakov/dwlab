package dwlab.platform;

import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.controllers.MouseWheel;
import dwlab.controllers.Pushable;
import dwlab.shapes.sprites.Camera;
import dwlab.visualizers.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static  org.lwjgl.opengl.GL11.*;

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
}