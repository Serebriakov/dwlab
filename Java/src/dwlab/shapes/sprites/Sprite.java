/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites;

import dwlab.base.Project;
import dwlab.base.Sys;
import dwlab.base.XMLObject;
import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.Line;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.line_segments.collision.CollisionWithLineSegment;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.shapes.sprites.shape_types.collisions.SpritesCollision;
import dwlab.shapes.sprites.shape_types.drawing_shape.DrawingShape;
import dwlab.shapes.sprites.shape_types.overlapping.SpritesOverlapping;
import dwlab.shapes.sprites.shape_types.wedging_off.WedgingOffSprites;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.util.Iterator;

/**
 * Sprite is the main basic shape of the framework to draw, move and check collisions.
 * @see #lTVectorSprite
 */
public class Sprite extends Shape {
	private static Vector serviceVector = new Vector();
	private static Sprite serviceSprite = new Sprite();

	
	/**
	 * Type of the sprite shape.
	 * @see #pivot, #oval, #rectangle
	 */
	public ShapeType shapeType = ShapeType.rectangle;


	/**
	 * Direction of the sprite
	 * @see #moveForward, #moveTowards
	 */
	public double angle;

	/**
	 * Angle of displaying image.
	 * Displaying angle is relative to sprite's direction if visualizer's rotating flag is set to True.
	 */
	public double displayingAngle;

	/**
	 * Velocity of the sprite in units per second.
	 * @see #moveForward, #moveTowards
	 */
	public double velocity;

	/**
	 * Frame of the sprite image.
	 * Can only be used with visualizer which have images.
	 */
	public int frame;

	public SpriteMap spriteMap;

	public static int checkNum = 0;
	public int checkValue = 0;

	@Override
	public String getClassTitle() {
		return "Sprite";
	}

	// ==================== Creating ===================	

	public Sprite() {
		this.shapeType = ShapeType.pivot;
	}

	/**
	 * Creates sprite using given shape type.
	 * @return Created sprite.
	 */
	public Sprite( ShapeType shapeType ) {
		this.shapeType = shapeType;
		this.x = 0d;
		this.y = 0d;
		if( shapeType == ShapeType.pivot ) {
			this.width = 0d;
			this.height = 0d;
		} else {
			this.width = 1d;
			this.height = 1d;
		}
	}
	
	public Sprite( double x, double y ) {
		this.x = x;
		this.y = y;
		this.shapeType = ShapeType.pivot;
	}

	public Sprite( double x, double y, double radius ) {
		this.x = x;
		this.y = y;
		this.width = radius;
		this.height = radius;
		this.shapeType = ShapeType.oval;
	}

