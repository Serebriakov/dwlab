/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.shapes.maps;

import dwlab.base.service.Service;
import dwlab.base.service.Service.Margins;
import dwlab.base.XMLObject;
import static dwlab.platform.Functions.*;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.util.*;

/**
 * Sprite map is a structure which can contain sprites, draw and perform collision checks between them and other shapes.
 * Operations like drawing and checking collision between large groups of sprites will be faster with use of collision maps.
 */
public class SpriteMap extends Map {
	public HashSet<Sprite> sprites = new HashSet<Sprite>();
	public Sprite lists[][][];
	public int listSize[][];

	/**
	 * Width of sprite map cell in units.
	 * @see #setCellSize
	 */
	public double cellWidth = 1d;

	/**
	 * Height of sprite map cell in units.
	 * @see #setCellSize
	 */
	public double cellHeight = 1d;

	/**
	 * Margins for drawing sprite map in units.
	 * When drawing sprite map, margins define the size of rectangular frame around camera's rectangle in which objects will be also drawn.
	 * Will be handy if you draw sprite map with objects with XScale / YScale parameters greater than 1.0.
	 */
	public double leftMargin, rightMargin, topMargin, bottomMargin;

	/**
	 * Flag which defines will be the sprite map sorted by sprite Y coordinates.
	 * False by default.
	 */
	public boolean sorted = false;

	/**
	 * Flag which defines will be all objects recognized as pivots.
	 * False by default.
	 */
	public boolean pivotMode = false;

	public int initialArraysSize = 8;


	@Override
	public int wrapX( int value ) {
		return value & xMask;
	}


	@Override
	public int wrapY( int value ) {
		return value & yMask;
	}


	@Override
	public String getClassTitle() {
		return "Sprite map";
	}

	
	@Override
	public SpriteMap toSpriteMap() {
		return this;
	}


	/**
	 * Creates collision map.
	 * You should specify cell quantities and size.
	 * 
	 * @see #createForShape
	 */
	public SpriteMap( int xQuantity, int yQuantity, double cellWidth, double cellHeight, boolean sorted, boolean pivotMode ) {
		this.setResolution( xQuantity, yQuantity );
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.sorted = sorted;
		this.pivotMode = pivotMode;
	}

	/**
	 * Creates collision map using existing shape.
	 * Collision map with size not less than shape size will be created. You can specify cell size either.
	 * Use this function ob layer bounds or layer tilemap which are covers all level to maximize performance.
	 */
	public SpriteMap( Shape shape, double cellSize, boolean sorted, boolean pivotMode ) {
		this.setResolution( Service.toPowerOf2( (int) Math.ceil( shape.getWidth() / cellSize ) ), Service.toPowerOf2( (int) Math.ceil( shape.getHeight() / cellSize ) ) );
		this.cellWidth = cellSize;
		this.cellHeight = cellSize;
		this.sorted = sorted;
		this.pivotMode = pivotMode;
	}
	
	public SpriteMap( Shape shape, double cellSize ) {
		this.setResolution( Service.toPowerOf2( (int) Math.ceil( shape.getWidth() / cellSize ) ), Service.toPowerOf2( (int) Math.ceil( shape.getHeight() / cellSize ) ) );
		this.cellWidth = cellSize;
		this.cellHeight = cellSize;
	}
	
	public SpriteMap() {
	}

	// ==================== Parameters ====================

	@Override
	public final void setResolution( int newXQuantity, int newYQuantity ) {
		super.setResolution( newXQuantity, newYQuantity );

		if( debug ) if( ! masked ) error( "Map resoluton must be power of 2" );

		lists = new Sprite[ newYQuantity ][][];
		listSize = new int[ newYQuantity ][];
		for( int yy = 0; yy < newYQuantity; yy++ ) {
			lists[ yy ] = new Sprite[ newXQuantity ][];
			listSize[ yy ] = new int[ newXQuantity ];
			for( int xx = 0; xx < newXQuantity; xx++ ) lists[ yy ][ xx ] = new Sprite[ initialArraysSize ];
		}
	}


	/**
	 * Sets cell size of sprite map.
	 */
	public void setCellSize( double newCellWidth, double newCellHeight ) {
		cellWidth = newCellWidth;
		cellHeight = newCellHeight;
	}


	/**
	 * Sets all margins to single value.
	 */
	public void setBorder( double border ) {
		setMargins( border, border, border, border );
	}


