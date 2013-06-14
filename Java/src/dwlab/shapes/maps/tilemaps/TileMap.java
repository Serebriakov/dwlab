/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.shapes.maps.tilemaps;

import dwlab.base.Obj;
import dwlab.base.Sys;
import dwlab.base.XMLObject;
import dwlab.base.files.BinaryFile;
import dwlab.base.service.IntVector;
import dwlab.base.service.Service;
import dwlab.shapes.Shape;
import dwlab.shapes.maps.IntMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;

/**
 * Tilemap is displayable rectangular tile-based shape with 2d array of tile indexes and tileset with tile images.
 */
public class TileMap extends IntMap {
	public static TileMapLoadingErrorHandler loadingErrorHandler = new TileMapLoadingErrorHandler();
	public static BinaryFile file;
	public static int offset = 0;
	
	/**
	 * Tilemap's default tileset.
	 * @see #lTTileSet
	 */
	public TileSet tileSet;

	/**
	 * Tiles quantity of tilemap.
	 * All map tile indexes should be in 0...TilesQuantity - 1 interval.
	 */
	public int tilesQuantity;

	/**
	 * Margins for drawing tile map in units.
	 * When drawing tile map, margins define the size of rectangular frame around camera's rectangle in which tiles will be also drawn.
	 * Will be handy if tilemap's XScale / YScale parameters are greater than 1.0.
	 */
	public double leftMargin, rightMargin, topMargin, bottomMargin;

	/**
	 * Wrapping flag.
	 * If this flag will be set to True, then map will be repeated (tiled, wrapped) enlessly in all directions.
	 */
	public boolean wrapped = false;

	/**
	 * Horizontal displaying order.
	 * 1 means drawing tile columns from left to right, -1 means otherwise.
	 */
	public int horizontalOrder = 1;

	/**
	 * Vertical displaying order.
	 * 1 means drawing tile rows from top to bottom, -1 means otherwise.
	 */
	public int verticalOrder = 1	;
	
	public long loadingTime;
	
	// ==================== Parameters ===================	

	/**
	 * Returns tilemap tile width.
	 * @return Tile width of the tilemap in units.
	 * @see #getTileHeight
	 */
	public double getTileWidth() {
			return width / xQuantity;
	}


	/**
	 * Returns tilemap tile height.
	 * @return Tile height of the tilemap in units.
	 */
	public double getTileHeight() {
		return height / yQuantity;
	}


	/**
	 * Returns tile collision shape.
	 * @return Tile collision shape of tilemap's tile with given coordinates using default tilemap tileset.
	 */
	public Sprite[] getTileCollisionSprites( int tileX, int tileY ) {
		if( tileX < 0 || tileX >= xQuantity ) error( "Incorrect tile X position" );
		if( tileY < 0 || tileY >= yQuantity ) error( "Incorrect tile Y position" );

		return tileSet.collisionSprites[ value[ tileY ][ tileX ] ];
	}


	/**
	 * Returns tile coordinates for given field coordinates.
	 * @return Tile coordinates for given point.
	 */	
	public void getTileForPoint( double x, double y, IntVector vector ) {
		vector.x = (int) Math.floor( ( x - leftX() ) / getTileWidth() );
		vector.y = (int) Math.floor( ( y - topY() ) / getTileHeight() );
	}


	@Override
	public String getClassTitle() {
		return "Tile map";
	}

	// ==================== Drawing ===================	

	@Override
	public void draw( Color drawingColor ) {
		if( visible ) visualizer.drawUsingTileMap( this, null, drawingColor );
	}



	@Override
	public void drawUsingVisualizer( Visualizer visualizer, Color drawingColor ) {
		if( visible ) visualizer.drawUsingTileMap( this, null, drawingColor );
	}

	// ==================== Other ===================	

	/**
	 * Enframes tilemap.
	 * You can specify tileset for enframing. If no tileset will be specified, tilemap's default tileset will be used.
	 * 
	 * @see #lTTileSet
	 */
	public void enframe( TileSet byTileSet ) {
		for( int yy = 0; yy < yQuantity; yy++ ) {
			for( int xx = 0; xx < xQuantity; xx++ ) {
				byTileSet.enframe( this, xx , yy );
			}
		}
	}
	
	public void enframe() {
		enframe( tileSet );
	}


	/**
	 * Returns tile index for given coordinates.
	 * @return Tile index for given tile coordinates.
	 * @see #setTile, #setAsTile example
	 */
	public int getTile( int tileX, int tileY ) {
		if( Sys.debug ) {
			if( tileX < 0 || tileX >= xQuantity ) error( "Incorrect tile X position" );
			if( tileY < 0 || tileY >= yQuantity ) error( "Incorrect tile Y position" );
		}
		return value[ tileY ][ tileX ];
	}


	/**
	 * Sets tile index for given tile coordinates.
	 * @see #getTile, #getTileForPoint example, #stretch example
	 */
	public void setTile( int tileX, int tileY, int tileNum ) {
		if( Sys.debug ) {
			if( tileNum < 0 || tileNum >= tilesQuantity ) error( "Incorrect tile number" );
			if( tileX < 0 || tileX >= xQuantity ) error( "Incorrect tile X position" );
			if( tileY < 0 || tileY >= yQuantity ) error( "Incorrect tile Y position" );
		}
		value[ tileY ][ tileX ] = tileNum;
	}


	public void swapTiles( int tileX1, int tileY1, int tileX2, int tileY2 ) {
		int z = getTile( tileX1, tileY1 );
		setTile( tileX1, tileY1, getTile( tileX2, tileY2 ) );
		setTile( tileX2, tileY2, z );
	}


