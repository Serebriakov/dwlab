/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.line_segments.collision;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.collisions.PivotWithTriangleCollision;

public class TriangleWithLineSegmentCollision extends CollisionWithLineSegment {
	public static TriangleWithLineSegmentCollision instance = new TriangleWithLineSegmentCollision();

	
	@Override
	public boolean check( Sprite triangle, Sprite lSPivot1, Sprite lSPivot2 ) {
		if( PivotWithTriangleCollision.instance.check( lSPivot1, triangle ) ) return true;
		triangle.getOtherVertices( servicePivots[ 0 ], servicePivots[ 1 ] );
		triangle.getRightAngleVertex( servicePivots[ 2 ] );
		for( int n = 0; n <= 2; n++ ) if( lineSegment( servicePivots[ n ], servicePivots[ ( n + 1 ) % 3 ], lSPivot1, lSPivot2 ) ) return true;
		return false;
	}
}
