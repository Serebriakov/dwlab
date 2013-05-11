package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffPivotWithRectangle extends WedgingOffSprites {
	public static WedgingOffPivotWithRectangle instance = new WedgingOffPivotWithRectangle();

	
	@Override
	public void calculateVector( Sprite pivot, Sprite rectangle, Vector vector ) {
		if( Math.abs( pivot.getY() - rectangle.getY() ) * rectangle.getWidth() >= Math.abs( pivot.getX() - rectangle.getX() ) * rectangle.getHeight() ) {
			vector.y = rectangle.getY() + 0.5 * rectangle.getHeight() * Math.signum( pivot.getY() - rectangle.getY() ) - pivot.getY();
			vector.x = 0;
		} else {
			vector.x = rectangle.getX() + 0.5 * rectangle.getWidth() * Math.signum( pivot.getX() - rectangle.getX() ) - pivot.getX();
			vector.y = 0;
		}
	}
}