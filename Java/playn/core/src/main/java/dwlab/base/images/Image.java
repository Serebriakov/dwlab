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
import static dwlab.base.Obj.objectFileName;
import dwlab.base.XMLObject;
import static dwlab.platform.Functions.*;
import dwlab.visualizers.Color;
import java.util.HashSet;

public class Image extends Obj {
	private static final HashSet<Image> images = new HashSet<Image>(); 

	public Texture texture;
	public String fileName;
	protected int frameWidth, frameHeight;
	protected double kx, ky;
	protected int xCells, yCells;
	

	public Image() {
		
	}
	
	public Image( String fileName ) {
		this( fileName, 1, 1 );
	}
	
	public Image( Texture texture ) {
		this.xCells = 1;
		this.yCells = 1;
		this.texture = texture;
		this.init();
	}
	
	public Image( String fileName, int xCells, int yCells ) {
		if( debug ) if( xCells <= 0 || yCells <= 0 ) error( "Cells quantity must be 1 or more" );
		
		this.fileName = fileName;
		this.xCells = xCells;
		this.yCells = yCells;
		this.init();
	}
	
	@Override
	public final void init() {
		if( !initialized() ) return;
		if( texture == null ) texture = createTexture( fileName );
		frameWidth = texture.getImageWidth() / xCells;
		frameHeight = texture.getImageHeight() / yCells;
		kx = 1d * frameWidth / texture.getTextureWidth();
		ky = 1d * frameHeight / texture.getTextureHeight();
		texture.init();
	}


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

		xCells = xMLObject.manageIntAttribute( "xcells", xCells, 1 );
		yCells = xMLObject.manageIntAttribute( "ycells", yCells, 1 );

		if( XMLObject.xMLGetMode() ) {
			String textureFileName = xMLObject.manageStringAttribute( "filename", "" );
			int lastSlash = objectFileName.lastIndexOf( "/" );
			if( lastSlash >= 0 ) textureFileName = objectFileName.substring( 0, lastSlash ) + "/" + textureFileName;
			texture = createTexture( textureFileName );
			init();
		} else {
			xMLObject.manageStringAttribute( "filename", fileName );
		}
	}
	
	
	public void draw( int frame, double x, double y, double width, double height, double angle, Color color ) {
		double tx = frame % xCells;
		double ty = Math.floor( frame / xCells );
		texture.draw( x, y, width, height, tx * kx, ty * ky, ( tx + 1 ) * kx, ( ty + 1 ) *ky, angle, color );
	}
	
	public void draw( int frame, double x, double y, double width, double height, int tx1, int ty1, int tx2, int ty2, double angle, Color color ) {
		double tx = frame % xCells;
		double ty = Math.floor( frame / xCells );
		texture.draw( x, y, width, height, ( tx + tx1 * 1d / frameWidth ) * kx, ( ty + ty1 * 1d / frameHeight ) * ky, ( tx + tx2 * 1d / frameWidth ) * kx, ( ty + ty1 * 1d / frameHeight ) * ky,
				angle, color );
	}
	
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
		return false;
	}
	
	public boolean collides( int frame1, double x1, double y1, double width1, double height1,
			Image image2, int frame2, double x2, double y2, double width2, double height2 ) {
		return false;
	}

	public boolean collides( int frame1, double x1, double y1, double width1, double height1, double angle1,
			Image image2, int frame2, double x2, double y2, double width2, double height2, double angle2 ) {
		return false;
	}
}
