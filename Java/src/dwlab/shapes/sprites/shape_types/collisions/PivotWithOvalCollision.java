package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class PivotWithOvalCollision extends SpritesCollision {
	public static PivotWithOvalCollision instance = new PivotWithOvalCollision();

	
	@Override
	public boolean check( Sprite pivot, Sprite oval ) {
		oval = oval.toCircle( pivot, serviceOval1 );
		double radius = 0.5d * oval.getWidth() - inaccuracy;
		if( pivot.distance2to( oval ) < radius * radius ) return true; else return false;
	}
}