	public Sprite( double x, double y, double width, double height ) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.shapeType = ShapeType.rectangle;
	}

	public Sprite( ShapeType shapeType, double width, double height ) {
		this.width = width;
		this.height = height;
		this.shapeType = shapeType;
	}
	
	/**
	 * Creates sprite using given coordinates, size, shape type, angle and velocity.
	 * @return Created sprite.
	 * See also #overlaps example.
	 */
	public Sprite( ShapeType shapeType, double x, double y, double width, double height ) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.shapeType = shapeType;
	}
	
	public Sprite( ShapeType shapeType, double x, double y, double width, double height, double angle, double velocity ) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.shapeType = shapeType;
		this.angle = angle;
		this.velocity = velocity;
	}

	public Sprite( Shape shape ) {
		this.x = shape.getX();
		this.y = shape.getY();
		this.width = shape.getWidth();
		this.height = shape.getWidth();
	}
	
	// ==================== Drawing ===================	

	@Override
	public void draw( Color drawingColor ) {
		visualizer.drawUsingSprite( this, this, drawingColor );
	}


	@Override
	public void drawUsingVisualizer( Visualizer vis, Color drawingColor ) {
		vis.drawUsingSprite( this, this, drawingColor );
	}
	

	public void drawShape( Color drawingColor, boolean contour ) {
		DrawingShape.handlers[ shapeType.getNum() ].perform( this, drawingColor, contour );
	}

	// ==================== Collisions ===================


	/**
	 * Checks if this sprite collides with given sprite.
	 * @return True if the sprite collides with given sprite, False otherwise.
	 */
	public boolean collidesWithSprite( Sprite sprite ) {
		if( Sys.debug ) Project.collisionChecks += 1;
		int num1 = shapeType.getNum();
		int num2 = sprite.shapeType.getNum();
		if( num1 <= num2 ) {
			return SpritesCollision.handlers[ num1 ][ num2 ].check( this, sprite );
		} else {
			return SpritesCollision.handlers[ num2 ][ num1 ].check( sprite, this );
		}
	}


	/**
	 * Checks if the sprite collides with given line.
	 * @return True if the sprite collides with given line, otherwise false.
	 * Only collision of line and Oval is yet implemented.
	 */
	public boolean collidesWithLineSegment( LineSegment lineSegment ) {
		if( Sys.debug ) Project.collisionChecks += 1;
		return CollisionWithLineSegment.handlers[ shapeType.getNum() ].check( this, lineSegment );
	}


	/**
	 * Checks if the sprite overlaps given sprite.
	 * @return True if the sprite overlaps given sprite, otherwise false.
	 * Pivot overlapping is not supported.
	 */
	public boolean overlaps( Sprite sprite ) {
		if( Sys.debug ) Project.collisionChecks += 1;
		return SpritesOverlapping.handlers[ shapeType.getNum() ][ sprite.shapeType.getNum() ].check( this, sprite );
	}


	/**
	 * Searches the layer for first sprite which collides with given.
	 * @return First found sprite which collides with given.
	 * Included layers will be also checked.
	 * 
	 * @see #clone example
	 */	
	public Sprite lastCollidedSpriteOfLayer( Layer layer ) {
		for ( Iterator<Shape> it = layer.children.descendingIterator(); it.hasNext(); ) {
			Shape shape = it.next();
			if( shape != this ) {
				Sprite collided = shape.layerLastSpriteCollision( this );
				if( collided != null ) return collided;
			}
		}
		return null;
	}


	@Override
	public Sprite layerLastSpriteCollision( Sprite sprite ) {
		if( collidesWithSprite( sprite ) ) return this; else return null;
	}


	/**
	 * Executes given collision handler for collision of sprite with given sprite.
	 * If sprites collide then HandleCollisionWithSprite() method will be executed and given sprite will be passed to this method.
	 * You can specify collision type which will be passed to this method too.
	 * 
	 * @see #collisionsWithLayer, #collisionsWithTileMap, #collisionsWithLine, #collisionsWithSpriteMap, #horizontal, #vertical
	 */
	public void collisionsWithSprite( Sprite sprite, SpriteCollisionHandler handler ) {
		if( collidesWithSprite( sprite ) ) handler.handleCollision( this, sprite );
	}


	/**
	 * Executes given collision handler for collision of sprite with shapes in given group.
	 * For every collided shape collision handling method will be executed and corresponding parameters will be passed to this method.
	 * You can specify collision type which will be passed to this method too.
	 * 
	 * @see #collisionsWithSprite, #collisionsWithTileMap, #collisionsWithLine, #collisionsWithSpriteMap, #horizontal, #vertical
	 */
	public void collisionsWithLayer( Layer layer, SpriteCollisionHandler handler ) {
		for( Shape shape: layer.children ) {
			if( shape != this ) shape.spriteLayerCollisions( this, handler );
		}
	}


	@Override
	public void spriteLayerCollisions( Sprite sprite, SpriteCollisionHandler handler ) {
		if( sprite.collidesWithSprite( this ) ) handler.handleCollision( sprite, this );
	}


	/**
	 * Executes given collision handler for collision of sprite with given line.
	 * If sprite collides with line then HandleCollisionWithLine() method will be executed and line will be passed to this method.
	 * You can specify collision type which will be passed to this method too.
	 * 
	 * @see #collisionsWithLayer, #collisionsWithSprite, #collisionsWithTileMap, #collisionsWithSpriteMap, #horizontal, #vertical
	 */
	public void collisionsWithLineSegment( LineSegment lineSegment, SpriteAndLineSegmentCollisionHandler handler ) {
		if( collidesWithLineSegment( lineSegment ) ) handler.handleCollision( this, lineSegment );
	}


	public static final double smallNum = 0.00000000001;
	
	/**
	 * Executes given collision handler for collision of sprite with tiles in given tilemap.
	 * For every collided tile HandleCollisionWithTile() method will be executed and tilemap with tile indexes will be passed to this method.
	 * You can specify collision type which will be passed to this method too.
	 * 
	 * @see #collisionsWithLayer, #collisionsWithSprite, #collisionsWithLine, #collisionsWithSpriteMap, #horizontal, #vertical, #lTVectorSprite example
	 */
	public void collisionsWithTileMap( TileMap tileMap, SpriteAndTileCollisionHandler handler ) {
		double x0 = tileMap.leftX();
		double y0 = tileMap.topY();
		double cellWidth = tileMap.getTileWidth();
		double cellHeight = tileMap.getTileHeight();
		int xQuantity = tileMap.xQuantity;
		int yQuantity = tileMap.yQuantity;
		Sprite[][] collisionSprites = tileMap.tileSet.collisionSprites;

		if( shapeType == ShapeType.pivot ) {
			int tileX = Service.floor( ( x - x0 ) / cellWidth );
			int tileY = Service.floor( ( y - y0 ) / cellHeight );

			if( tileX >= 0 && tileY >= 0 && tileX < xQuantity && tileY < yQuantity ) {
				Sprite[] spriteArray = collisionSprites[ tileMap.value[ tileY ][ tileX ] ];
				if( spriteArray != null ) {
					for( int n = 0; n < spriteArray.length; n++ ) {
						Sprite sprite = spriteArray[ n ];
						sprite.transformFrom( x0, y0, cellWidth, cellHeight, tileX, tileY );
						if( collidesWithSprite( serviceSprite ) ) handler.handleCollision( this, tileMap, tileX, tileY, sprite );
					}
				}
			}
		} else if( shapeType != ShapeType.ray ) {
			int x1 = Service.floor( ( x - 0.5d * width - x0 ) / cellWidth );
			int y1 = Service.floor( ( y - 0.5d * height - y0 ) / cellHeight );
			int x2 = Service.floor( ( x + 0.5d * width - x0 - smallNum ) / cellWidth );
			int y2 = Service.floor( ( y + 0.5d * height - y0 - smallNum ) / cellHeight );

			if( x2 >= 0 && y2 >= 0 && x1 < xQuantity && y1 < yQuantity ) {
				x1 = Service.limit( x1, 0, xQuantity - 1 );
				y1 = Service.limit( y1, 0, yQuantity - 1 );
				x2 = Service.limit( x2, 0, xQuantity - 1 );
				y2 = Service.limit( y2, 0, yQuantity - 1 );

				for( int tileY = y1; tileY < y2; tileY++ ) {
					for( int tileX = x1; tileX < x2; tileX++ ) {
						Sprite[] spriteArray = collisionSprites[ tileMap.value[ tileY ][ tileX ] ];
						if( spriteArray != null ) {
							for( Sprite sprite : spriteArray ) {
								sprite.transformFrom( x0, y0, cellWidth, cellHeight, tileX, tileY );
								if( collidesWithSprite( serviceSprite ) ) handler.handleCollision( this, tileMap, tileX, tileY, sprite );
							}
						}
					}
				}
			}
		}
	}


	/**
	 * Executes reaction for collision of sprite with sprites in sprite map.
	 * For every collided sprite HandleCollisionWithSprite() method will be executed and collided srite will be passed to this method.
	 * You can specify collision type which will be passed to this method too.
	 * Map parameter allows you to specify map to where collided sprites will be added as keys.
	 * 
	 * @see #collisionsWithGroup, #collisionsWithSprite, #collisionsWithTileMap, #collisionsWithLine, #horizontal, #vertical, #lTSpriteMap example
	 */
	public void collisionsWithSpriteMap( SpriteMap spriteMap, SpriteCollisionHandler handler ) {
		checkNum++;
		if( shapeType == ShapeType.pivot ) {
			int wrappedCellX = ( (int) Service.floor( x / spriteMap.cellWidth ) ) & spriteMap.xMask;
			int wrappedCellY = ( (int) Service.floor( y / spriteMap.cellHeight ) ) & spriteMap.yMask;
			Sprite[] sprites = spriteMap.lists[ wrappedCellY ][ wrappedCellX ];
			int quantity = spriteMap.listSize[ wrappedCellY ][ wrappedCellX ];
			for( int n = 0; n < quantity; n++ ) {
				Sprite mapSprite = sprites[ n ];
				if( this == mapSprite ) continue;
				if( collidesWithSprite( mapSprite ) ) {
					if( mapSprite.checkValue != checkNum ) {
						mapSprite.checkValue = checkNum;
						handler.handleCollision( this, mapSprite );
					}
				}
			}
		} else if( shapeType != ShapeType.ray ) {
			int mapX1 = Service.floor( ( x - 0.5d * width ) / spriteMap.cellWidth );
			int mapY1 = Service.floor( ( y - 0.5d * height ) / spriteMap.cellHeight );
			int mapX2 = Service.floor( ( x + 0.5d * width - smallNum ) / spriteMap.cellWidth );
			int mapY2 = Service.floor( ( y + 0.5d * height - smallNum ) / spriteMap.cellHeight );

			for( int cellY = mapY1; cellY <= mapY2; cellY++ ) {
				int wrappedCellY = cellY & spriteMap.yMask;
				for( int cellX = mapX1; cellX <= mapX2; cellX++ ) {
					int wrappedCellX = cellX & spriteMap.xMask;
					Sprite[] sprites = spriteMap.lists[ wrappedCellY ][ wrappedCellX ];
					int quantity = spriteMap.listSize[ wrappedCellY ][ wrappedCellX ];
					for( int n = 0; n < quantity; n++ ) {
						Sprite mapSprite = sprites[ n ];
						if( this == mapSprite ) continue;
						if( collidesWithSprite( mapSprite ) ) {
							if( mapSprite.checkValue != checkNum ) {
								mapSprite.checkValue = checkNum;
								handler.handleCollision( this, mapSprite );
							}
						}
					}
				}
			}
		}
	}

	// ==================== Wedging off ====================

	/**
	 * Wedges off sprite with given sprite.
	 * Pushes sprites from each other until they stops colliding. More the moving resistance, less the sprite will be moved.
	 * <ul>
	 * <li> If each sprite's moving resistance is zero, or each sprite's moving resistance is less than 0 then sprites will be moved on same distance.
	 * <li> If one of the sprite has zero moving resistance and other's moving resistance is non-zero, only zero-moving-resistance sprite will be moved
	 * <li> If one of the sprite has moving resistance less than 0 and other has moving resistance more or equal to 0, then only zero-or-more-moving-resistance sprite will be moved.
	 * </ul>
	 */
	public void wedgeOffWithSprite( Sprite sprite, double selfMovingResistance, double spriteMovingResistance ) {
		int num1 = shapeType.getNum();
		int num2 = sprite.shapeType.getNum();
		if( num1 <= num2 ) {
			WedgingOffSprites.handlers[ num1 ][ num2 ].calculateVector( this, sprite, serviceVector );
			WedgingOffSprites.separate( this, sprite, serviceVector, selfMovingResistance, spriteMovingResistance );
		} else {
			WedgingOffSprites.handlers[ num2 ][ num1 ].calculateVector( sprite, this, serviceVector );
			WedgingOffSprites.separate( sprite, this, serviceVector, spriteMovingResistance, selfMovingResistance );
		}
	}
	
	public void wedgeOffWithSprite( Sprite sprite ) {
		wedgeOffWithSprite( sprite, 0.5d, 0.5d );
	}


	/**
	 * Pushes sprite from given sprite.
	 * See also : #wedgeOffWithSprite, #pushFromTile
	 */
	public void pushFromSprite( Sprite sprite ) {
		wedgeOffWithSprite( sprite, 0.0, 1.0 );
	}


	/**
	 * Pushes sprite from given tile.
	 * See also : #pushFromSprite
	 */
	public void pushFromTile( TileMap tileMap, int tileX, int tileY ) {
		double cellWidth = tileMap.getTileWidth();
		double cellHeight = tileMap.getTileHeight();
		double x0 = tileMap.leftX() + cellWidth * tileX;
		double y0 = tileMap.topY() + cellHeight * tileY;
		Sprite[] spriteArray = tileMap.tileSet.collisionSprites[ tileMap.value[ tileY ][ tileX ] ];
		if( spriteArray != null ) {
			for( int n = 0; n < spriteArray.length; n++ ) {
				Sprite sprite = spriteArray[ n ];
				sprite.transformFrom( x0, y0, cellWidth, cellHeight, tileX, tileY );
				pushFromSprite( sprite );
			}
		}
	}


	/**
	 * Forces sprite to bounce off the inner bounds of the shape.
	 * @see #active example
	 */
	public Sprite bounceInside( Sprite sprite, boolean leftSide, boolean topSide, boolean rightSide, boolean bottomSide, SpriteCollisionHandler handler ) {
		if( leftSide ) {
			if( leftX() < sprite.leftX() ) {
				x = sprite.leftX() + 0.5 * width;
				angle = 180 - angle;
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		if( topSide ) {
			if( topY() < sprite.topY() ) {
				y = sprite.topY() + 0.5 * height;
				angle = -angle;
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		if( rightSide ) {
			if( rightX() > sprite.rightX() ) {
				x = sprite.rightX() - 0.5 * width;
				angle = 180 - angle;
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		if( bottomSide ) {
			if( bottomY() > sprite.bottomY() ) {
				y = sprite.bottomY() - 0.5 * height;
				angle = -angle;
				if( handler != null ) handler.handleCollision( this, sprite );
			}
		}
		return this;
	}
	
	public Sprite bounceInside( Sprite sprite, SpriteCollisionHandler handler ) {
		bounceInside( sprite, true, true, true, true, handler );
		return this;
	}
	
	public Sprite bounceInside( Sprite sprite ) {
		bounceInside( sprite, true, true, true, true, null );
		return this;
	}

	// ==================== Position and size ====================

	@Override
	public Shape setCoords( double newX, double newY ) {
		if( spriteMap != null ) spriteMap.removeSprite( this, false );

		x = newX;
		y = newY;

		update();
		if( spriteMap != null ) spriteMap.insertSprite( this, false );
		return this;
	}


	@Override
	public Shape setCoordsAndSize( double x1, double y1, double x2, double y2 ) {
		if( spriteMap != null ) spriteMap.removeSprite( this, false );

		x = 0.5d * ( x1 + x2 );
		y = 0.5d * ( y1 + y2 );
		width = x2 - x1;
		height = y2 - y1;

		update();
		if( spriteMap != null ) spriteMap.insertSprite( this, false );
		return this;
	}


	/**
	 * Moves sprite forward.
	 * @see #move, #moveBackward, #turn example
	 */
	public Sprite moveForward() {
		setCoords( x + Math.cos( angle ) * velocity * Project.deltaTime, y + Math.sin( angle ) * velocity * Project.deltaTime );
		return this;
	}


	/**
	 * Moves sprite backward.
	 * @see #move, #moveForward, #turn example
	 */
	public Sprite moveBackward() {
		setCoords( x - Math.cos( angle ) * velocity * Project.deltaTime, y - Math.sin( angle ) * velocity * Project.deltaTime );
		return this;
	}


	@Override
	public Shape setSize( double newWidth, double newHeight ) {
		if( spriteMap != null ) spriteMap.removeSprite( this, false );

		width = newWidth;
		height = newHeight;

		update();
		if( spriteMap != null ) spriteMap.insertSprite( this, false );
		return this;
	}


	/**
	 * Sets the sprite as a tile.
	 * Position, size, visualizer and frame will be changed. This method can be used to cover other shapes with the tile or voluntary moving the tile.
	 * 
	 * @see #getTileForPoint example
	 */
	public Sprite setAsTile( TileMap tileMap, int tileX, int tileY ) {
		width = tileMap.getTileWidth();
		height = tileMap.getTileHeight();
		x = tileMap.leftX() + width * ( 0.5 + tileX );
		y = tileMap.topY() + height * ( 0.5 + tileY );
		visualizer = tileMap.visualizer.clone();
		visualizer.image = tileMap.tileSet.image;
		frame = tileMap.getTile( tileX, tileY );
		return this;
	}

	// ==================== Limiting ====================

	@Override
	public Shape limitLeftWith( Shape rectangle, SpriteCollisionHandler handler ) {
		double rectLeftX = rectangle.leftX();
		if( leftX() < rectLeftX ) {
			setX( rectLeftX + 0.5 * width );
			if( handler != null ) handler.handleCollision( this, null );
		}
		return this;
	}


	@Override
	public Shape limitTopWith( Shape rectangle, SpriteCollisionHandler handler ) {
		double rectTopY = rectangle.topY();
		if( topY() < rectTopY ) {
			setY( rectTopY + 0.5 * height );
			if( handler != null ) handler.handleCollision( this, null );
		}
		return this;
	}


	@Override
	public Shape limitRightWith( Shape rectangle, SpriteCollisionHandler handler ) {
		double rectRightX = rectangle.rightX();
		if( rightX() > rectRightX ) {
			setX( rectRightX - 0.5 * width );
			if( handler != null ) handler.handleCollision( this, null );
		}
		return this;
	}


	@Override
	public Shape limitBottomWith( Shape rectangle, SpriteCollisionHandler handler ) {
		double rectBottomY = rectangle.bottomY();
		if( bottomY() > rectBottomY ) {
			setY( rectBottomY - 0.5 * height );
			if( handler != null ) handler.handleCollision( this, null );
		}
		return this;
	}

	// ==================== Angle ====================

	/**
	 * Directs sprite as given angular sprite. 
	 * @see #directTo
	 */
	public Sprite directAs( Sprite sprite ) {
		angle = sprite.angle;
		return this;
	}


	/**
	 * Turns the sprite.
	 * Turns the sprite with given speed per second.
	 */
	public Sprite turn( double turningSpeed ) {
		angle += Project.deltaTime * turningSpeed;
		return this;
	}


	/**
	 * Direct the sprite to center of the given shape.
	 * @see #directAs
	 */
	public Sprite directTo( Shape shape ) {
		angle = Math.atan2( shape.getY() - y, shape.getX() - x );
		return this;
	}


	public Sprite reverseDirection() {
		angle = angle + 180;
		return this;
	}


	/**
	 * Alters angle by given value.
	 * @see #clone example
	 */
	public Sprite alterAngle( double dAngle ) {
		angle += dAngle;
		return this;
	}

	// ==================== Animation ====================

	/**
	 * Animates the sprite.
	 */
	public Sprite animate( double speed, int framesQuantity, int frameStart, double startingTime, boolean pingPong ) {
		if( framesQuantity == 0 ) framesQuantity = visualizer.getImage().framesQuantity();
		int modFactor = framesQuantity;
		if( pingPong ) modFactor = framesQuantity * 2 - 2;
		frame =  ( int ) Math.floor( ( Project.current.time - startingTime ) / speed ) % modFactor;
		if( pingPong && frame >= framesQuantity ) frame = modFactor - frame;
		frame += frameStart;
		return this;
	}

	// ==================== Methods for oval ====================	

	public Sprite toCircle( Sprite pivot1, Sprite circleSprite ) {
		if( width == height ) return this;
		if( circleSprite !=null ) circleSprite = new Sprite( ShapeType.oval );
		if( width > height ) {
			circleSprite.x = Service.limit( pivot1.x, x - 0.5 * ( width - height ), x + 0.5 * ( width - height ) );
			circleSprite.y = y;
			circleSprite.width = height;
			circleSprite.height = height;
		} else {
			circleSprite.x = x;
			circleSprite.y = Service.limit( pivot1.y, y - 0.5 * ( height - width ), y + 0.5 * ( height - width ) );
			circleSprite.width = width;
			circleSprite.height = width;
		}
		return circleSprite;
	}


	public Sprite toCircleUsingLine( Line line, Sprite circleSprite ) {
		if( width == height ) return circleSprite;
		if( width > height ) {
			double dWidth = 0.5 * ( width - height );
			double o1 = line.a * ( x - dWidth ) + line.b * y + line.c;
			double o2 = line.a * ( x + dWidth ) + line.b * y + line.c;
			if( Math.signum( o1 ) != Math.signum( o2 ) ) {
				circleSprite.x = -( line.b * y + line.c ) / line.a;
			} else if( Math.abs( o1 ) < Math.abs( o2 ) ) {
				circleSprite.x = x - dWidth;
			} else {
				circleSprite.x = x + dWidth;
			}
			circleSprite.y = y;
		} else {
			double dHeight = 0.5 * ( height - width );
			double o1 = line.a * x + line.b * ( y - dHeight ) + line.c;
			double o2 = line.a * x + line.b * ( y + dHeight ) + line.c;
			if( Math.signum( o1 ) != Math.signum( o2 ) ) {
				circleSprite.y = -( line.a * x + line.c ) / line.b;
			} else if( Math.abs( o1 ) < Math.abs( o2 ) ) {
				circleSprite.y = y - dHeight;
			} else {
				circleSprite.y = y + dHeight;
			}
			circleSprite.x = x;
		}
		return circleSprite;
	}
	
	public Sprite toCircleUsingLine( Line line ) {
		return toCircleUsingLine( line, new Sprite( ShapeType.oval ) );
	}

	// ==================== Methods for ray ====================	

	public void toLine( Line line ) {
		line.usePoints( x, y, x + Math.cos( angle ), y + Math.sin( angle ) );
	}
	
	public Line toLine() {
		Line line = new Line();
		toLine( line );
		return line;
	}


	public boolean hasPoint( double x1, double y1 ) {
		double ang = Service.wrap( angle, 360.0 );
		if( ang < 45.0 || ang >= 315.0 ) {
			return x1 >= x;
		} else if( ang < 135.0 ) {
			return y1 >= y;
		} else if( ang < 225.0 ) {
			return x1 <= x;
		} else {
			return y1 <= y;
		}
	}
	

	public boolean hasPivot( Sprite pivot ) {
		return hasPoint( pivot.x, pivot.y );
	}

	// ==================== Methods for triangle ====================	

	public static void getMedium( Sprite pivot1, Sprite pivot2, Sprite medium ) {
		medium.setCoords( 0.5 * ( pivot1.x + pivot2.x ), 0.5 * ( pivot1.y + pivot2.y ) );
	}

	public static Sprite getMedium( Sprite pivot1, Sprite pivot2 ) {
		Sprite medium = new Sprite();
		getMedium( pivot1, pivot2, medium );
		return medium;
	}


	public void getHypotenuse( Line line ) {
		if( shapeType == ShapeType.topLeftTriangle || shapeType == ShapeType.bottomRightTriangle ) {
			line.usePoints( x, y, x - width, y + height );
		} else if( shapeType == ShapeType.topRightTriangle || shapeType == ShapeType.bottomLeftTriangle ) {
			line.usePoints( x, y, x + width, y + height );
		}
	}
	
	public Line getHypotenuse() {
		Line line = new Line();
		getHypotenuse( line );
		return line;
	}


	public void getRightAngleVertex( Sprite vertex ) {
		if( shapeType == ShapeType.topLeftTriangle || shapeType == ShapeType.bottomLeftTriangle ) {
			vertex.setX( x - 0.5 * width );
		} else if( shapeType == ShapeType.topRightTriangle || shapeType == ShapeType.bottomRightTriangle ) {
			vertex.setX( x + 0.5 * width );
		}
		if( shapeType == ShapeType.topLeftTriangle || shapeType == ShapeType.topRightTriangle ) {
			vertex.setY( y - 0.5 * height );
		} else if( shapeType == ShapeType.bottomLeftTriangle || shapeType == ShapeType.bottomRightTriangle ) {
			vertex.setY( y + 0.5 * height );
		}
	}
	
	public Sprite getRightAngleVertex() {
		Sprite vertex = new Sprite();
		getRightAngleVertex( vertex );
		return vertex;
	}


	public void getOtherVertices( Sprite pivot1, Sprite pivot2 ) {
		if( shapeType == ShapeType.topRightTriangle || shapeType == ShapeType.bottomLeftTriangle ) {
			getPivots( pivot1, null, pivot2, null );
		} else {
			getPivots( null, pivot1, null, pivot2 );
		}
	}

	// ==================== Cloning ===================	

	@Override
	public Sprite clone() {
		Sprite newSprite = new Sprite();
		copySpriteTo( newSprite );
		return newSprite;
	}


	public void copySpriteTo( Sprite sprite ) {
		copyShapeTo( sprite );

		sprite.shapeType = shapeType;
		sprite.angle = angle;
		sprite.displayingAngle = displayingAngle;
		sprite.velocity = velocity;
		sprite.frame = frame;
		sprite.updateFromAngularModel();
	}


	@Override
	public void copyTo( Shape shape ) {
		copySpriteTo( shape.toSprite() );
	}
	

	@Override
	public Sprite toSprite() {
		return this;
	}

	// ==================== Other ====================

	public void updateFromAngularModel() {
	}
	

	private void transformFrom( double x0, double y0, double cellWidth, double cellHeight, int tileX, int tileY ) {
		serviceSprite.setCoords( x0 + ( x + tileX ) * cellWidth, y0 + ( y + tileY ) * cellHeight );
		serviceSprite.setSize( width * cellWidth, height * cellHeight );
		serviceSprite.shapeType = shapeType;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		if( Sys.xMLGetMode() ) {
			if( xMLObject.fieldExists( "shape-type" ) ) {
				shapeType = xMLObject.manageObjectField( "shape-type", shapeType );
			} else {
				shapeType = ShapeType.getByNum( xMLObject.getIntegerAttribute( "shape" ) );
			}
		} else if( shapeType.singleton() ) {
			xMLObject.setAttribute( "shape", String.valueOf( shapeType.getNum() ) );
		} else {
			shapeType = xMLObject.manageObjectField( "shape-type", shapeType );
		}
		
		angle = xMLObject.manageDoubleAttribute( "angle", angle );
		displayingAngle = xMLObject.manageDoubleAttribute( "disp_angle", displayingAngle );
		velocity = xMLObject.manageDoubleAttribute( "velocity", velocity, 1.0 );
		frame = xMLObject.manageIntAttribute( "frame", frame );
	}
}
