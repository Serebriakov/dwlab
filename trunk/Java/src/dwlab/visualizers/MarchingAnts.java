/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.visualizers;

import dwlab.base.images.Image;
import dwlab.base.images.ImageBuffer;
import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import java.util.LinkedList;

/**
 * This visualizer draws rectangular animated dashed frame around the shape.
 */
public class MarchingAnts extends Visualizer {
	private static Vector serviceVector = new Vector();
	private static Vector serviceSizes = new Vector();
	private static Vector serviceVector11 = new Vector();
	private static Vector serviceVector12 = new Vector();
	private static Vector serviceVector21 = new Vector();
	private static Vector serviceVector22 = new Vector();
	
	private static Image lineImage = null;
	
	
	@Override
	public void drawUsingSprite( Sprite sprite, Sprite spriteShape, Color drawingColor ) {
		if( !sprite.visible ) return;

		int pos = 7 - (int) ( ( System.currentTimeMillis() / 100 ) % 8 );

		if( Camera.current.isometric ) {
			Camera.current.fieldToScreen( spriteShape.leftX(), spriteShape.topY(), serviceVector11 );
			Camera.current.fieldToScreen( spriteShape.leftX(), spriteShape.bottomY(), serviceVector12 );
			Camera.current.fieldToScreen( spriteShape.rightX(), spriteShape.topY(), serviceVector21 );
			Camera.current.fieldToScreen( spriteShape.rightX(), spriteShape.bottomY(), serviceVector22 );

			updateLineImage();
			
			double width = Service.distance( serviceVector12.x - serviceVector11.x, serviceVector12.y - serviceVector11.y );
			if( width > 0 ) {
				drawMALine( serviceVector11.x, serviceVector11.y, serviceVector12.x, serviceVector12.y, pos );
				drawMALine( serviceVector22.x, serviceVector22.y, serviceVector21.x, serviceVector21.y, pos );
			}

			double height = Service.distance( serviceVector21.x - serviceVector11.x, serviceVector21.y - serviceVector11.y );
			if( height > 0 ) {
				drawMALine( serviceVector12.x, serviceVector12.y, serviceVector22.x, serviceVector22.y, pos );
				drawMALine( serviceVector21.x, serviceVector21.y, serviceVector11.x, serviceVector11.y, pos );
			}
		} else {
			Camera.current.fieldToScreen( spriteShape, serviceVector );
			Camera.current.sizeFieldToScreen( spriteShape.getWidth() * xScale, spriteShape.getHeight() * yScale, serviceSizes );
			drawMARect( (int) serviceVector.x, (int) serviceVector.y, (int) serviceSizes.x, (int) serviceSizes.y, pos );
		}
	}



	@Override
	public void drawUsingTileMap( TileMap tileMap, LinkedList shapes, Color drawingColor ) {
		if( !tileMap.visible ) return;
		Sprite sprite = new Sprite();
		sprite.jumpTo( tileMap );
		sprite.setSize( tileMap.getWidth(), tileMap.getHeight() );
		drawUsingSprite( sprite );
	}



	/**
	 * Draws voluntary marching ants rectangle.
	 */
	public static void drawMARect( double x, double y, double width, double height, int pos ) {
		updateLineImage();
		
		double width2 = 0.5 * width;
		double height2 = 0.5 * height;
		
		lineImage.draw( 0, Math.floor( x - width2 ), Math.floor( y - height2), Math.floor( width ), 1d, pos, 0, (int) width + pos, 1, Color.white );
		pos += width;
		lineImage.draw( 0, Math.floor( x + width2 ), Math.floor( y - height2), 1d, Math.floor( height ), 0, pos, 1, (int) height + pos, Color.white );
		pos += height;
		lineImage.draw( 0, Math.floor( x + width2 ), Math.floor( y + height2), -Math.floor( width ), 1d, pos, 0, (int) width + pos, 1, Color.white );
		pos += width;
		lineImage.draw( 0, Math.floor( x - width2 ), Math.floor( y + height2), 1d, -Math.floor( height ), 0, pos, 1, (int) height + pos, Color.white );
	}
	
	
	private static void updateLineImage() {
		if( lineImage == null ) {
			ImageBuffer buffer = new ImageBuffer( 8, 8 );
			buffer.clear( 0xFF );
			for( int n = 0; n < 4; n++ ) {
				buffer.setPixel( n, 0, 0xFFFFFFFF );
				buffer.setPixel( 0, n, 0xFFFFFFFF );
			}
			lineImage = buffer.toImage();
		}
	}
	
	
	public static void drawMALine( double x1, double y1, double x2, double y2, int pos ) {
		double length = Service.distance( x2 - x1, y2 - y1 );
		lineImage.draw( 0, 0.5d * ( x1 + x2 ), 0.5d * ( y1 + y2 ), length, 1d, pos, 0, (int) length + pos, 1, Color.white );
	}
}