	/**
	 * Sets margins of the map.
	 */
	public void setMargins( double newLeftMargin, double newTopMargin, double newRightMargin, double newBottomMargin ) {
		leftMargin = newLeftMargin;
		topMargin = newTopMargin;
		rightMargin = newRightMargin;
		bottomMargin = newBottomMargin;
	}

	// ==================== Drawing ===================	

	/**
	 * Draws all objects of sprite map which are in cells under camera's rectangle plus margins.
	 */
	@Override
	public void draw( Color drawingColor ) {
		drawUsingVisualizer( null, drawingColor );
	}


	Margins serviceMargins = new Margins();
	
	@Override
	public void drawUsingVisualizer( Visualizer vis, Color drawingColor ) {
		if( visible ) {
			Sprite.checkNum++;
			
			Service.getEscribedRectangle( leftMargin, topMargin, rightMargin, bottomMargin, serviceMargins );

			int mapX1 = Service.floor( serviceMargins.min.x / cellWidth );
			int mapY1 = Service.floor( serviceMargins.min.y / cellHeight );
			int mapX2 = Service.floor( serviceMargins.max.x / cellWidth );
			int mapY2 = Service.floor( serviceMargins.max.y / cellHeight );

			int xN[] = new int[ mapX2 - mapX1 + 1 ];

			for( int yy = mapY1; yy <= mapY2; yy++ ) {
				int maskedY = yy & yMask;
				if( sorted ) {
					double maxY = ( yy + 1 ) * cellHeight;
					while( true ) {
						double minY = 0;
						int storedX = 0;
						Sprite storedSprite = null;
						for( int xx = 0; xx <= mapX2 - mapX1; xx++ ) {
							int maskedX = xx & yMask;
							int n = xN[ xx ];
							if( n >= listSize[ maskedY ][ maskedX ] ) continue;
							Sprite sprite = lists[ maskedY ][ maskedX ][ n ];
							double y0 = sprite.getY();
							if( y0 >= maxY ) continue;
							if( storedSprite == null || y0 < minY ) {
								minY = y0;
								storedX = xx;
								storedSprite = sprite;
							}
						}
						if( storedSprite == null ) break;

						if( storedSprite.checkValue != Sprite.checkNum ) {
							if( vis != null ) {
								vis.drawUsingSprite( storedSprite, storedSprite, drawingColor );
							} else {
								storedSprite.draw( drawingColor );
							}
							storedSprite.checkValue = Sprite.checkNum;
						}

						xN[ storedX ] += 1;
					}
				} else {
					for( int xx = mapX1; xx <= mapX2; xx++ ) {
						int maskedX = xx & xMask;
						Sprite array[] = lists[ maskedY ][ maskedX ];
						for( int n = 0; n < listSize[ maskedY ][ maskedX ]; n++ ) {
							Sprite sprite = array[ n ];
							if( sprite.checkValue != Sprite.checkNum ) {
								if( vis != null ) {
									sprite.drawUsingVisualizer( vis, drawingColor );
								} else {
									sprite.draw( drawingColor );
								}
								sprite.checkValue = Sprite.checkNum;
							}
						}
					}
				}
			}
		}
	}

	// ==================== Insert / remove objects ====================

	/**
	 * Inserts a sprite into sprite map
	 * When PivotMode is set to True, insertion will be faster.
	 * 
	 * @see #removeSprite
	 */
	public void insertSprite( Sprite sprite, boolean changeSpriteMapField, boolean modifySet ) {
		if( modifySet ) sprites.add( sprite );
		if( pivotMode ) {
			insertSprite( sprite, ( (int) Service.round( sprite.getX() / cellWidth ) ) & xMask, ( (int) Service.round( sprite.getY() / cellHeight ) ) & yMask );
		} else {
			int mapX1 = Service.floor( ( sprite.getX() - 0.5d * sprite.getWidth() ) / cellWidth );
			int mapY1 = Service.floor( ( sprite.getY() - 0.5d * sprite.getHeight() ) / cellHeight );
			int mapX2 = Service.floor( ( sprite.getX() + 0.5d * sprite.getWidth() - inaccuracy ) / cellWidth );
			int mapY2 = Service.floor( ( sprite.getY() + 0.5d * sprite.getHeight() - inaccuracy ) / cellHeight );

			for( int yy = mapY1; yy <= mapY2; yy++ ) {
				for( int xx = mapX1; xx <= mapX2; xx++ ) {
					insertSprite( sprite, xx & xMask, yy & yMask );
				}
			}
		}
		if( changeSpriteMapField ) sprite.spriteMap = this;
	}

