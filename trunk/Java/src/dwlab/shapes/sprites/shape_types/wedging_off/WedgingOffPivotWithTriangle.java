package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffPivotWithTriangle extends WedgingOffSprites {
	public static WedgingOffPivotWithTriangle instance = new WedgingOffPivotWithTriangle();

	
	@Override
	public void calculateVector( Sprite pivot, Sprite triangle, Vector vector ) {
		vector.y = serviceLines[ 0 ].getY( pivot.getX() ) - pivot.getY();

		WedgingOffPivotWithRectangle.instance.calculateVector( pivot, triangle, vector1 );
		if( Service.distance2( vector1.x, vector1.y ) < vector.y * vector.y ) {
			vector.x = vector1.x;
			vector.y = vector1.y;
		}
	}
}