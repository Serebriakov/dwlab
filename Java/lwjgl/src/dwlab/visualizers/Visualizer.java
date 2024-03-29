/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.visualizers;

import dwlab.base.Project;
import dwlab.base.XMLObject;
import dwlab.base.images.Image;
import dwlab.base.service.Service;
import dwlab.base.service.Service.Margins;
import dwlab.base.service.Vector;
import static dwlab.platform.Functions.*;
import dwlab.shapes.Shape;
import dwlab.shapes.Shape.Facing;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.drawing_sprites.DrawingSprite;
import java.util.LinkedList;

/**
 * Visualizer is object which contains parameters for drawing the shape.
 */
public class Visualizer extends Color {
	/**
	 * Horizontal shift of displaying image from the center of drawing shape in units .
	 * @see #setDXDY
	 */
	public double dX = 0d;

	/**
	 * Vertical shift of displaying image from the center of drawing shape in units .
	 * @see #setDXDY
	 */
	public double dY = 0d;

	/**
	 * Horizontal scaling of displaying image relative to the width of the drawing shape.
	 * @see #setVisualizerScale
	 */
	public double xScale = 1d;

	/**
	 * Vertical scaling of displaying image relative to the height of the drawing shape.
	 * @see #setVisualizerScale
	 */
	public double yScale = 1d;

	/**
	 * Scaling flag.
	 * If False then image will be drawn with no scaling at all.
	 */
	public boolean scaling = true;

	/**
	 * Rotating flag.
	 * If False then Angle parameter will not be used.
	 */
	public boolean rotating = true;

	/**
	 * Image field.
	 */
	public Image image;

	// ==================== Creating ====================

	public Visualizer() {
		this.set( 1.0d, 1.0d, 1.0d, 1.0d );
	}
	
	/**
	 * Creates new visualizer using given RGB components and transparency.
	 * @return New color.
	 * @see #fromHex
	 */
	public Visualizer( double red, double green, double blue, double alpha ) {
		this.set( red, green, blue, alpha );
	}
	
	public Visualizer( double red, double green, double blue ) {
		this.set( red, green, blue, 1.0d );
	}
	
	
	/**
	 * Creates new visualizer from image file.
	 * @return New visualizer.
	 * @see #fromImage, #fromColor, #fromHexColor
	 */
	public Visualizer( String filename, int xCells, int yCells ) {
		this.image = new Image( filename, xCells, yCells );
	}
	
	public Visualizer( String filename ) {
		this.image = new Image( filename, 1, 1 );
	}


	/**
	 * Creates new visualizer from existing image (LTImage).
	 * @return New visualizer.
	 * @see #fromFile, #fromRGBColor, #fromHexColor
	 */
	public Visualizer( Image image ) {
		this.image = image;
	}


	/**
	 * Creates new visualizer using given color RGB components and transparency.
	 * @return New visualizer.
	 * @see #fromFile, #fromImage, #fromHexColor
	 */
	public Visualizer( double red, double green, double blue, double alpha, double scale, boolean scaling ) {
		this.set( red, green, blue, alpha );
		this.setVisualizerScales( scale );
		this.scaling = scaling;
	}


	/**
	 * Creates new visualizer using given hexadecimal color representation and transparency.
	 * @return New visualizer.
	 * @see #fromFile, #fromImage, #fromRGBColor, #overlaps example.
	 */
	public Visualizer( String hexColor, double scale, boolean scaling ) {
		this.set( hexColor );
		this.setVisualizerScales( scale );
		this.scaling = scaling;
	}

	// ==================== Parameters ====================

	/**
	 * Sets shifts of the visualizer.
	 * Works only with images.
	 */
	public void setDXDY( double newDX, double newDY ) {
		dX = newDX;
		dY = newDY;
	}


	/**
	 * Sets vertical and horizontal scaling of the visualizer
	 * Works only with images.
	 * 
	 * @see #clone example
	 */
	public void setVisualizerScale( double newXScale, double newYScale ) {
		xScale = newXScale;
		yScale = newYScale;
	}


	/**
	 * Sets scalings of the visualizer to one value
	 * Works only with images.
	 */
	public final void setVisualizerScales( double newScale ) {
		xScale = newScale;
		yScale = newScale;
	}


	public Image getImage() {
		return image;
	}


	public void setImage( Image newImage ) {
		image = newImage;
	}

	// ==================== Drawing ===================	}


	private static Vector servicePivot = new Vector();
	private static Vector serviceSizes = new Vector();

	/**
	 * Draws given sprite using this visualizer.
	 * Change this method if you are making your own visualizer.
	 */
	public void drawUsingSprite( Sprite sprite, Sprite spriteShape, Color drawingColor ) {
		if( !sprite.visible ) return;

		if( debug ) Project.spritesDisplayed += 1;
		
		DrawingSprite.handlers[ sprite.shapeType.getNum() ].perform( this, sprite, spriteShape, drawingColor );
	}

