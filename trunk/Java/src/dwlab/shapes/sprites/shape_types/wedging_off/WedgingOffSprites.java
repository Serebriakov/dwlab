package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map;

/**
 * Constant for dealing with inaccuracy of double type operations.
 */

public class WedgingOffSprites extends ServiceObjects {
	public static WedgingOffSprites defaultHandler = new WedgingOffSprites();
	
	private static HashMap<ShapeType, HashMap<ShapeType, WedgingOffSprites>> spritesCollisionsMap = new HashMap<ShapeType, HashMap<ShapeType, WedgingOffSprites>>();
	
	static {
		register( ShapeType.pivot, ShapeType.oval, WedgingOffPivotWithOval.instance );
		register( ShapeType.pivot, ShapeType.rectangle, WedgingOffPivotWithRectangle.instance );
		register( ShapeType.oval, ShapeType.oval, WedgingOffOvalWithOval.instance );
		register( ShapeType.oval, ShapeType.rectangle, WedgingOffOvalWithRectangle.instance );
		register( ShapeType.rectangle, ShapeType.rectangle, WedgingOffRectangleWithRectangle.instance );
		for( ShapeType triangle : ShapeType.triangles ) {
			register( ShapeType.pivot, triangle, WedgingOffPivotWithTriangle.instance );
			register( ShapeType.oval, triangle, WedgingOffOvalWithTriangle.instance );
			register( ShapeType.rectangle, triangle, WedgingOffRectangleWithTriangle.instance );
			for( ShapeType triangle2 : ShapeType.triangles ) {
				register( triangle, triangle2, WedgingOffTriangleWithTriangle.instance );
			}
		}
		initSystem();
	}


	public void calculateVector( Sprite sprite1, Sprite sprite2, Vector vector ) {
		vector.set( 0d, 0d );
	}


	static void register( ShapeType shapeType1, ShapeType shapeType2, WedgingOffSprites wedgingOff ) {
		HashMap map = spritesCollisionsMap.get( shapeType1 );
		if( map == null ) {
			map = new HashMap();
			spritesCollisionsMap.put( shapeType1, map );
		}
		map.put( shapeType2, wedgingOff );
	}


	public static WedgingOffSprites handlers[][];

	static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlers = new WedgingOffSprites[ quantity ][];
		for( int n =0; n < quantity; n++ ) {
			handlers[ n ] =new WedgingOffSprites[ quantity ];
			for( int m = 0; m < quantity; m++ ) handlers[ n ][ m ] = defaultHandler;
		}
		for( Map.Entry<ShapeType, HashMap<ShapeType, WedgingOffSprites>> entry1 : spritesCollisionsMap.entrySet() ) {
			ShapeType shapeType1 = entry1.getKey();
			for( Map.Entry<ShapeType, WedgingOffSprites> entry2 : entry1.getValue().entrySet() ) {
				handlers[ shapeType1.getNum() ][ entry2.getKey().getNum() ] = entry2.getValue();
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


	public static void separate( Sprite pivot1, Sprite pivot2, Vector impulse, double pivot1movingResistance, double pivot2movingResistance ) {
		if( pivot1movingResistance < 0 ) {
			if( pivot2movingResistance < 0 ) return;
			pivot1movingResistance = 1.0;
			pivot2movingResistance = 0.0;
		} else if( pivot2movingResistance < 0 ) {
			pivot1movingResistance = 0.0;
			pivot2movingResistance = 1.0;
		}

		double k1, k2;
		double movingResistanceSum = pivot1movingResistance + pivot2movingResistance;
		if( movingResistanceSum != 0 ) {
			k1 = pivot2movingResistance / movingResistanceSum;
			k2 = pivot1movingResistance / movingResistanceSum;
		} else {
			k1 = 0.5d;
			k2 = 0.5d;
		}

		if( k1 != 0.0 ) pivot1.alterCoords( k1 * impulse.x, k1 * impulse.y );
		if( k2 != 0.0 ) pivot2.alterCoords( -k2 * impulse.x, -k2 * impulse.y );
	}
}