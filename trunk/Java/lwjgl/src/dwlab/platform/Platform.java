/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dwlab.platform;

import dwlab.base.Font;
import dwlab.base.Project;
import dwlab.base.Sound;
import dwlab.base.images.Texture;
import dwlab.base.service.IntVector;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.controllers.MouseWheel;
import dwlab.shapes.sprites.Camera;
import dwlab.visualizers.Color;
import java.util.LinkedList;

public abstract class Platform {
	public static Platform current;
	
	public static String version = "2.0";
	public static LinkedList<ButtonAction> controllers = new LinkedList<>();
	public static LinkedList<Integer> keys = new LinkedList<>();
	
	public static final boolean debug = true;
	
	public static Color currentColor = Color.white;
	public static Color currentClearingColor = Color.black;
	public Font currentFont;
	public static double lineWidth = 1.0d;
	static int width, height;
	static int viewportX, viewportY;
	static int viewportWidth, viewportHeight;
	
	public abstract void init( int newWidth, int newHeight, double unitSize, boolean loadFont );
	
	public void initCamera( double unitSize ) {
		Camera.current.viewport.setCoords( 0.5d * width, 0.5d * height );
		Camera.current.viewport.setSize( width, height );
		Camera.current.setSize( width / unitSize, height / unitSize );
		Camera.current.setCoords( 0d, 0d );
	}
	
	public void initCamera() {
		initCamera( 25d );
	}
	
	public boolean initialized() {
		return width != 0;
	}
	
	public int getScreenWidth() {
		return width;
	}
	
	public int getScreenHeight() {
		return height;
	}
	
	
	public abstract double getTextWidth( String text );
	
	public abstract double getTextHeight();
	
	public abstract Color getContourColor();
	
	public abstract void setContourColor( double red, double green, double blue, double alpha );
	
	public abstract void setContourColor( Color color );
	
	public abstract Color getTextColor();
	
	public abstract void setTextColor( double red, double green, double blue, double alpha );
	
	public abstract void setTextColor( Color color );
	

	public static Color getCurrentColor() {
		return currentColor;
	}
	
	public static void setCurrentColor( double red, double green, double blue, double alpha ) {
		currentColor.set( red, green, blue, alpha );
	}
	
	public static void setCurrentColor( double red, double green, double blue ) {
		currentColor.set( red, green, blue, 1d );
	}

	public static void resetCurrentColor() {
		currentColor.set( 1d, 1d, 1d, 1d );
	}
	
	public static void setClearingColor( double red, double green, double blue, double alpha ) {
		currentClearingColor.set( red, green, blue, alpha );
	}
	
	public static void setClearingColor( double red, double green, double blue ) {
		currentClearingColor.set( red, green, blue, 1d );
	}
	
	public static void resetClearingColor() {
		currentClearingColor.set( 0d, 0d, 0d, 1d );
	}
	
	public abstract void setCurrentFont( Font font );

	public abstract void drawLine( double x1, double y1, double x2, double y2, double width, Color color );
	
	public void drawLine( double x1, double y1, double x2, double y2, Color color ) {
		drawLine( x1, y1, x2, y2, lineWidth, color );
	}
	
	public void drawLine( double x1, double y1, double x2, double y2 ) {
		drawLine( x1, y1, x2, y2, lineWidth, currentColor );
	}
	
	public abstract void drawRectangle( double x, double y, double width, double height, double angle, Color color, boolean empty );
	
	public void drawRectangle( double x, double y, double width, double height ) {
		drawRectangle( x, y, width, height, 0d, currentColor, false );
	}
	
	public void drawEmptyRectangle( double x, double y, double width, double height ) {
		drawRectangle( x, y, width, height, 0d, currentColor, true );
	}
		
	public abstract void drawOval( double x, double y, double width, double height, double angle, Color color, boolean empty );
	
	public void drawOval( double x, double y, double width, double height, double angle ) {
		drawOval( x, y, width, height, angle, currentColor, false );
	}
	
	public void drawEmptyOval( double x, double y, double width, double height, double angle ) {
		drawOval( x, y, width, height, angle, currentColor, true );
	}
	
	public void drawOval( double x, double y, double width, double height ) {
		drawOval( x, y, width, height, 0d, currentColor, false );
	}
	
	public void drawEmptyOval( double x, double y, double width, double height ) {
		drawOval( x, y, width, height, 0d, currentColor, true );
	}
	

	public abstract void drawLongOval( double x, double y, double width, double height, double angle, Color color, boolean empty );
	
	public void drawLongOval( double x, double y, double width, double height ) {
		drawLongOval( x, y, width, height, 0d, currentColor, false );
	}
	
	public void drawEmptyLongOval( double x, double y, double width, double height ) {
		drawLongOval( x, y, width, height, 0d, currentColor, true );
	}
	

	public abstract void startPolygon( int vertexQuantity, Color color, boolean empty );
	
	public void startPolygon( int vertexQuantity, boolean empty ) {
		startPolygon( vertexQuantity, currentColor, empty );
	}
	
	public void startPolygon( int vertexQuantity ) {
		startPolygon( vertexQuantity, currentColor, false );
	}

	public abstract void addPolygonVertex( double x, double y );

	public abstract void drawPolygon();

	public abstract void drawText( String string, double x, double y, Color color );
	
	public abstract void drawText( String string, double x, double y );
	
	public abstract void clearScreen( Color color );
		
	public void clearScreen() {
		clearScreen( currentClearingColor );
	}

	public abstract void setViewport( int vX, int vY, int vWidth, int vHeight );

	public abstract void swapBuffers();
	
	public void getViewport( IntVector pivot, IntVector size ) {
		pivot.x = viewportX;
		pivot.y = viewportY;
		size.x = viewportWidth;
		size.y = viewportHeight;
	}

	public void resetViewport() {
		setViewport( width / 2, height / 2, width, height );
	}
	
	
	public abstract int getChar();
	
	public abstract int getKeyboardCode( Key keyID );
	
	public abstract int mouseX();
		
	public abstract int mouseY();
	
	public abstract void processEvents( Project project );

	public abstract void flushControllers();

	public abstract boolean getPushable( ButtonAction action );

	public abstract void waitForKey();
	
	public abstract void processKeyboardKeyEvent( KeyboardKey key );
	
	public abstract void processMouseButtonEvent( MouseButton button );
	
	public abstract void processMouseWheelEvent( MouseWheel wheel );
	

	public abstract Texture createTexture( String filename );
	
	public abstract Font createFont( String fileName, float size );
	
	public abstract Sound createSound( String filename );
}