	public void drawUsingSprite( Sprite sprite ) {
		drawUsingSprite( sprite, sprite, Color.white );
	}



	/**
	 * Draws rectangle for isometric camera using given field coordinates and size.
	 */
	public void drawIsoRectangle( double x, double y, double width, double height, Color color ) {
		startPolygon( 4, color, false );
		Camera.current.fieldToScreen( x - 0.5 * width, y - 0.5 * height, servicePivot );
		addPolygonVertex( servicePivot.x, servicePivot.y );
		Camera.current.fieldToScreen( x - 0.5 * width, y + 0.5 * height, servicePivot );
		addPolygonVertex( servicePivot.x, servicePivot.y );
		Camera.current.fieldToScreen( x + 0.5 * width, y + 0.5 * height, servicePivot );
		addPolygonVertex( servicePivot.x, servicePivot.y );
		Camera.current.fieldToScreen( x + 0.5 * width, y - 0.5 * height, servicePivot );
		addPolygonVertex( servicePivot.x, servicePivot.y );
		drawPolygon();
	}



	/**
	 * Draws oval for isometric camera using given field coordinates and size.
	 */
	public void drawIsoOval( double x, double y, double width, double height, Color color ) {
		startPolygon( 8, color, false );
		double xRadius = 0.5 * width;
		double yRadius = 0.5 * height;
		for( int n=0; n < 16; n += 2 ) {
			double angle = 22.5 * n;
			Camera.current.fieldToScreen( x + xRadius * Math.cos( angle ), y + yRadius * Math.sin( angle ), servicePivot );
			addPolygonVertex( servicePivot.x, servicePivot.y );
		}
		drawPolygon();
	}


	private static Vector servicePivot1 = new Vector();
	private static Vector servicePivot2 = new Vector();
	
	/**
	 * Draws given line using this visualizer.
	 * Change this method if you are making your own visualizer.
	 */
	public void drawUsingLineSegment( LineSegment lineSegment, Color drawingColor ) {
		if( ! lineSegment.visible ) return;
		Camera.current.fieldToScreen( lineSegment.pivot[ 0 ].getX(), lineSegment.pivot[ 0 ].getY(), servicePivot1 );
		Camera.current.fieldToScreen( lineSegment.pivot[ 1 ].getX(), lineSegment.pivot[ 1 ].getY(), servicePivot2 );
		drawLine( servicePivot1.x, servicePivot1.y, servicePivot2.x, servicePivot2.y, 1d, multiplyBy( drawingColor ) );
	}


	private static Margins margins = new Margins();
	
	/**
	 * Draws given tilemap using this visualizer.
	 * Change this method if you are making your own visualizer.
	 */
	public void drawUsingTileMap( TileMap tileMap, LinkedList<Shape> shapes, Color drawingColor ) {
		if( !tileMap.visible ) return;

		TileSet tileSet = tileMap.tileSet;
		if( tileSet == null ) return;

		Image tileSetImage = tileSet.image;

		double cellWidth = tileMap.getTileWidth();
		double cellHeight = tileMap.getTileHeight();

		Service.getEscribedRectangle( tileMap.leftMargin, tileMap.topMargin, tileMap.rightMargin, tileMap.bottomMargin, margins );

		double cornerX = tileMap.leftX();
		double cornerY = tileMap.topY();
		int minTileX = Service.floor( ( margins.min.x - cornerX ) / cellWidth );
		int minTileY = Service.floor( ( margins.min.y - cornerY ) / cellHeight );
		int maxTileX = Service.ceil( ( margins.max.x - cornerX ) / cellWidth );
		int maxTileY = Service.ceil( ( margins.max.y - cornerY ) / cellHeight );

		if( !tileMap.wrapped ) {
			minTileX = Service.limit( minTileX, 0, tileMap.xQuantity - 1 );
			minTileY = Service.limit( minTileY, 0, tileMap.yQuantity - 1 );
			maxTileX = Service.limit( maxTileX, 0, tileMap.xQuantity - 1 );
			maxTileY = Service.limit( maxTileY, 0, tileMap.yQuantity - 1 );
		}

		int tileDX = tileMap.horizontalOrder, tileDY = tileMap.verticalOrder;
		double sDX = cellWidth * tileDX, sDY = cellHeight * tileDY;

		Camera.current.sizeFieldToScreen( cellWidth, cellHeight, serviceSizes );

		if( !Camera.current.isometric ) if ( tileSetImage == null ) return;

		Color color = multiplyBy( drawingColor );
		
		int tileY;
		if( tileDY == 1 ) tileY = minTileY; else tileY = maxTileY;
		double y = cornerY + cellHeight * ( 0.5 + tileY );
		while( true ) {
			if( tileDY == 1 ) {
				if( tileY > maxTileY ) break;
			} else {
				if( tileY < minTileY ) break;
			}

			int tileX;
			if( tileDX == 1 ) tileX = minTileX; else tileX = maxTileX;

			double x = cornerX + cellWidth * ( 0.5 + tileX );

			while( true ) {
				if( tileDX == 1 ) {
					if( tileX > maxTileX ) break;
				} else {
					if( tileX < minTileX ) break;
				}

				if( shapes != null ) {
					for( Shape shape: shapes ) {
						TileMap childTileMap = shape.toTileMap();
						if( childTileMap != null ) {
							drawTile( childTileMap, x, y, serviceSizes.x, serviceSizes.y, tileX, tileY, color );
						} else {
							drawSpriteMapTile( shape.toSpriteMap(), x, y, color );
						}
					}
				} else {
					drawTile( tileMap, x, y, serviceSizes.x, serviceSizes.y, tileX, tileY, color );
				}

				x += sDX;
				tileX += tileDX;
			}

			y+= sDY;
			tileY += tileDY;
		}
	}
	
