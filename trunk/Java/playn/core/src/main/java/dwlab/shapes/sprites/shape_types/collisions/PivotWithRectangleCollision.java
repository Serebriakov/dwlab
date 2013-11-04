/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

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