/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base;

import dwlab.shapes.sprites.Camera;
import dwlab.visualizers.Color;
import java.awt.Font;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

public class Graphics {
	private static Color currentColor = Color.white.clone();
	private static Color currentClearingColor = Color.black.clone();
	private static double lineWidth = 1.0d;
	private static int width, height;
	private static int viewportX, viewportY;
	private static int viewportWidth, viewportHeight;
	private static TrueTypeFont currentFont;
	
	/**
	* Sets graphics mode.
	* Provide width and height of screen in pixels and unit size in pixels for camera.

	* @see #parallax example
	*/
	public static void init( int newWidth, int newHeight, double unitSize, boolean loadFont ) {
		width =newWidth;
		height = newHeight;
		try {
			Display.setDisplayMode( new DisplayMode( width, height ) );
			Display.create();
		} catch ( LWJGLException ex ) {
			Logger.getLogger( Graphics.class.getName() ).log( Level.SEVERE, null, ex );
		}

		glShadeModel( GL_SMOOTH );
		glEnable( GL_TEXTURE_2D ); 
		glDisable( GL_DEPTH_TEST );
		glDisable( GL_LIGHTING ); 
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glMatrixMode( GL_PROJECTION) ;
		glLoadIdentity();
		glOrtho( 0d, width, height, 0d, -1d, 1d );
		glMatrixMode( GL_MODELVIEW ) ;
		
		resetViewport();

		try {
			Mouse.create();
		} catch ( LWJGLException ex ) {
			Logger.getLogger( Graphics.class.getName() ).log( Level.SEVERE, null, ex );
		}
		
		Camera.current.viewport.setCoords( 0.5d * width, 0.5d * height );
		Camera.current.viewport.setSize( width, height );
		Camera.current.setSize( width / unitSize, height / unitSize );
		
		if( loadFont ) setCurrentFont( loadFont( "res/font.ttf", 16 ) );
	}

	public static void init() {
		init( 800, 600, 25d, true );
	}
	
	
	public static boolean initialized() {
		return width == 0 ? false : true;
	}
	
	public static int getScreenWidth() {
		return width;
	}
	
	public static int getScreenHeight() {
		return height;
	}
	
	
	public static void setColor( double red, double green, double blue, double alpha ) {
		currentColor.set( red, green, blue, alpha );
	}
	
	public static void setClearingColor( double red, double green, double blue, double alpha ) {
		currentClearingColor.set( red, green, blue, alpha );
	}
	
	public static void setLineWidth( double width ) {
		lineWidth = width;
	}
	

