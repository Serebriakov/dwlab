/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.visualizers;

import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.platform.Platform;
import dwlab.shapes.Shape;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.drawing_shape.DrawingShape;
import java.util.LinkedList;

/**
 * This visualizer can draw collision shape, vector and name of the shape with this shape itself.
 * See also #wedgeOffWithSprite example
 */

public class DebugVisualizer extends Visualizer {
	public static DebugVisualizer instance = new DebugVisualizer();
	
	public Color colors[] = { new Color( "7FFF007F" ), new Color( "7F007FFF" ), new Color( "7F00FF7F" ), new Color( "7F7F00FF" ), new Color( "7F7FFF00" ),
			new Color( "7FFF7F00" ), new Color( "7FFFFFFF" ), new Color( "7F000000" ) };
	public int maxColor = colors.length - 1;

	public boolean showCollisionShapes = true;
	public boolean showVectors = true;
	public boolean showNames = true;
	
	public Color invisibleSpritesColor = new Color( 1d, 1d, 1d, 0.5d );
	public Color collisionShapesColor = new Color( 1d, 0d, 1d, 0.5d );


	private static Vector serviceVector = new Vector();
	private static Vector serviceSizes = new Vector();

	
	private DebugVisualizer() {
	}
	
	
	@Override
	public void drawUsingSprite( Sprite sprite, Sprite spriteShape, Color drawingColor ) {
		if( sprite.visible ) {
			sprite.visualizer.drawUsingSprite( sprite, spriteShape, Color.white );
		} else {
			sprite.visualizer.drawUsingSprite( sprite, spriteShape, invisibleSpritesColor );
		}

		Camera.current.fieldToScreen( spriteShape, serviceVector );
		Camera.current.sizeFieldToScreen( spriteShape, serviceSizes );

		if( showCollisionShapes ) sprite.drawShape( colors[ sprite.collisionLayer & maxColor ], false );

		if( showVectors ) {
			double size = Math.max( serviceSizes.x, serviceSizes.y );
			if( size != 0d ) {
				double sX2 = serviceVector.x + Math.cos( sprite.angle ) * size;
				double sY2 = serviceVector.y + Math.sin( sprite.angle ) * size;
				Platform.current.drawLine( serviceVector.x, serviceVector.y, sX2, sY2 );
				for( double d=-135; d <= 135; d += 270 ) {
					Platform.current.drawLine( sX2, sY2, sX2 + 5.0 * Math.cos( sprite.angle + d ), sY2 + 5.0 * Math.sin( sprite.angle + d ) );
				}
			}
		}

		if( showNames ) {
			String titles[] = sprite.getTitle().split( ";" );
			serviceVector.y -= titles.length * 8;
			for( String title: titles ) {
				org.newdawn.slick.Color oldContourColor = Platform.current.getContourColor();
				Platform.current.setContourColor( 0f, 0f, 0f );
				Platform.current.drawText( title, serviceVector.x - 0.5 * Platform.current.getTextWidth( title ), serviceVector.y );
				Platform.current.setContourColor( oldContourColor );
				serviceVector.y += 14;
			}
		}
	}


	@Override
	public void drawUsingTileMap( TileMap tileMap, LinkedList<Shape> shapes, Color drawingColor ) {
		tileMap.visualizer.drawUsingTileMap( tileMap, shapes, drawingColor );
		if( showCollisionShapes ) super.drawUsingTileMap( tileMap, shapes, drawingColor );
	}


	@Override
	public void drawTile( TileMap tileMap, double x, double y, double width, double height, int tileX, int tileY, Color drawingColor ) {
		Sprite[] sprites = tileMap.tileSet.collisionSprites[ tileMap.value[ tileMap.wrapY( tileY ) ][ tileMap.wrapX( tileX ) ] ];
		if( sprites == null ) return;
		double tileWidth = tileMap.getTileWidth();
		double tileHeight = tileMap.getTileHeight();
		for( Sprite sprite : sprites ) {
			DrawingShape.handlers[ sprite.shapeType.getNum() ].perform( sprite, x + ( sprite.getX() - 0.5d ) * tileWidth, y + ( sprite.getY() - 0.5d ) * tileHeight, 
					sprite.getWidth() * tileWidth, sprite.getHeight() * tileHeight, colors[ sprite.collisionLayer & maxColor ], false );
		}
	}


	public void drawCollisionSprite( TileMap tileMap, double x, double y, Sprite sprite ) {
		double tileWidth = tileMap.getTileWidth();
		double tileHeight = tileMap.getTileHeight();
	}


	@Override
	public void drawSpriteMapTile( SpriteMap spriteMap, double x, double y, Color drawingColor ) {
		int tileX = Service.floor( x / spriteMap.cellWidth ) & spriteMap.xMask;
		int tileY = Service.floor( y / spriteMap.cellHeight ) & spriteMap.yMask;
		for( int n = 0; n <= spriteMap.listSize[ tileY ][ tileX ]; n++ ) {
			drawUsingSprite( spriteMap.lists[ tileY ][ tileX ][ n ] );
		}
	}
}
