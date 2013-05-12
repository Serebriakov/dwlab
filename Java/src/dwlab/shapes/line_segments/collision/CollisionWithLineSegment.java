package dwlab.shapes.line_segments.collision;

import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class CollisionWithLineSegment extends ServiceObjects {
	private static HashMap<ShapeType, CollisionWithLineSegment> handlersMap = new HashMap<ShapeType, CollisionWithLineSegment>();



	public static void register( ShapeType shapeType, CollisionWithLineSegment handler ) {
		handlersMap.put( shapeType, handler );
	}



	public boolean check( Sprite sprite, LineSegment lineSegment ) {
		return check( sprite, lineSegment.pivot[ 0 ], lineSegment.pivot[ 1 ] );
	}
	
	public abstract boolean check( Sprite sprite, Sprite lSPivot1, Sprite lSPivot2 );


	public static CollisionWithLineSegment handlers[];

	public static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlers = new CollisionWithLineSegment[ quantity ];
		for( Entry<ShapeType, CollisionWithLineSegment> entry : handlersMap.entrySet() ) {
			handlers[ entry.getKey().getNum() ] = entry.getValue();
		}
	}


	public static boolean lineSegment( Sprite lS1pivot1, Sprite lS1pivot2, Sprite lS2pivot1, Sprite lS2pivot2 ) {
		serviceLine1.usePivots( lS1pivot1, lS1pivot2 );
		if( serviceLine1.pivotOrientation( lS2pivot1 ) == serviceLine1.pivotOrientation( lS2pivot2 ) ) return false;
		serviceLine1.usePivots( lS2pivot1, lS2pivot2 );
		if( serviceLine1.pivotOrientation( lS1pivot1 ) != serviceLine1.pivotOrientation( lS1pivot2 ) ) return true;
		return false;
	}
}