package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class OvalWithOvalCollision extends SpritesCollision {
	public static OvalWithOvalCollision instance = new OvalWithOvalCollision();

	@Override
	public boolean check( Sprite oval1, Sprite oval2 ) {
		oval1 = oval1.toCircle( oval2, serviceOval1 );
		oval2 = oval2.toCircle( oval1, serviceOval2 );
		double radiuses = 0.5 * ( oval1.getWidth() + oval2.getWidth() ) - inaccuracy;
		if( oval1.distance2to( oval2 ) < radiuses * radiuses ) return true; else return false;
	}
}
