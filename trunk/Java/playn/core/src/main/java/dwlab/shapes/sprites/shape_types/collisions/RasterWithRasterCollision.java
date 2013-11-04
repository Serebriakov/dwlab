/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.base.images.Image;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;

public class RasterWithRasterCollision extends SpritesCollision {
	public static RasterWithRasterCollision instance = new RasterWithRasterCollision();

	@Override
	public boolean check( Sprite raster1, Sprite raster2 ) {
		Visualizer visualizer1 = raster1.visualizer;
		Visualizer visualizer2 = raster2.visualizer;
		Image image1 = visualizer1.image;
		Image image2 = visualizer2.image;
		if( image1 == null || image2 == null ) return false;
		if( raster1.angle == 0d && raster2.angle == 0d && raster1.getWidth() * image2.getWidth() == raster2.getWidth() * image2.getWidth() &&
				raster1.getHeight() * image2.getHeight() == raster2.getHeight() * image2.getHeight() ) {
			double xScale = image1.getWidth() / raster1.getWidth();
			double yScale = image1.getHeight() / raster1.getHeight();
			/*return Image.collide( image1, raster1.getX() * xScale, raster1.getY() * yScale, raster1.frame, 
					image2, raster2.getX() * xScale, raster2.getY() * yScale, raster2.frame );*/
			return false;
		} else {
			double xScale1 = image1.getWidth() / raster1.getWidth();
			double yScale1 = image1.getHeight() / raster1.getHeight();
			double xScale2 = image2.getWidth() / raster2.getWidth();
			double yScale2 = image2.getHeight() / raster2.getHeight();
			double xScale = Math.max( xScale1, xScale2 );
			double yScale = Math.max( yScale1, yScale2 );
			/*return Image.collide( image1, raster1.getX() * xScale, raster1.getY() * yScale, raster1.frame, raster1.angle, 
					xScale / xScale1, yScale / yScale1, image2, raster2.getX() * xScale, raster2.getY() * yScale, 
					raster2.frame, raster2.angle, xScale / xScale2, yScale / yScale2 );*/
			return false;
		}
	}
}
