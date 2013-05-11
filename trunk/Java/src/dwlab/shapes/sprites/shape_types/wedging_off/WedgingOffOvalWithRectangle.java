package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffOvalWithRectangle extends WedgingOffSprites {
	public static WedgingOffOvalWithRectangle instance = new WedgingOffOvalWithRectangle();

	
	@Override
	public void calculateVector( Sprite oval, Sprite rectangle, Vector vector ) {
		int a = ( Math.abs( oval.getY() - rectangle.getY() ) * rectangle.getWidth() >= Math.abs( oval.getX() - rectangle.getX() ) * rectangle.getHeight() );
		if( ( oval.getX() > rectangle.leftX() && oval.getX() < rectangle.rightX() ) && a ) {
			dX = 0;
			dY = ( 0.5 * ( rectangle.getHeight() + oval.getHeight() ) - Math.abs( rectangle.getY() - oval.getY() ) ) * sgn( oval.getY() - rectangle.getY() );
		} else if( oval.getY() > rectangle.topY() && oval.getY() < rectangle.bottomY() && ! a ) {
			dX = ( 0.5 * ( rectangle.getWidth() + oval.getWidth() ) - Math.abs( rectangle.getX() - oval.getX() ) ) * sgn( oval.getX() - rectangle.getX() );
			dY = 0;
		} else {
			servicePivots[ 0 ].getX() = rectangle.getX() + 0.5 * rectangle.getWidth() * sgn( oval.getX() - rectangle.getX() );
			servicePivots[ 0 ].getY() = rectangle.getY() + 0.5 * rectangle.getHeight() * sgn( oval.getY() - rectangle.getY() );
			oval = oval.toCircle( servicePivots[ 0 ], serviceOval1 );
			double k = 1.0 - 0.5 * oval.getWidth() / oval.distanceTo( servicePivots[ 0 ] );
			dX = ( servicePivots[ 0 ].getX() - oval.getX() ) * k;
			dY = ( servicePivots[ 0 ].getY() - oval.getY() ) * k;
		}
	}
}
