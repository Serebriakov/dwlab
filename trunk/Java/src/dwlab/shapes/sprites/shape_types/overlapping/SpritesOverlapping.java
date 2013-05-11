package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map;

/**
 * Constant for dealing with inaccuracy of double type operations.
 */

public abstract class SpritesOverlapping extends ServiceObjects {
	static {
		register( ShapeType.oval, ShapeType.pivot, OvalOverlapsPivot.instance );
		register( ShapeType.oval, ShapeType.oval, OvalOverlapsOval.instance );
		register( ShapeType.oval, ShapeType.rectangle, OvalOverlapsRectangle.instance );
		for( ShapeType triangle : ShapeType.triangles ) {
			register( ShapeType.oval, triangle, OvalOverlapsTriangle.instance );
		}
	}


	public abstract boolean check( Sprite sprite1, Sprite sprite2 );


	static HashMap<ShapeType, HashMap<ShapeType, SpritesOverlapping>> spritesOverlappingMap = new HashMap<ShapeType, HashMap<ShapeType, SpritesOverlapping>>();

	static void register( ShapeType shapeType1, ShapeType shapeType2, SpritesOverlapping handler ) {
		HashMap map = spritesOverlappingMap.get( shapeType1 );
		if( map == null ) {
			map = new HashMap();
			spritesOverlappingMap.put( shapeType1, map );
		}
		map.put( shapeType2, handler );
	}


	static SpritesOverlapping overlappingArray[][];

	static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		overlappingArray = new SpritesOverlapping[ quantity ][];
		for( int n =0; n < quantity; n++ ) overlappingArray[ n ] =new SpritesOverlapping[ quantity ];
		for( Map.Entry<ShapeType, HashMap<ShapeType, SpritesOverlapping>> entry1 : spritesOverlappingMap.entrySet() ) {
			ShapeType shapeType1 = entry1.getKey();
			for( Map.Entry<ShapeType, SpritesOverlapping> entry2 : entry1.getValue().entrySet() ) {
				overlappingArray[ shapeType1.getNum() ][ entry2.getKey().getNum() ] = entry2.getValue();
			}
		}
	}
}