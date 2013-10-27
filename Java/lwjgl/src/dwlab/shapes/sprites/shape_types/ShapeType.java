/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types;

import dwlab.base.Obj;
import dwlab.shapes.sprites.Sprite;
import java.util.LinkedList;

public abstract class ShapeType extends Obj {
	public static LinkedList<ShapeType> shapeTypes = new LinkedList();
	
	/**
	 * Type of the sprite shape: pivot. It's a point on game field with (X, Y) coordinates.
	 */
	public static final Pivot pivot = new Pivot();
	
	/**
	 * Type of the sprite shape: oval which is inscribed in shape's rectangle.
	 */
	public static final Oval oval = new Oval();
	
	/**
	 * Type of the sprite shape: rectangle.
	 */
	public static final Rectangle rectangle = new Rectangle();
	
	/**
	 * Type of the sprite shape: ray which starts in (X, Y) and directed as Angle.
	 */
	public static final Ray ray = new Ray();
	
	/**
	 * Type of the sprite shape: right triangle which is inscribed in shape's rectangle and have right angle situated in corresponding corner.
	 */
	public static final TopLeftTriangle topLeftTriangle = new TopLeftTriangle();
	public static final TopRightTriangle topRightTriangle = new TopRightTriangle();
	public static final BottomLeftTriangle bottomLeftTriangle = new BottomLeftTriangle();
	public static final BottomRightTriangle bottomRightTriangle = new BottomRightTriangle();
	
	public static final ShapeType triangles[] = { ShapeType.topLeftTriangle, ShapeType.topRightTriangle, 
			ShapeType.bottomLeftTriangle, ShapeType.bottomRightTriangle };
	/**
	 * Type of the sprite shape: mask of raster image which is inscribed in shape's rectangle.
	 */
	public static final Raster raster = new Raster();
					
	public static final SpriteTemplate spriteTemplate = new SpriteTemplate();
	
	
	static {
		register( pivot );
		register( oval );
		register( rectangle );
		register( ray );
		register( topLeftTriangle );
		register( topRightTriangle );
		register( bottomLeftTriangle );
		register( bottomRightTriangle );
		register( raster );
		register( spriteTemplate );
	}


	public abstract int getNum();


	public abstract String getName();

	
	public boolean singleton() {
		return true;
	}


	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		return null;
	}


	public static ShapeType getByNum( int num ) {
		for( ShapeType shapeType : shapeTypes ) {
			if( shapeType.getNum() == num ) return shapeType;
		}
		return null;
	}


	public static void register( ShapeType shapeType ) {
		int num = shapeType.getNum();
		if( getByNum( num ) != null ) error( "Trying to add shape type with number which assigned to already registered shape type" );
		if( num < 0 || num > shapeTypes.size() ) error( "Wrong shape type number" );
		shapeTypes.addLast( shapeType );
	}
}
