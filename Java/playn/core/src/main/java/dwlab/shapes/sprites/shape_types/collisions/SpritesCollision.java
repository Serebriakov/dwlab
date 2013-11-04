/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map.Entry;

public class SpritesCollision extends ServiceObjects {
	public static SpritesCollision defaultHandler = new SpritesCollision();
	
	private static HashMap<ShapeType, HashMap<ShapeType, SpritesCollision>> spritesCollisionsMap = new HashMap<ShapeType, HashMap<ShapeType, SpritesCollision>>();

	public static SpritesCollision handlers[][];

	static {
		register( ShapeType.pivot, ShapeType.oval, new PivotWithOvalCollision() );
		register( ShapeType.pivot, ShapeType.rectangle, new PivotWithRectangleCollision() );
		register( ShapeType.oval, ShapeType.oval, new OvalWithOvalCollision() );
		register( ShapeType.oval, ShapeType.rectangle, new OvalWithRectangleCollision() );
		register( ShapeType.oval, ShapeType.ray, new OvalWithRayCollision() );
		register( ShapeType.rectangle, ShapeType.rectangle, new RectangleWithRectangleCollision() );
		register( ShapeType.rectangle, ShapeType.ray, new RectangleWithRayCollision() );
		for( ShapeType triangle : ShapeType.triangles ) {
			register( ShapeType.pivot, triangle, new PivotWithTriangleCollision() );
			register( ShapeType.oval, triangle, new OvalWithTriangleCollision() );
			register( ShapeType.rectangle, triangle, new RectangleWithTriangleCollision() );
			register( ShapeType.ray, triangle, new RayWithTriangleCollision() );
			for( ShapeType triangle2 : ShapeType.triangles ) {
				register( triangle, triangle2, TriangleWithTriangleCollision.instance );
			}
		}
		initSystem();		
	}


	public boolean check( Sprite sprite1, Sprite sprite2 ) {
		return false;
	}

	public static void register( ShapeType shapeType1, ShapeType shapeType2, SpritesCollision collsion ) {
		HashMap<ShapeType, SpritesCollision> map = spritesCollisionsMap.get( shapeType1 );
		if( map == null ) {
			map = new HashMap<ShapeType, SpritesCollision>();
			spritesCollisionsMap.put( shapeType1, map );
		}
		map.put( shapeType2, collsion );
	}


	public static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlers = new SpritesCollision[ quantity ][];
		for( int n =0; n < quantity; n++ ) {
			handlers[ n ] =new SpritesCollision[ quantity ];
			for( int m = 0; m < quantity; m++ ) handlers[ n ][ m ] = defaultHandler;
		}
		for( Entry<ShapeType, HashMap<ShapeType, SpritesCollision>> entry1 : spritesCollisionsMap.entrySet() ) {
			for( Entry<ShapeType, SpritesCollision> entry2 : entry1.getValue().entrySet() ) {
				handlers[ entry1.getKey().getNum() ][ entry2.getKey().getNum() ] = entry2.getValue();
			}
		}
	}
}