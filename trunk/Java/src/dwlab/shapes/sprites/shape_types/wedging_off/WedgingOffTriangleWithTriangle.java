package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.shapes.sprites.Sprite;

public class WedgingOffTriangleWithTriangle extends WedgingOffSprites {
	public static WedgingOffTriangleWithTriangle instance = new WedgingOffTriangleWithTriangle();

	@Override
	public void wedgeOffSprites( Sprite triangle1, Sprite triangle2, double dX var, double dY var ) {
		dX1d, dY1d;
		WedgingOffRectangleWithTriangle.instance.wedgeOffSprites( triangle1, triangle2, dX1, dY1 );
		d1d = distance2( dX1, dY1 );

		dX2d, dY2d;
		WedgingOffRectangleWithTriangle.instance.wedgeOffSprites( triangle2, triangle1, dX2, dY2 );
		d2d = distance2( dX2, dY2 );

		while( true ) {
			int triangle2shapeTypeNum = triangle2.shapeType.getNum();
			switch( triangle1.shapeType.getNum() ) {
				case Sprite.topLeftTriangle.getNum():
					if( triangle2shapeTypeNum != Sprite.bottomRightTriangle.getNum() ) break;
				case Sprite.topRightTriangle.getNum():
					if( triangle2shapeTypeNum != Sprite.bottomLeftTriangle.getNum() ) break;
				case Sprite.bottomLeftTriangle.getNum():
					if( triangle2shapeTypeNum != Sprite.topRightTriangle.getNum() ) break;
				case Sprite.bottomRightTriangle.getNum():
					if( triangle2shapeTypeNum != Sprite.topLeftTriangle.getNum() ) break;
			}

			dY3d = 0;
			popAngle( triangle1, triangle2, dY3 );
			popAngle( triangle2, triangle1, dY3 );
			if( dY3 == 0 ) break;

			dY32d = dY3 * dY3;
			if( dY32 < d1 && dY32 < d2 ) {
				triangle1.getRightAngleVertex( servicePivots[ 0 ] );
				triangle2.getRightAngleVertex( servicePivots[ 1 ] );
				dX = 0;
				dY = dY3 * sgn( servicePivots[ 0 ].getY() - servicePivots[ 1 ].getY() );
				return;
			} else {
				break;
			}
		}

		if( d1 < d2 ) {
			dX = dX1;
			dY = dY1;
		} else {
			dX = -dX2;
			dY = -dY2;
		}
	}
}
