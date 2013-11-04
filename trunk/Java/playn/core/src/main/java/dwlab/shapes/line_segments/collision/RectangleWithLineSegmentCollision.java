/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.line_segments.collision;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.collisions.PivotWithRectangleCollision;

public class RectangleWithLineSegmentCollision extends CollisionWithLineSegment {
	public static RectangleWithLineSegmentCollision instance = new RectangleWithLineSegmentCollision();

	
	@Override
	public boolean check( Sprite rectangle, Sprite lSPivot1, Sprite lSPivot2 ) {
		if( PivotWithRectangleCollision.instance.check( lSPivot1, rectangle ) ) return true;
		rectangle.getBounds( servicePivots[ 0 ], servicePivots[ 0 ], servicePivots[ 2 ], servicePivots[ 2 ] );
		rectangle.getBounds( servicePivots[ 1 ], servicePivots[ 3 ], servicePivots[ 3 ], servicePivots[ 1 ] );
		for( int n = 0; n <= 3; n++ ) if( lineSegment( servicePivots[ n ], servicePivots[ ( n + 1 ) % 4 ], lSPivot1, lSPivot2 ) ) return true;
		return false;
	}
}	