	public void insertSprite( Sprite sprite ) {
		insertSprite( sprite, true, true );
	}


	public void insertSprite( Sprite sprite, int mapX, int mapY ) {
		Sprite array[] = lists[ mapY ][ mapX ];
		int size = listSize[ mapY ][ mapX ];
		if( array.length == size ) {
			array = Arrays.copyOf( array, size * 2 );
			lists[ mapY ][ mapX ] = array;
		}

		array[ size ] = sprite;
		if( sorted ) {
			for( int n=0; n <= size; n++ ) {
				if( sprite.getY() < array[ n ].getY() ) {
					for( int m=size - 1; m <= n; m += -1 ) array[ m + 1 ] = array[ m ];
					array[ n ] = sprite;
					break;
				}
			}
		} else {
			array[ size ] = sprite;
		}

		listSize[ mapY ][ mapX ] += 1;
	}



	/**
	 * Removes sprite from sprite map.
	 * When PivotMode is set to True, removal will be faster.
	 * 
	 * @see #insertSprite
	 */
	public void removeSprite( Sprite sprite, boolean changeSpriteMapField, boolean modifySet ) {
		if( modifySet ) sprites.remove( sprite );
		if( pivotMode ) {
			removeSprite( sprite, ( (int) Service.round( sprite.getX() / cellWidth ) ) & xMask, ( (int) Service.round( sprite.getY() / cellHeight ) ) & yMask );
		} else {
			int mapX1 = Service.floor( ( sprite.getX() - 0.5d * sprite.getWidth() ) / cellWidth );
			int mapY1 = Service.floor( ( sprite.getY() - 0.5d * sprite.getHeight() ) / cellHeight );
			int mapX2 = Service.floor( ( sprite.getX() + 0.5d * sprite.getWidth() - inaccuracy ) / cellWidth );
			int mapY2 = Service.floor( ( sprite.getY() + 0.5d * sprite.getHeight() - inaccuracy ) / cellHeight );

			for( int yy = mapY1; yy <= mapY2; yy++ ) {
				for( int xx = mapX1; xx <= mapX2; xx++ ) {
					removeSprite( sprite, xx & xMask, yy & yMask );
				}
			}
		}
		if( changeSpriteMapField ) sprite.spriteMap = null;
	}
	
	public void removeSprite( Sprite sprite ) {
		removeSprite( sprite, true, true );
	}



	public void removeSprite( Sprite sprite, int mapX, int mapY ) {
		Sprite array[] = lists[ mapY ][ mapX ];
		int size = listSize[ mapY ][ mapX ];
		for( int n = 0; n <= size; n++ ) {
			if( array[ n ] == sprite ) {
				if( sorted ) {
					for( int m=n + 1; m <= size; m++ ) array[ m - 1 ] = array[ m ];
				} else {
					array[ n ] = array[ size - 1 ];
				}
				listSize[ mapY ][ mapX ] -= 1;
				return;
			}
		}
	}


	/**
	 * Clears sprite map.
	 */
	public void clear() {
		sprites.clear();
		for( int yy = 0; yy <= yQuantity; yy++ ) {
			for( int xx = 0; xx <= xQuantity; xx++ ) {
				listSize[ yy ][ xx ] = 0;
			}
		}
	}

	// ==================== Shape management ====================

	@Override
	public Shape load() {
		SpriteMap newSpriteMap = loadShape().toSpriteMap();
		for( Sprite childSprite: sprites ) {
			newSpriteMap.insertSprite( childSprite.loadShape().toSprite(), true, true );
		}
		return newSpriteMap;
	}


	@Override
	public Shape findShape( String parameterName, String parameterValue ) {
		super.findShape( parameterName, parameterValue );
		for( Shape childShape: sprites ) {
			Shape shape = childShape.findShape( parameterName, parameterValue );
			if( shape != null ) return shape;
		}
		return null;
	}


	@Override
	public Shape findShape( Class shapeClass ) {
		super.findShape( shapeClass );
		for( Shape childShape: sprites ) {
			Shape shape = childShape.findShape( shapeClass );
			if( shape != null ) return shape;
		}
		return null;
	}


	@Override
	public Shape findShape( String parameterName, String parameterValue, Class shapeClass ) {
		super.findShape( parameterName, parameterValue, shapeClass );
		for( Shape childShape: sprites ) {
			Shape shape = childShape.findShape( parameterName, parameterValue, shapeClass );
			if( shape != null ) return shape;
		}
		return null;
	}


