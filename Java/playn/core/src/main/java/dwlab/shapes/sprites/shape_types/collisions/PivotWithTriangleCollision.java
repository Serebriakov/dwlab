/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class PivotWithTriangleCollision extends SpritesCollision {
	public static PivotWithTriangleCollision instance = new PivotWithTriangleCollision();

	
	@Override
	public boolean check( Sprite pivot, Sprite triangle ) {
		if( PivotWithRectangleCollision.instance.check( pivot, triangle ) ) {
			triangle.getHypotenuse( serviceLine1 );
			triangle.getRightAngleVertex( servicePivot1 );
			if( serviceLine1.pivotOrientation( pivot ) == serviceLine1.pivotOrientation( servicePivot1 ) ) return true;
		}
		return false;
	}
}