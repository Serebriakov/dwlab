/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dwlab.platform;

import dwlab.base.AbstractSound;
import dwlab.base.images.Font;
import dwlab.base.images.AbstractTexture;
import dwlab.base.service.IntVector;
import dwlab.controllers.Button;
import dwlab.controllers.ButtonAction;
import dwlab.visualizers.Color;
import java.util.LinkedList;

/**
 *
 * @author Admin
 */
public class Functions {
	public static Platform currentPlatform;
	
	public static String version = "2.0";
	public static LinkedList<ButtonAction> controllers = new LinkedList<ButtonAction>();
	public static LinkedList<Integer> keys = new LinkedList<Integer>();
	
	public static final boolean debug = true;
	
	public static Color currentColor = Color.white;
	public static Color currentClearingColor = Color.black;
	public static double lineWidth = 1.0d;
	public static int mouseX, mouseY;
	static int screenWidth = 0, screenHeight = 0;
	static int viewportX, viewportY;
	static int viewportWidth, viewportHeight;
	
	public static Font currentFont;
	public static double currentTextSize = 1d;
	public static Color currentTextColor =  Color.white;
	
	public static boolean initialized() {
		return screenWidth != 0;
	}
	
	
	public static int getScreenWidth() {
		return screenWidth;
	}
	
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	
	public static double getTextWidth( String text ) {
		return currentFont.getWidth( text );
	}
	
	public static double getTextHeight() {
		return currentFont.getHeight();
	}
	
	public static void setTextColor( double red, double green, double blue, double alpha ) {
		currentTextColor = new Color( red, green, blue, alpha );
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
	
	
	public static void drawLine( double x1, double y1, double x2, double y2, double width, Color color ) {
		currentPlatform.drawLine( x1, y1, x2, y2, width, color );
	}
	
	public static void drawLine( double x1, double y1, double x2, double y2, Color color ) {
		currentPlatform.drawLine( x1, y1, x2, y2, lineWidth, color );
	}
	
	public static void drawLine( double x1, double y1, double x2, double y2 ) {
		currentPlatform.drawLine( x1, y1, x2, y2, lineWidth, currentColor );
	}
	
	public static void drawRectangle( double x, double y, double width, double height, double angle, Color color, boolean empty ) {
		currentPlatform.drawRectangle( x, y, width, height, angle, color, empty );
	}
	
	public static void drawRectangle( double x, double y, double width, double height ) {
		currentPlatform.drawRectangle( x, y, width, height, 0d, currentColor, false );
	}
	
	public static void drawEmptyRectangle( double x, double y, double width, double height ) {
		currentPlatform.drawRectangle( x, y, width, height, 0d, currentColor, true );
	}
		
	public static void drawOval( double x, double y, double width, double height, double angle, Color color, boolean empty ) {
		currentPlatform.drawOval( x, y, width, height, angle, color, empty );
	}
	
	public static void drawOval( double x, double y, double width, double height, double angle ) {
		currentPlatform.drawOval( x, y, width, height, angle, currentColor, false );
	}
	
	public static void drawEmptyOval( double x, double y, double width, double height, double angle ) {
		currentPlatform.drawOval( x, y, width, height, angle, currentColor, true );
	}
	
	public static void drawOval( double x, double y, double width, double height ) {
		currentPlatform.drawOval( x, y, width, height, 0d, currentColor, false );
	}
	
	public static void drawEmptyOval( double x, double y, double width, double height ) {
		currentPlatform.drawOval( x, y, width, height, 0d, currentColor, true );
	}
	

	public static void drawLongOval( double x, double y, double width, double height, double angle, Color color, boolean empty ) {
		currentPlatform.drawLongOval( x, y, width, height, angle, color, empty );
	}
	
	public static void drawLongOval( double x, double y, double width, double height ) {
		currentPlatform.drawLongOval( x, y, width, height, 0d, currentColor, false );
	}
	
	public static void drawEmptyLongOval( double x, double y, double width, double height ) {
		currentPlatform.drawLongOval( x, y, width, height, 0d, currentColor, true );
	}
	

	public static void startPolygon( int vertexQuantity, Color color, boolean empty ) {
		currentPlatform.startPolygon( vertexQuantity, color, empty );
	}
	
	public static void startPolygon( int vertexQuantity, boolean empty ) {
		currentPlatform.startPolygon( vertexQuantity, currentColor, empty );
	}
	
	public static void startPolygon( int vertexQuantity ) {
		currentPlatform.startPolygon( vertexQuantity, currentColor, false );
	}

	public static void addPolygonVertex( double x, double y ) {
		currentPlatform.addPolygonVertex( x, y );
	}

	public static void drawPolygon() {
		currentPlatform.drawPolygon();
	}


	public static void drawText( String string, double x, double y ) {
		currentFont.print( string, x, y, currentTextSize );
	}	
	
	
	public static void clearScreen( Color color ) {
		currentPlatform.clearScreen( color );
	}
		
	public static void clearScreen() {
		currentPlatform.clearScreen( currentClearingColor );
	}

	public static void setViewport( int vX, int vY, int vWidth, int vHeight ) {
		currentPlatform.setViewport( vX, vY, vWidth, vHeight );
	}

	public static void swapBuffers() {
		currentPlatform.swapBuffers();
	}
	
	public static void getViewport( IntVector pivot, IntVector size ) {
		pivot.x = viewportX;
		pivot.y = viewportY;
		size.x = viewportWidth;
		size.y = viewportHeight;
	}

	public static void resetViewport() {
		currentPlatform.setViewport( screenWidth / 2, screenHeight / 2, screenWidth, screenHeight );
	}
	
	
	public static int getChar() {
		return currentPlatform.getChar();
	}

	public static void flushControllers() {
		currentPlatform.flushControllers();
	}

	public static void waitForKey() {
		currentPlatform.waitForKey();
	}
	
	public static Button createButton( Button.Name name ) {
		return currentPlatform.createButton( name );
	}
	
	

	public static AbstractTexture createTexture( String filename ) {
		return currentPlatform.createTexture( filename );
	}
	
	public static AbstractSound createSound( String filename ) {
		return currentPlatform.createSound( filename );
	}
}
