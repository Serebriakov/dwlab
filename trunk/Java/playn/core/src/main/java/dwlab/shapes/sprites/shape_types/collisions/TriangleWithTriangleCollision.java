/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.line_segments.collision.CollisionWithLineSegment;
import dwlab.shapes.sprites.Sprite;

public class TriangleWithTriangleCollision extends SpritesCollision {
	public static TriangleWithTriangleCollision instance = new TriangleWithTriangleCollision();

	@Override
	public boolean check( Sprite triangle1, Sprite triangle2 ) {
		if( RectangleWithRectangleCollision.instance.check( triangle1, triangle2 ) ) {
			triangle1.getRightAngleVertex( servicePivot3 );
			triangle2.getRightAngleVertex( servicePivot4 );

			triangle1.getOtherVertices( servicePivot1, servicePivot2 );
			triangle2.getHypotenuse( serviceLine1 );
			int o1 = serviceLine1.pivotOrientation( servicePivot4 );
			if( PivotWithRectangleCollision.instance.check( servicePivot1, triangle2 ) ) if( o1 == serviceLine1.pivotOrientation( servicePivot1 ) ) return true;
			if( PivotWithRectangleCollision.instance.check( servicePivot2, triangle2 ) ) if( o1 == serviceLine1.pivotOrientation( servicePivot2 ) ) return true;
			if( PivotWithRectangleCollision.instance.check( servicePivot3, triangle2 ) ) if( o1 == serviceLine1.pivotOrientation( servicePivot3 ) ) return true;
			boolean o3 = ( serviceLine1.pivotOrientation( servicePivot3 ) != serviceLine1.pivotOrientation( servicePivot1 ) );

			triangle2.getOtherVertices( servicePivots[ 0 ], servicePivots[ 1 ] );
			triangle1.getHypotenuse( serviceLine1 );
			int o2 = serviceLine1.pivotOrientation( servicePivot3 );
			if( PivotWithRectangleCollision.instance.check( servicePivots[ 0 ], triangle1 ) ) if( o2 == serviceLine1.pivotOrientation( servicePivots[ 0 ] ) ) return true;
			if( PivotWithRectangleCollision.instance.check( servicePivots[ 1 ], triangle1 ) ) if( o2 == serviceLine1.pivotOrientation( servicePivots[ 1 ] ) ) return true;
			if( PivotWithRectangleCollision.instance.check( servicePivot4, triangle1 ) ) if( o2 == serviceLine1.pivotOrientation( servicePivot4 ) ) return true;

			if( CollisionWithLineSegment.lineSegment( servicePivot1, servicePivot2, servicePivots[ 0 ], servicePivots[ 1 ] ) ) return true;
			if( o3 ) if( serviceLine1.pivotOrientation( servicePivot4 ) != serviceLine1.pivotOrientation( servicePivots[ 0 ] ) ) return true;
		}
		return false;
	}
}
