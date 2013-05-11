package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class PivotWithRectangleCollision extends SpritesCollision {
	public static PivotWithRectangleCollision instance = new PivotWithRectangleCollision();

	
	@Override
	public boolean check( Sprite pivot, Sprite rectangle ) {
		if( 2d * Math.abs( pivot.getX() - rectangle.getX() ) < rectangle.getWidth() - inaccuracy &&
				2d * Math.abs( pivot.getY() - rectangle.getY() ) < rectangle.getHeight() - inaccuracy ) return true; else return false;
	}
}