	@Override
	public boolean insert( Shape shape, Shape pivotShape, Relativity relativity ) {
		Sprite sprite = pivotShape.toSprite();
		if( sprite == null ) return false;
		if( sprites.contains( sprite ) ) {
			sprite = shape.toSprite();
			if( sprite != null ) sprites.add( sprite );
			return true;
		} else {
			return false;
		}
	}
	
	
	@Override
	public boolean insert( Collection<Shape> shapes, Shape pivotShape, Relativity relativity ) {
		Sprite sprite = pivotShape.toSprite();
		if( sprite == null ) return false;
		if( sprites.contains( sprite ) ) {
			for( Shape shape : shapes ) {
				sprite = shape.toSprite();
				if( sprite != null ) sprites.add( sprite );
			}
			return true;
		} else {
			return false;
		}
	}
	

	@Override
	public Shape remove( Shape shape ) {
		Sprite sprite = shape.toSprite();
		if( sprite != null ) sprites.remove( sprite );
		return this;
	}


	@Override
	public Shape remove( Class shapeClass ) {
		for ( Iterator<Sprite> iterator = sprites.iterator(); iterator.hasNext(); ) {
			Shape childSprite = iterator.next();
			if( childSprite.getClass() == shapeClass ) iterator.remove(); else childSprite.remove( shapeClass );
		}
		return this;
	}

	// ==================== Other ===================	
	
	@Override
	public void init() {
		for( Sprite sprite: sprites ) sprite.init();
	}


	@Override
	public void act() {
		for( Sprite sprite: sprites ) sprite.act();
	}


	@Override
	public void update() {
		for( Shape obj: sprites ) obj.update();
	}


	@Override
	public Shape clone() {
		SpriteMap newSpriteMap = new SpriteMap();
		copyTo( newSpriteMap );
		for( Sprite sprite: sprites ) newSpriteMap.insertSprite( sprite, true, true );
		return newSpriteMap;
	}


	@Override
	public void copyTo( Shape shape ) {
		copyShapeTo( shape );
		SpriteMap spriteMap =  shape.toSpriteMap();
		
		if( debug ) if( spriteMap == null ) error( "Trying to copy sprite map \"" + shape.getTitle() + "\" data to non-sprite-map" );

		spriteMap.setResolution( xQuantity, yQuantity );
		spriteMap.cellWidth = cellWidth;
		spriteMap.cellHeight = cellHeight;
		spriteMap.leftMargin = leftMargin;
		spriteMap.rightMargin = rightMargin;
		spriteMap.topMargin = topMargin;
		spriteMap.bottomMargin = bottomMargin;
		spriteMap.sorted = sorted;
		spriteMap.pivotMode = pivotMode;
	}


	@Override
	public int showModels( int y, String shift ) {
		if( behaviorModels.isEmpty() ) {
			if( sprites.isEmpty() ) return y;
			drawText( shift + getTitle() + " ", 0, y );
	    	y += 16;
		} else {
			y = super.showModels( y, shift );
		}
		for( Shape shape: sprites ) y = shape.showModels( y, shift + " " );
		return y;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		cellWidth = xMLObject.manageDoubleAttribute( "cell-width", cellWidth, 1d );
		cellHeight = xMLObject.manageDoubleAttribute( "cell-height", cellHeight, 1d );
		leftMargin = xMLObject.manageDoubleAttribute( "left-margin", leftMargin );
		rightMargin = xMLObject.manageDoubleAttribute( "right-margin", rightMargin );
		topMargin = xMLObject.manageDoubleAttribute( "top-margin", topMargin );
		bottomMargin = xMLObject.manageDoubleAttribute( "bottom-margin", bottomMargin );
		sorted = xMLObject.manageBooleanAttribute( "sorted", sorted );
		pivotMode = xMLObject.manageBooleanAttribute( "pivot-mode", pivotMode );
		initialArraysSize = xMLObject.manageIntAttribute( "arrays-size", initialArraysSize, 8 );

		super.xMLIO( xMLObject );

		if( XMLObject.xMLGetMode() ) {
			for( XMLObject spriteXMLObject: xMLObject.children ) insertSprite( (Sprite) spriteXMLObject.manageObject( null ), true, true );
		} else {
			for( Sprite sprite: sprites ) {
				XMLObject newXMLObject = new XMLObject();
				newXMLObject.manageObject( sprite );
				xMLObject.children.addLast( newXMLObject );
			}
		}
	}
}
