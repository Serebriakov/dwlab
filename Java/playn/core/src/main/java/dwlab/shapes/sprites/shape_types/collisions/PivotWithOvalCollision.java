/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

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