package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.base.Obj;
import dwlab.base.Service;
import dwlab.shapes.Line;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.shapes.sprites.shape_types.overlapping.SpritesOverlapping;
import java.util.HashMap;
import java.util.Map.Entry;

/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */


public class SpritesCollision extends ServiceObjects {
	public static SpritesCollision defaultHandler = new SpritesCollision();
	
	static {
		for( ShapeType triangle : ShapeType.triangles ) {
			register( ShapeType.pivot, triangle, PivotWithTriangleCollision.instance );
			register( ShapeType.oval, triangle, OvalWithTriangleCollision.instance );
			register( ShapeType.rectangle, triangle, RectangleWithTriangleCollision.instance );
			register( ShapeType.ray, triangle, RayWithTriangleCollision.instance );
			for( ShapeType triangle2 : ShapeType.triangles ) {
				register( triangle, triangle2, TriangleWithTriangleCollision.instance );
			}
		}
	}


	boolean check( Sprite sprite1, Sprite sprite2 ) {
		return false;
	}


	static HashMap<ShapeType, HashMap<ShapeType, SpritesCollision>> spritesCollisionsMap = new HashMap<ShapeType, HashMap<ShapeType, SpritesCollision>>();

	static void register( ShapeType shapeType1, ShapeType shapeType2, SpritesCollision collsion ) {
		HashMap map = spritesCollisionsMap.get( shapeType1 );
		if( map == null ) {
			map = new HashMap();
			spritesCollisionsMap.put( shapeType1, map );
		}
		map.put( shapeType2, collsion );
	}


	static SpritesCollision handlers[][];

	static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlers = new SpritesCollision[ quantity ][];
		for( int n =0; n < quantity; n++ ) {
			handlers[ n ] =new SpritesCollision[ quantity ];
			for( int m = 0; m < quantity; m++ ) handlers[ n ][ m ] = defaultHandler;
		}
		for( Entry<ShapeType, HashMap<ShapeType, SpritesCollision>> entry1 : spritesCollisionsMap.entrySet() ) {
			ShapeType shapeType1 = entry1.getKey();
			for( Entry<ShapeType, SpritesCollision> entry2 : entry1.getValue().entrySet() ) {
				handlers[ shapeType1.getNum() ][ entry2.getKey().getNum() ] = entry2.getValue();
			}
		}
	}
}