	public void drawUsingTileMap( TileMap tileMap ) {
		drawUsingTileMap( tileMap, null, Color.white );
	}



	/**
	 * Draws tile of given tilemap with given coordinates using this visualizer.
	 * X and Y are center coordinates of this tile on the screen.
	 * If you want to make your own tilemap visualizer, make class which extends LTVisualizer and rewrite this method.
	 * 
	 * @see #drawUsingTileMap
	 */
	public void drawTile( TileMap tileMap, double x, double y, double width, double height, int tileX, int tileY, Color drawingColor ) {
		TileSet tileSet =tileMap.tileSet;
		int tileValue = getTileValue( tileMap, tileX, tileY );
		if( tileValue == tileSet.emptyTile ) return;

		Image tileSetImage = tileSet.image;
		if( tileSetImage == null ) return;

		Camera.current.fieldToScreen( x, y, servicePivot );

		Visualizer visualizer = tileMap.visualizer;
		width *= visualizer.xScale;
		height *= visualizer.yScale;

		tileSetImage.draw( tileValue, servicePivot.x + visualizer.dX * width, servicePivot.y + visualizer.dY * height, width, height, 0d, drawingColor );

		if( debug ) Project.tilesDisplayed += 1;
	}


	/**
	 * Function which defines which tile to draw.
	 * @return Tile number for given tilemap and tile coordinates.
	 */
	public int getTileValue( TileMap tileMap, int tileX, int tileY ) {
		return tileMap.value[ tileMap.wrapY( tileY ) ][ tileMap.wrapX( tileX ) ];
	}


	public void drawSpriteMapTile( SpriteMap spriteMap, double x, double y, Color drawingColor ) {
		if( !spriteMap.visible ) return;
		int tileX = Service.floor( x / spriteMap.cellWidth ) & spriteMap.xMask;
		int tileY = Service.floor( y / spriteMap.cellHeight ) & spriteMap.yMask;
		for( int n=0; n <= spriteMap.listSize[ tileY ][ tileX ]; n++ ) {
			spriteMap.lists[ tileY ][ tileX ][ n ].draw( drawingColor );
		}
	}


	public Facing getFacing() {
		return xScale < 0 ? Facing.LEFT : Facing.RIGHT;
	}


	public void setFacing( Facing newFacing ) {
		xScale = Math.abs( xScale ) * ( newFacing == Facing.LEFT ? -1d : 1d );
	}

	// ==================== Other ====================

	/**
	 * Clones the visualizer.
	 * @return Clone of the visualizer.
	 */
	@Override
	public Visualizer clone() {
		Visualizer visualizer = new Visualizer();
		copyVisualizerTo( visualizer );
		return visualizer;
	}



	public void copyVisualizerTo( Visualizer visualizer ) {
		copyColorTo( visualizer );

		visualizer.dX = dX;
		visualizer.dY = dY;
		visualizer.xScale = xScale;
		visualizer.yScale = yScale;
		visualizer.scaling = scaling;
		visualizer.rotating = rotating;
		visualizer.image = image;
	}



	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		dX = xMLObject.manageDoubleAttribute( "dx", dX );
		dY = xMLObject.manageDoubleAttribute( "dy", dY );
		xScale = xMLObject.manageDoubleAttribute( "xscale", xScale, 1.0 );
		yScale = xMLObject.manageDoubleAttribute( "yscale", yScale, 1.0 );
		scaling = xMLObject.manageBooleanAttribute( "scaling", scaling, true );
		rotating = xMLObject.manageBooleanAttribute( "rotating", rotating, true );
		image = xMLObject.manageObjectField( "image", image );
	}
}
