package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffOvalWithOval extends WedgingOffSprites {
	public static WedgingOffOvalWithOval instance = new WedgingOffOvalWithOval();

	@Override
	public void calculateVector( Sprite oval1, Sprite oval2, Vector vector ) {
		oval1 = oval1.toCircle( oval2, serviceOval1 );
		oval2 = oval2.toCircle( oval1, serviceOval2  );
		double k = 0.5 * ( oval1.getWidth() + oval2.getWidth() ) / oval1.distanceTo( oval2 ) - 1.0;
		vector.x = ( oval1.getX() - oval2.getX() ) * k;
		vector.y = ( oval1.getY() - oval2.getY() ) * k;
	}
}
