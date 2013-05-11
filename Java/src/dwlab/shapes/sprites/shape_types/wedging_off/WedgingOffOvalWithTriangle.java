package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Service;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class WedgingOffOvalWithTriangle extends WedgingOffSprites {
	public static WedgingOffOvalWithTriangle instance = new WedgingOffOvalWithTriangle();

	
	@Override
	public void calculateVector( Sprite oval, Sprite triangle, Vector vector ) {
		triangle.getRightAngleVertex( servicePivots[ 2 ] );
		triangle.getOtherVertices( servicePivots[ 0 ], servicePivots[ 1 ] );
		serviceOval1 = oval.toCircle( servicePivots[ 2 ], serviceOval1 );
		double vDistance = 0.5 * Service.distance( triangle.getWidth(), triangle.getHeight() ) * serviceOval1.getWidth() / triangle.getWidth();
		double dHeight = 0.5 * ( oval.getHeight() - serviceOval1.getHeight() );
		double dDX = 0.5 * serviceOval1.getWidth() / vDistance * Service.cathetus( vDistance, 0.5 * serviceOval1.getWidth() );
		int dir = -1;
		int triangleNum = triangle.shapeType.getNum();
		if( triangleNum == ShapeType.bottomLeftTriangle.getNum() || triangleNum == ShapeType.bottomRightTriangle.getNum() ) dir = 1;
		if( triangleNum == ShapeType.topRightTriangle.getNum() || triangleNum == ShapeType.bottomRightTriangle.getNum() ) dDX = -dDX;
		if( serviceOval1.getX() < triangle.leftX() + dDX ) {
			vector.y = servicePivots[ 0 ].getY() - dir * Service.cathetus( serviceOval1.getWidth() * 0.5, serviceOval1.getX() - servicePivots[ 0 ].getX() ) - serviceOval1.getY();
		} else if( serviceOval1.getX() > triangle.rightX() + dDX ) {
			vector.y = servicePivots[ 1 ].getY() - dir * Service.cathetus( serviceOval1.getWidth() * 0.5, serviceOval1.getX() - servicePivots[ 1 ].getX() ) - serviceOval1.getY();
		} else {
			vector.y = serviceLines[ 0 ].getY( serviceOval1.getX() ) - dir * ( vDistance + dHeight ) - oval.getY();
		}

		WedgingOffOvalWithRectangle.instance.calculateVector( oval, triangle, vector1 );
		if( Service.distance2( vector1.x, vector1.y ) < vector.y * vector.y ) {
			vector.x = vector1.y;
			vector.y = vector1.y;
		}
	}
}
