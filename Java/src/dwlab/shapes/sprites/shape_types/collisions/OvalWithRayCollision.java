/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class OvalWithRayCollision extends SpritesCollision {
	public static OvalWithRayCollision instance = new OvalWithRayCollision();

	
	@Override
	public boolean check( Sprite oval, Sprite ray ) {
		ray.toLine( serviceLine1 );
		oval.toCircleUsingLine( serviceLine1, serviceOval1 );
		if( serviceLine1.collisionPointsWithCircle( serviceOval1, servicePivot1, servicePivot2 ) ) {
			if( ray.hasPivot( servicePivot1 ) ) return true;
			if( ray.hasPivot( servicePivot2 ) ) return true;
		}
		if( PivotWithOvalCollision.instance.check( ray, oval ) ) return true; else return false;
	}
}
