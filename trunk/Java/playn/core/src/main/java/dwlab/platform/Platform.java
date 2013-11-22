/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dwlab.platform;

import dwlab.base.Project;
import dwlab.base.AbstractSound;
import dwlab.base.images.AbstractTexture;
import dwlab.controllers.Button;
import dwlab.visualizers.Color;

public abstract class Platform {
	public abstract void init( int newWidth, int newHeight, double unitSize, boolean loadFont );
	
	
	public abstract void drawLine( double x1, double y1, double x2, double y2, double width, Color color );
	
	public abstract void drawRectangle( double x, double y, double width, double height, double angle, Color color, boolean empty );
		
	public abstract void drawOval( double x, double y, double width, double height, double angle, Color color, boolean empty );

	public abstract void drawLongOval( double x, double y, double width, double height, double angle, Color color, boolean empty );
	

	public abstract void startPolygon( int vertexQuantity, Color color, boolean empty );

	public abstract void addPolygonVertex( double x, double y );

	public abstract void drawPolygon();
	
	
	public abstract void clearScreen( Color color );

	public abstract void setViewport( int vX, int vY, int vWidth, int vHeight );

	public abstract void swapBuffers();
	
	
	public abstract int getChar();
	
	public abstract Button createButton( Button.Name name );
	
	public abstract void processEvents();

	public abstract void flushControllers();

	public abstract void waitForKey();
	
	

	public abstract AbstractTexture createTexture( String filename );
	
	public abstract AbstractSound createSound( String filename );
}
