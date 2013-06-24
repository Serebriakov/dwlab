/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class RayWithRayCollision extends SpritesCollision {
	public static RayWithRayCollision instance = new RayWithRayCollision();

	@Override
	public boolean check( Sprite ray1, Sprite ray2 ) {
		ray1.toLine( serviceLine1 );
		ray2.toLine( serviceLine2 );
		serviceLine1.intersectionWithLine( serviceLine2, servicePivot1 );
		if( ! ray1.hasPivot( servicePivot1 ) ) return false;
		if( ray2.hasPivot( servicePivot1 ) ) return true; else return false;
	}
}
