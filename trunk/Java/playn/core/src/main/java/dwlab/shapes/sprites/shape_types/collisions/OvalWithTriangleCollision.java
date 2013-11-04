/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class OvalWithTriangleCollision extends SpritesCollision {
	public static OvalWithTriangleCollision instance = new OvalWithTriangleCollision();

	
	@Override
	public boolean check( Sprite oval, Sprite triangle ) {
		if( OvalWithRectangleCollision.instance.check( oval, triangle ) ) {
			triangle.getHypotenuse( serviceLine1 );
			oval = oval.toCircleUsingLine( serviceLine1, serviceOval1 );
			triangle.getRightAngleVertex( servicePivot1 );
			if( serviceLine1.pivotOrientation( oval ) == serviceLine1.pivotOrientation( servicePivot1 ) ) return true;
			if( ! serviceLine1.collisionPointsWithCircle( oval, servicePivot1, servicePivot2 ) ) return false;
			if( PivotWithRectangleCollision.instance.check( servicePivot1, triangle ) ||
					PivotWithRectangleCollision.instance.check( servicePivot2, triangle ) ) return true;
		}
		return false;
	}
}
