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
	}


	public void calculateVector( Sprite sprite1, Sprite sprite2, Vector vector ) {
		
	}


	private static HashMap<ShapeType, HashMap<ShapeType, WedgingOffSprites>> spritesCollisionsMap = new HashMap<ShapeType, HashMap<ShapeType, WedgingOffSprites>>();

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
}