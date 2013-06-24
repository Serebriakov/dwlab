/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class OvalWithRectangleCollision extends SpritesCollision {
	public static OvalWithRectangleCollision instance = new OvalWithRectangleCollision();

	
	@Override
	public boolean check( Sprite oval, Sprite rectangle ) {
		oval = oval.toCircle( rectangle, serviceOval1 );
		if( ( rectangle.getX() - rectangle.getWidth() * 0.5 <= oval.getX() && oval.getX() <= rectangle.getX() + rectangle.getWidth() * 0.5 ) || ( rectangle.getY() - rectangle.getHeight() * 0.5 <= oval.getY() && oval.getY() <= rectangle.getY() + rectangle.getHeight() * 0.5 ) ) {
			if( 2.0 * Math.abs( oval.getX() - rectangle.getX() ) < oval.getWidth() + rectangle.getWidth() - inaccuracy && 2.0 * Math.abs( oval.getY() - rectangle.getY() ) < oval.getWidth() + rectangle.getHeight() - inaccuracy ) return true;
		} else {
			double dX = Math.abs( rectangle.getX() - oval.getX() ) - 0.5 * rectangle.getWidth();
			double dY = Math.abs( rectangle.getY() - oval.getY() ) - 0.5 * rectangle.getHeight();
			double radius = 0.5 * oval.getWidth() - inaccuracy;
			if( dX * dX + dY * dY < radius * radius ) return true;
		}
		return false;
	}
}
