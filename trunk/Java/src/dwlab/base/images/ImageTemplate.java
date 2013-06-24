/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base.images;

import dwlab.base.Obj;
import dwlab.base.Sys;
import dwlab.base.XMLObject;
import dwlab.visualizers.Color;
import java.util.HashSet;

public abstract class ImageTemplate extends Obj {
	public static boolean loadImages = true;
	private static HashSet<Image> images = new HashSet<Image>(); 

	String fileName;
	int frameWidth, frameHeight;
	int xCells = 1, yCells = 1;


	/**
	 * Returns frames quantity of given image.
	 * @return Frames quantity of given image.
	 */
	public int framesQuantity() {
		return xCells * yCells;
	}


	/**
	 * Returns width of image.
	 * @return Width of image in pixels.
	 */
	public int getWidth() {
		return frameWidth;
	}

	/**
	 * Returns height of image.
	 * @return Height of image in pixels.
	 */
	public int getHeight() {
		return frameHeight;
	}

	
	public int getXCells() {
		return xCells;
	}
	
	public int getYCells() {
		return yCells;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		fileName = xMLObject.manageStringAttribute( "filename", fileName );
		xCells = xMLObject.manageIntAttribute( "xcells", xCells, 1 );
		yCells = xMLObject.manageIntAttribute( "ycells", yCells, 1 );

		if( Sys.xMLGetMode() ) fileName = objectFileName.substring( 0, objectFileName.lastIndexOf( "/" ) ) + "/" + fileName;
		if( Sys.xMLGetMode() && loadImages ) init();
	}
	
	
	public abstract void draw( int frame, double x, double y, double width, double height, double angle, Color color );
	
	public void draw( int frame, double x, double y, double width, double height, double angle ){
		draw( frame, x, y, width, height, angle, Color.white );
	}
	
	public void draw( int frame, double x, double y, double width, double height ){
		draw( frame, x, y, width, height, 0d, Color.white );
	}
	
	public void draw( int frame, double x, double y ){
		draw( frame, x, y, getWidth(), getHeight(), 0, Color.white );
	}
	
	public void draw( double x, double y ){
		draw( 0, x, y, getWidth(), getHeight(), 0, Color.white );
	}
	

	public void drawAsLine( int frame, double x1, double y1, double x2, double y2, Color color ) {
		draw( frame, 0.5d * ( x1 + x2 ), 0.5d * ( y1 + y2 ), getWidth(), getHeight(), Math.atan2( y2 - y1, x2 - x1 ), color );
	}
	
	public void drawAsLine( double x1, double y1, double x2, double y2 ) {
		draw( 0, 0.5d * ( x1 + x2 ), 0.5d * ( y1 + y2 ), getWidth(), getHeight(), Math.atan2( y2 - y1, x2 - x1 ), Color.white );
	}

	
	public boolean collides( int frame1, double x1, double y1, Image image2, int frame2, double x2, double y2 ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
	
	public boolean collides( int frame1, double x1, double y1, double width1, double height1,
			Image image2, int frame2, double x2, double y2, double width2, double height2 ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	public boolean collides( int frame1, double x1, double y1, double width1, double height1, double angle1,
			Image image2, int frame2, double x2, double y2, double width2, double height2, double angle2 ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
}
