package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffPivotWithOval extends WedgingOffSprites {
	public static WedgingOffPivotWithOval instance = new WedgingOffPivotWithOval();

	
	@Override
	public void calculateVector( Sprite pivot, Sprite oval, Vector vector ) {
		oval = oval.toCircle( pivot, serviceOval1 );
		double k = 0.5 * oval.getWidth() / oval.distanceTo( pivot ) - 1.0;
		vector.x = ( pivot.getX() - oval.getX() ) * k;
		vector.y = ( pivot.getY() - oval.getY() ) * k;
	}
}