	/**
	 * Refreshes tile indexes of tilemap.
	 * Execute this method after lowering tiles quantity of this tilemap or its tileset to avoid errors.
	 * Tile indexes will be limited to 0...TilesQuantity - 1 interval.
	 */
	public void refreshTilesQuantity() {
		if( tileSet == null ) return;
		if( tileSet.tilesQuantity < tilesQuantity ) {
			for( int yy = 0; yy <= yQuantity; yy++ ) {
				for( int xx = 0; xx <= xQuantity; xx++ ) {
					if( value[ yy ][ xx ] >= tileSet.tilesQuantity ) value[ yy ][ xx ] = tileSet.tilesQuantity - 1;
				}
			}
		}
		tilesQuantity = tileSet.tilesQuantity;
	}

	// ==================== Creating ===================

	public static TileMap create( TileSet tileSet, int xQuantity, int yQuantity ) {
		TileMap tileMap = new TileMap();
		tileMap.setResolution( xQuantity, yQuantity );
		tileMap.tileSet = tileSet;
		if( tileSet != null ) tileMap.tilesQuantity = tileSet.tilesQuantity;
		return tileMap;
	}

	// ==================== Cloning ===================

	@Override
	public Shape clone() {
		TileMap newTileMap = new TileMap();
		copyTileMapTo( newTileMap );
		return newTileMap;
	}


	public void copyTileMapTo( TileMap tileMap ) {
		copyShapeTo( tileMap );

		tileMap.tileSet = tileSet;
		tileMap.tilesQuantity = tilesQuantity;
		tileMap.wrapped = wrapped;
		tileMap.setResolution( xQuantity, yQuantity );
		for( int yy = 0; yy < yQuantity; yy++ ) {
			for( int xx = 0; xx < xQuantity; xx++ ) {
				tileMap.value[ yy ][ xx ] = value[ yy ][ xx ];
			}
		}
	}


	@Override
	public void copyTo( Shape shape ) {
		TileMap tileMap = shape.toTileMap();
		if( Sys.debug ) if( tileMap == null ) error( "Trying to copy tilemap \"" + shape.getTitle() + "\" data to non-tilemap" );
		copyTileMapTo( tileMap );
	}

	
	@Override
	public TileMap toTileMap() {
		return this;
	}

	// ==================== Saving / loading ===================

	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		tileSet = xMLObject.manageObjectField( "tileset", tileSet );
		tilesQuantity = xMLObject.manageIntAttribute( "tiles-quantity", tilesQuantity );
		leftMargin = xMLObject.manageDoubleAttribute( "left-margin", leftMargin );
		rightMargin = xMLObject.manageDoubleAttribute( "right-margin", rightMargin );
		topMargin = xMLObject.manageDoubleAttribute( "top-margin", topMargin );
		bottomMargin = xMLObject.manageDoubleAttribute( "bottom-margin", bottomMargin );
		wrapped = xMLObject.manageBooleanAttribute( "wrapped", wrapped );
		horizontalOrder = xMLObject.manageIntAttribute( "horizontal-order", horizontalOrder, 1 );
		verticalOrder = xMLObject.manageIntAttribute( "vertical-order", verticalOrder, 1 );
		offset = xMLObject.manageIntAttribute( "offset", offset, -1 );

		if( Sys.xMLGetMode() ) {
			long time = System.currentTimeMillis();
			if( file == null ) {
				if( offset == 0 ) loadingErrorHandler.handleError( Obj.objectFileName + "bin" );
			} else {
				file.seek( offset );
				if( tilesQuantity <= 256 ) {
					for( int yy = 0; yy < yQuantity; yy++ ) {
						for( int xx = 0; xx < xQuantity; xx++ ) {
							value[ yy ][ xx ] = file.readByte();
						}
					}
				} else if( tilesQuantity <= 65536 ) {
					for( int yy = 0; yy < yQuantity; yy++ ) {
						for( int xx = 0; xx < xQuantity; xx++ ) {
							value[ yy ][ xx ] = file.readShort();
						}
					}
				} else {
					for( int yy = 0; yy < yQuantity; yy++ ) {
						for( int xx = 0; xx < xQuantity; xx++ ) {
							value[ yy ][ xx ] = file.readInt();
						}
					}
				}
			}

			Service.objectLoadingTime += loadingTime;
			loadingTime = System.currentTimeMillis() - time;
			Service.newTotalLoadingTime += loadingTime;
			Service.loadingProgress = 1d * loadingTime / Service.totalLoadingTime;
			if( Service.loadingUpdater != null ) Service.loadingUpdater.update();
		} else {
			if( tilesQuantity <= 256 ) {
				for( int yy = 0; yy < yQuantity; yy++ ) {
					for( int xx = 0; xx < xQuantity; xx++ ) {
						file.writeByte( value[ yy ][ xx ] );
					}
				}
				offset += xQuantity * yQuantity;
			} else if( tilesQuantity <= 65536 ) {
				for( int yy = 0; yy < yQuantity; yy++ ) {
					for( int xx = 0; xx < xQuantity; xx++ ) {
						file.writeShort( value[ yy ][ xx ]);
					}
				}
				offset += 2 * xQuantity * yQuantity;
			} else {
				for( int yy = 0; yy < yQuantity; yy++ ) {
					for( int xx = 0; xx < xQuantity; xx++ ) {
						file.writeInt( value[ yy ][ xx ] );
					}
				}
				offset += 4 * xQuantity * yQuantity;
			}
		}
	}
}