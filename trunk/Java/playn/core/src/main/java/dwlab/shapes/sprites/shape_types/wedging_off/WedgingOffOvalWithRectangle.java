/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffOvalWithRectangle extends WedgingOffSprites {
	public static WedgingOffOvalWithRectangle instance = new WedgingOffOvalWithRectangle();

	
	@Override
	public void calculateVector( Sprite oval, Sprite rectangle, Vector vector ) {
		boolean a = ( Math.abs( oval.getY() - rectangle.getY() ) * rectangle.getWidth() >= Math.abs( oval.getX() - rectangle.getX() ) * rectangle.getHeight() );
		if( ( oval.getX() > rectangle.leftX() && oval.getX() < rectangle.rightX() ) && a ) {
			vector.x = 0d;
			vector.y = ( 0.5d * ( rectangle.getHeight() + oval.getHeight() ) - Math.abs( rectangle.getY() - oval.getY() ) ) * Math.signum( oval.getY() - rectangle.getY() );
		} else if( oval.getY() > rectangle.topY() && oval.getY() < rectangle.bottomY() && ! a ) {
			vector.x = ( 0.5d * ( rectangle.getWidth() + oval.getWidth() ) - Math.abs( rectangle.getX() - oval.getX() ) ) * Math.signum( oval.getX() - rectangle.getX() );
			vector.y = 0d;
		} else {
			servicePivots[ 0 ].setX( rectangle.getX() + 0.5d * rectangle.getWidth() * Math.signum( oval.getX() - rectangle.getX() ) );
			servicePivots[ 0 ].setY( rectangle.getY() + 0.5d * rectangle.getHeight() * Math.signum( oval.getY() - rectangle.getY() ) );
			oval = oval.toCircle( servicePivots[ 0 ], serviceOval1 );
			double k = 1d - 0.5d * oval.getWidth() / oval.distanceTo( servicePivots[ 0 ] );
			vector.x = ( servicePivots[ 0 ].getX() - oval.getX() ) * k;
			vector.y = ( servicePivots[ 0 ].getY() - oval.getY() ) * k;
		}
	}
}
