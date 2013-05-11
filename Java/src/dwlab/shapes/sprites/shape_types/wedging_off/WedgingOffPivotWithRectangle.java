package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.shapes.sprites.Sprite;

public class WedgingOffPivotWithRectangle extends WedgingOffSprites {
	public static WedgingOffPivotWithRectangle instance = new WedgingOffPivotWithRectangle();

	
	@Override
	public void calculateVector( Sprite pivot, Sprite rectangle, Vector vector ) {
		if( Math.abs( pivot.getY() - rectangle.getY() ) * rectangle.getWidth() >= Math.abs( pivot.getX() - rectangle.getX() ) * rectangle.getHeight() ) {
			dY = rectangle.getY() + 0.5 * rectangle.getHeight() * sgn( pivot.getY() - rectangle.getY() ) - pivot.getY();
		} else {
			dX = rectangle.getX() + 0.5 * rectangle.getWidth() * sgn( pivot.getX() - rectangle.getX() ) - pivot.getX();
		}
	}
}