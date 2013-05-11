package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Service;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map;

/**
 * Constant for dealing with inaccuracy of double type operations.
 */

public abstract class WedgingOffSprites extends ServiceObjects {
	static {
		for( ShapeType triangle : ShapeType.triangles ) {
			register( ShapeType.pivot, triangle, WedgingOffPivotWithTriangle.instance );
			register( ShapeType.oval, triangle, WedgingOffOvalWithTriangle.instance );
			register( ShapeType.rectangle, triangle, WedgingOffRectangleWithTriangle.instance );
			register( ShapeType.ray, triangle, RayWithTriangle.instance );
			for( ShapeType triangle2 : ShapeType.triangles ) {
				register( triangle, triangle2, WedgingOffTriangleWithTriangle.instance );
			}
		}
	}


	public abstract void calculateVector( Sprite oval1, Sprite oval2, Vector vector );


	static HashMap<ShapeType, HashMap<ShapeType, WedgingOffSprites>> spritesCollisionsMap = new HashMap<ShapeType, HashMap<ShapeType, WedgingOffSprites>>();

	static void register( ShapeType shapeType1, ShapeType shapeType2, WedgingOffSprites wedgingOff ) {
		HashMap map = spritesCollisionsMap.get( shapeType1 );
		if( map == null ) {
			map = new HashMap();
			spritesCollisionsMap.put( shapeType1, map );
		}
		map.put( shapeType2, wedgingOff );
	}


	static WedgingOffSprites wedgingOffArray[][];

	static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		wedgingOffArray = new WedgingOffSprites[ quantity ][];
		for( int n =0; n < quantity; n++ ) wedgingOffArray[ n ] =new WedgingOffSprites[ quantity ];
		for( Map.Entry<ShapeType, HashMap<ShapeType, WedgingOffSprites>> entry1 : spritesCollisionsMap.entrySet() ) {
			ShapeType shapeType1 = entry1.getKey();
			for( Map.Entry<ShapeType, WedgingOffSprites> entry2 : entry1.getValue().entrySet() ) {
				wedgingOffArray[ shapeType1.getNum() ][ entry2.getKey().getNum() ] = entry2.getValue();
			}
		}
	}


	static double popAngle( Sprite triangle1, Sprite triangle2, double dY ) {
		triangle2.getRightAngleVertex( servicePivots[ 0 ] );
		triangle2.getHypotenuse( serviceLines[ 0 ] );
		triangle1.getOtherVertices( servicePivots[ 1 ], servicePivots[ 2 ] );
		int o = serviceLines[ 0 ].pivotOrientation( servicePivots[ 0 ] );
		for( int n = 1; n <= 2; n++ ) {
			if( o == serviceLines[ 0 ].pivotOrientation( servicePivots[ n ] ) ) {
				if( Service.inLimits( servicePivots[ n ].getX(), triangle2.leftX(), triangle2.rightX() ) ) {
					dY = Math.max( dY, Math.abs( serviceLines[ 0 ].getY( servicePivots[ n ].getX() ) - servicePivots[ n ].getY() ) );
				}
			}
		}
		return dY;
	}
}