	public static void drawLine( double x1, double y1, double x2, double y2, double width, Color color ) {
		glBlendFunc(GL_ONE, GL_ZERO );
		glColor4d( color.red, color.green, color.blue, color.alpha );
		glBegin( GL_LINES );
			glVertex2d( x1, y1 );
			glVertex2d( x2, y2 );
		glEnd();		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void drawLine( double x1, double y1, double x2, double y2 ) {
		drawLine( x1, y1, x2, y2, lineWidth, currentColor );
	}
	
	
	public static void drawRectangle( double x, double y, double width, double height, double angle, Color color, boolean empty ){
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
			glColor4d( color.red, color.green, color.blue, color.alpha );
			glBlendFunc(GL_ONE, GL_ZERO );
			glRectd( x - width, y - height, x + width, y + height );
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
	}
	
	public static void drawRectangle( double x, double y, double width, double height ){
		drawRectangle( x, y, width, height, 0d, currentColor, false );
	}
	
	public static void drawEmptyRectangle( double x, double y, double width, double height ){
		drawRectangle( x, y, width, height, 0d, currentColor, true );
	}
	
	
	public static void drawOval( double x, double y, double width, double height, double angle, Color color, boolean empty ){
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
	
	public static void drawOval( double x, double y, double width, double height, double angle ){
		drawOval( x, y, width, height, angle, currentColor, false );
	}
	
	public static void drawEmptyOval( double x, double y, double width, double height, double angle ){
		drawOval( x, y, width, height, angle, currentColor, true );
	}
	
	public static void drawOval( double x, double y, double width, double height ){
		drawOval( x, y, width, height, 0d, currentColor, false );
	}
	
	public static void drawEmptyOval( double x, double y, double width, double height ){
		drawOval( x, y, width, height, 0d, currentColor, true );
	}
	

	public static void drawLongOval( double x, double y, double width, double height, double angle, Color color, boolean empty ) {
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
	
	public static void drawLongOval( double x, double y, double width, double height ){
		drawLongOval( x, y, width, height, 0d, currentColor, false );
	}
	
	public static void drawEmptyLongOval( double x, double y, double width, double height ){
		drawLongOval( x, y, width, height, 0d, currentColor, true );
	}
	

	public static void startPolygon( int vertexQuantity, Color color, boolean empty ) {
		glBlendFunc(GL_ONE, GL_ZERO );
		glColor4d( color.red, color.green, color.blue, color.alpha );
		if( empty ) glBegin( GL_LINE_LOOP ); else glBegin( GL_POLYGON );
	}

	public static void addPolygonVertex( double x, double y ) {
		glVertex2d( x, y );
	}

	public static void drawPolygon() {
		glEnd();
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	
	public static TrueTypeFont loadFont( String fileName, float size ) {
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream( fileName );
			Font font = Font.createFont( Font.TRUETYPE_FONT, inputStream );
			font = font.deriveFont( size ); // set font size
			return new TrueTypeFont( font, false );
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void setCurrentFont( TrueTypeFont font ) {
		currentFont = font;
	}
	
	public static void drawText( String string, double x, double y, Color color, Color contourColor ) {
	}

	public static void drawText( String string, double x, double y, Color color ) {
		currentFont.drawString( (float) x, (float) y, string );
	}
	
	public static void drawText( String string, double x, double y ) {
		drawText( string, x, y, currentColor );
	}

	private static Vector serviceVector = new Vector();
	
	public static void drawText( String text, double x, double y, Align horizontalAlign, Align verticalAlign, Color color, boolean contour ) {
		Camera.current.fieldToScreen( x, y, serviceVector );

		double textWidth = Graphics.getTextWidth( text );
		double textHeight = Graphics.getTextHeight();

		switch( horizontalAlign ) {
			case TO_CENTER:
				serviceVector.x -= 0.5d * textWidth;
				break;
			case TO_RIGHT:
				serviceVector.x -= textWidth;
				break;
		}

		switch( verticalAlign ) {
			case TO_CENTER:
				serviceVector.y -= 0.5d * textHeight;
				break;
			case TO_BOTTOM:
				serviceVector.y -= textHeight;
				break;
		}

		if( contour ) {
			drawTextWithContour( text, serviceVector.x, serviceVector.y );
		} else {
			drawText( text, serviceVector.x, serviceVector.y, color );
		}
	}

	public static void drawText( String text, double x, double y, Align horizontalAlign, Align verticalAlign ) {
		drawText( text, x, y, horizontalAlign, verticalAlign, Color.white, false );
	}

	public static void drawTextWithContour( String text, double x, double y ) {
		for( int dY=-1; dY <= 1; dY++ ) {
			for( int dX=Math.abs( dY ) - 1; dX <= 1 - Math.abs( dY ); dX++ ) {
				drawText( text, x + dX, y + dY, Color.white );
			}
		}
		drawText( text, x, y, Color.black );
	}
	
	public static double getTextWidth( String text ) {
		return currentFont.getWidth( text );
	}

	public static double getTextHeight() {
		return currentFont.getHeight();
	}
	

	public static void clearScreen() {
		clearScreen( currentClearingColor );
	}
	
	public static void clearScreen( Color color ) {
		glClearColor( (float) color.red, (float) color.green, (float) color.blue, 1.0f );
		glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT );
		glClearDepth(1);
	}
	

	public static void getViewport( Vector pivot, Vector size ) {
		pivot.x = viewportX;
		pivot.y = viewportY;
		size.x = viewportWidth;
		size.y = viewportHeight;
	}

	public static void setViewport( int x, int y, int width, int height ) {
		viewportX = x;
		viewportY = y;
		viewportWidth = width;
		viewportHeight = height;
		glViewport( x - width / 2, y - height / 2, width, height );
	}

	public static void setViewport( Vector pivot, Vector size ) {
		setViewport( Service.round( pivot.x ), Service.round( pivot.y ), Service.round( size.x ), Service.round( size.y ) );
	}

	public static void resetViewport() {
		setViewport( getScreenWidth() / 2, getScreenHeight() / 2, getScreenWidth(), getScreenHeight() );
	}
	

	public static void switchBuffers() {
		Display.update();
	}
}


