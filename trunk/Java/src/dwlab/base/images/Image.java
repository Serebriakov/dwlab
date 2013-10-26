/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base.images;

import dwlab.platform.Platform;
import dwlab.visualizers.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
	
/**
 * Image class.
 */
public class Image extends ImageTemplate {
	private int textureID;
	private double kx = 1d, ky = 1d;
		
	public Image() {
	}
	
	/**
	 * Creates new image from file with specified cell quantities for splitting.
	 * @return New image (LTImage).
	 */
	public Image( String fileName, int xCells, int yCells ) {
		if( Platform.debug ) if( xCells <= 0 || yCells <= 0 ) error( "Cells quantity must be 1 or more" );

		this.fileName = fileName;
		this.xCells = xCells;
		this.yCells = yCells;
		this.textureID = glGenTextures();
		this.init();
	}

	public Image( String fileName ) {
		this.fileName = fileName;
		this.textureID = glGenTextures();
		this.init();
	}

	
	public final void setFields( int imageWidth, int imageHeight, int textureWidth, int textureHeight ) {
		frameWidth = imageWidth / xCells;
		frameHeight = imageHeight / yCells;
		kx = 1d * frameWidth / textureWidth;
		ky = 1d * frameHeight / textureHeight;
	}

	/**
	 * Initializes image.
	 * Splits image by XCells x YCells grid. Will be executed after loading image object from XML file.
	 */
	@Override
	public final void init() {
		if( !fileName.isEmpty() ) {
			try {
				Texture texture = TextureLoader.getTexture( fileName.substring( fileName.length() - 3 ).toUpperCase(), 
						ResourceLoader.getResourceAsStream( fileName ) );
				textureID = texture.getTextureID();
				setFields( texture.getImageWidth(), texture.getImageHeight(), texture.getTextureWidth(), texture.getTextureHeight() );
			} catch ( IOException ex ) {
				Logger.getLogger( Image.class.getName() ).log( Level.SEVERE, null, ex );
			}
		}
	}
	
	
	public ImageBuffer getBuffer() {
		ImageBuffer buffer = new ImageBuffer( getTextureWidth(), getTextureHeight() );
		glBindTexture( GL_TEXTURE_2D, textureID );
		glGetTexImage( GL_TEXTURE_2D,  0, GL_RGBA, GL_INT, buffer.buffer );
		return buffer;
	}
	
	public void applyBuffer( ImageBuffer buffer ) {
		applyBuffer( buffer, 1, 1 );
	}
	
	public void applyBuffer( ImageBuffer buffer, int xCells, int yCells ) {
		glBindTexture( GL_TEXTURE_2D, textureID );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR ); 
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR ); 
		glTexImage2D( GL_TEXTURE_2D, 0, GL_RGBA, buffer.getWidth(), buffer.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer.buffer );
		setFields( buffer.getWidth(), buffer.getHeight(), glGetTexLevelParameteri( GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH ),
				glGetTexLevelParameteri( GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT ) );
	}
	
	public void pasteBuffer( ImageBuffer buffer, int x, int y ) {
		glBindTexture( GL_TEXTURE_2D, textureID );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR );
		glTexParameteri( GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR );
		glTexSubImage2D( GL_TEXTURE_2D,  0,  x,  y,  buffer.getWidth(), buffer.getHeight(), GL_RGBA, GL_UNSIGNED_BYTE,  buffer.buffer );
		setFields( buffer.getWidth(), buffer.getHeight(), glGetTexLevelParameteri( GL_TEXTURE_2D, 0, GL_TEXTURE_WIDTH ),
				glGetTexLevelParameteri( GL_TEXTURE_2D, 0, GL_TEXTURE_HEIGHT ) );
	}
	
	
	@Override
	public void draw( int frame, double x, double y, double width, double height, double angle, Color color ){
		width *= 0.5d;
		height *= 0.5d;
		double tx = frame % xCells;
		double ty = Math.floor( frame / xCells );
		
		glBindTexture( GL_TEXTURE_2D, textureID );
		glBegin( GL_QUADS );
			glColor4d( color.red, color.green, color.blue, color.alpha );
			
			if( angle == 0d ) {
				glTexCoord2d( tx * kx, ty * ky );
				glVertex2d( x - width, y - height );
				glTexCoord2d( ( tx + 1 ) * kx, ty * ky );
				glVertex2d( x + width, y - height );
				glTexCoord2d( ( tx + 1 ) * kx, ( ty + 1 ) * ky );
				glVertex2d( x + width, y + height );
				glTexCoord2d( tx * kx, ( ty + 1 ) * ky );
				glVertex2d( x - width, y + height );
			} else {
				double sin = Math.sin( angle );
				double cos = Math.cos( angle );
				glTexCoord2d( tx * kx, ty * ky );
				glVertex2d( x + cos *  -width - sin * -height, y + sin *  -width + cos * -height );
				glTexCoord2d( ( tx + 1 ) * kx, ty * ky );
				glVertex2d( x + cos *  width - sin * -height, y + sin *  width + cos * -height );
				glTexCoord2d( ( tx + 1 ) * kx, ( ty + 1 ) * ky );
				glVertex2d( x  + cos *  width - sin * height, y + sin *  width + cos * height );
				glTexCoord2d( tx * kx, ( ty + 1 ) * ky );
				glVertex2d( x  + cos *  -width - sin * height, y + sin *  -width + cos * height );
				glVertex2d( x - width, y + height );
			}
		glEnd();
	}
	
	
	public void draw( int frame, double x, double y, double width, double height, int tx1, int ty1, int tx2, int ty2, Color color ){
		double tx = frame % xCells;
		double ty = Math.floor( frame / xCells );
		
		glBindTexture( GL_TEXTURE_2D, textureID );
		glColor4d( color.red, color.green, color.blue, color.alpha );
		glBegin( GL_QUADS );
			glColor4d( color.red, color.green, color.blue, color.alpha );
			glTexCoord2d( ( tx + tx1 * 1d / frameWidth ) * kx, ( ty + ty1 * 1d / frameHeight ) * kx );
			glVertex2d( x, y );
			glTexCoord2d( ( tx + tx2 * 1d / frameWidth ) * kx, ( ty + ty1 * 1d / frameHeight ) * ky );
			glVertex2d( x + width, y );
			glTexCoord2d( ( tx + tx2 * 1d / frameWidth ) * kx, ( ty + ty2 * 1d / frameHeight ) * ky );
			glVertex2d( x + width, y + height );
			glTexCoord2d( ( tx + tx1 * 1d / frameWidth ) * kx, ( ty + ty2 * 1d / frameHeight ) * ky );
			glVertex2d( x, y + height );
		glEnd();
	}
		
	
	@Override
	public boolean collides( int frame1, double x1, double y1, Image image2, int frame2, double x2, double y2 ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
	
	@Override
	public boolean collides( int frame1, double x1, double y1, double width1, double height1,
			Image image2, int frame2, double x2, double y2, double width2, double height2 ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}

	@Override
	public boolean collides( int frame1, double x1, double y1, double width1, double height1, double angle1,
			Image image2, int frame2, double x2, double y2, double width2, double height2, double angle2 ) {
		throw new UnsupportedOperationException( "Not yet implemented" );
	}
	

	private int getTextureWidth() {
		return xCells * frameWidth;
	}

	private int getTextureHeight() {
		return yCells * frameHeight;
	}
}
