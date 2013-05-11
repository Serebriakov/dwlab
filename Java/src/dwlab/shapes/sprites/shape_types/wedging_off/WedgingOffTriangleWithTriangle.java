package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Service;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class WedgingOffTriangleWithTriangle extends WedgingOffSprites {
	public static WedgingOffTriangleWithTriangle instance = new WedgingOffTriangleWithTriangle();

	@Override
	public void calculateVector( Sprite triangle1, Sprite triangle2, Vector vector ) {
		WedgingOffRectangleWithTriangle.instance.calculateVector( triangle1, triangle2, vector1 );
		double d1 = Service.distance2( vector1.x, vector1.y );

		WedgingOffRectangleWithTriangle.instance.calculateVector( triangle2, triangle1, vector2 );
		double d2 = Service.distance2( vector2.x, vector2.y );

		while( true ) {
			int triangle2shapeTypeNum = triangle2.shapeType.getNum();
			int num = triangle1.shapeType.getNum();
			if( num == ShapeType.topLeftTriangle.getNum() ) {
				if( triangle2shapeTypeNum != ShapeType.bottomRightTriangle.getNum() ) break;
			} else if( num == ShapeType.topRightTriangle.getNum() ) {
				if( triangle2shapeTypeNum != ShapeType.bottomLeftTriangle.getNum() ) break;
			} else if( num == ShapeType.bottomLeftTriangle.getNum() ) {
				if( triangle2shapeTypeNum != ShapeType.topRightTriangle.getNum() ) break;
			} else if( num == ShapeType.bottomRightTriangle.getNum() ) {
				if( triangle2shapeTypeNum != ShapeType.topLeftTriangle.getNum() ) break;
			}

			double dY3 = 0;
			popAngle( triangle1, triangle2, dY3 );
			popAngle( triangle2, triangle1, dY3 );
			if( dY3 == 0 ) break;

			double dY32 = dY3 * dY3;
			if( dY32 < d1 && dY32 < d2 ) {
				triangle1.getRightAngleVertex( servicePivots[ 0 ] );
				triangle2.getRightAngleVertex( servicePivots[ 1 ] );
				vector.x = 0;
				vector.y = dY3 * Math.signum( servicePivots[ 0 ].getY() - servicePivots[ 1 ].getY() );
				return;
			} else {
				break;
			}
		}

		if( d1 < d2 ) {
			vector.x = vector1.x;
			vector.y = vector1.y;
		} else {
			vector.x = -vector2.x;
			vector.y = -vector2.y;
		}
	}
}
