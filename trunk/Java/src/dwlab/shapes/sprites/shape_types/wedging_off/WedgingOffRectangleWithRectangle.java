package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.shapes.sprites.Sprite;

public class WedgingOffRectangleWithRectangle extends WedgingOffSprites {
	public static WedgingOffRectangleWithRectangle instance = new WedgingOffRectangleWithRectangle();

	
	@Override
	public void calculateVector( Sprite rectangle1, Sprite rectangle2, Vector vector ) {
		dX = 0.5 * ( rectangle1.getWidth() + rectangle2.getWidth() ) - Math.abs( rectangle1.getX() - rectangle2.getX() );
		dY = 0.5 * ( rectangle1.getHeight() + rectangle2.getHeight() ) - Math.abs( rectangle1.getY() - rectangle2.getY() );

		if( dX < dY ) {
			dX *= sgn( rectangle1.getX() - rectangle2.getX() );
			dY = 0;
		} else {
			dX = 0;
			dY *= sgn( rectangle1.getY() - rectangle2.getY() );
		}
	}
}
