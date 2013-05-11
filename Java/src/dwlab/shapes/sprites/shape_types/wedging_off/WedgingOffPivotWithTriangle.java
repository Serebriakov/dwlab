package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.shapes.sprites.Sprite;

public class WedgingOffPivotWithTriangle extends WedgingOffSprites {
	public static WedgingOffPivotWithTriangle instance = new WedgingOffPivotWithTriangle();

	
	@Override
	public void calculateVector( Sprite pivot, Sprite triangle, Vector vector ) {
		dY = serviceLines[ 0 ].getY( pivot.getX() ) - pivot.getY();

		dX1d, dY1d;
		WedgingOffPivotWithRectangle.instance.wedgeOffSprites( pivot, triangle, dX1, dY1 );
		if( distance2( dX1, dY1 ) < dY * dY ) {
			dX = dX1;
			dY = dY1;
		}
	}
}