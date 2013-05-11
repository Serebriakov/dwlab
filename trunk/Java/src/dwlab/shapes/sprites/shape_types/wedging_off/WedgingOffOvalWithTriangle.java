package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.shapes.sprites.Sprite;

public class WedgingOffOvalWithTriangle extends WedgingOffSprites {
	public static WedgingOffOvalWithTriangle instance = new WedgingOffOvalWithTriangle();

	
	@Override
	public void calculateVector( Sprite oval, Sprite triangle, Vector vector ) {
		triangle.getRightAngleVertex( servicePivots[ 2 ] );
		triangle.getOtherVertices( servicePivots[ 0 ], servicePivots[ 1 ] );
		serviceOval1 = oval.toCircle( servicePivots[ 2 ], serviceOval1 );
		double vDistance = 0.5 * distance( triangle.getWidth(), triangle.getHeight() ) * serviceOval1.getWidth() / triangle.getWidth();
		double dHeight = 0.5 * ( oval.getHeight() - serviceOval1.getHeight() );
		double dDX = 0.5 * serviceOval1.getWidth() / vDistance * cathetus( vDistance, 0.5 * serviceOval1.getWidth() );
		int dir = -1;
		int triangleNum = triangle.shapeType.getNum();
		if( triangleNum == Sprite.bottomLeftTriangle.getNum() || triangleNum == Sprite.bottomRightTriangle.getNum() ) dir = 1;
		if( triangleNum == Sprite.topRightTriangle.getNum() || triangleNum == Sprite.bottomRightTriangle.getNum() ) dDX = -dDX;
		if( serviceOval1.getX() < triangle.leftX() + dDX ) {
			dY = servicePivots[ 0 ].getY() - dir * cathetus( serviceOval1.getWidth() * 0.5, serviceOval1.getX() - servicePivots[ 0 ].getX() ) - serviceOval1.getY();
		} else if( serviceOval1.getX() > triangle.rightX() + dDX ) {
			dY = servicePivots[ 1 ].getY() - dir * cathetus( serviceOval1.getWidth() * 0.5, serviceOval1.getX() - servicePivots[ 1 ].getX() ) - serviceOval1.getY();
		} else {
			dY = serviceLines[ 0 ].getY( serviceOval1.getX() ) - dir * ( vDistance + dHeight ) - oval.getY();
		}

		dX1d, dY1d;
		WedgingOffOvalWithRectangle.instance.wedgeOffSprites( oval, triangle, dX1, dY1 );
		if( distance2( dX1, dY1 ) < dY * dY ) {
			dX = dX1;
			dY = dY1;
		}
	}
}
