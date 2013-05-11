package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffRectangleWithRectangle extends WedgingOffSprites {
	public static WedgingOffRectangleWithRectangle instance = new WedgingOffRectangleWithRectangle();

	
	@Override
	public void calculateVector( Sprite rectangle1, Sprite rectangle2, Vector vector ) {
		vector.x = 0.5 * ( rectangle1.getWidth() + rectangle2.getWidth() ) - Math.abs( rectangle1.getX() - rectangle2.getX() );
		vector.y = 0.5 * ( rectangle1.getHeight() + rectangle2.getHeight() ) - Math.abs( rectangle1.getY() - rectangle2.getY() );

		if( vector.x < vector.y ) {
			vector.x *= Math.signum( rectangle1.getX() - rectangle2.getX() );
			vector.y = 0;
		} else {
			vector.x = 0;
			vector.y *= Math.signum( rectangle1.getY() - rectangle2.getY() );
		}
	}
}
