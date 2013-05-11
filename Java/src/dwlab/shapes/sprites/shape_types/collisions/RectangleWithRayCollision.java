package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.line_segments.collision.RayWithLineSegmentCollision;
import dwlab.shapes.sprites.Sprite;

public class RectangleWithRayCollision extends SpritesCollision {
	public static RectangleWithRayCollision instance = new RectangleWithRayCollision();

	
	@Override
	public boolean check( Sprite rectangle, Sprite ray ) {
		rectangle.getBounds( servicePivots[ 0 ], servicePivots[ 0 ], servicePivots[ 2 ], servicePivots[ 2 ] );
		rectangle.getBounds( servicePivots[ 1 ], servicePivots[ 3 ], servicePivots[ 3 ], servicePivots[ 1 ] );
		for( int n = 0; n <= 3; n++ ) {
			if( RayWithLineSegmentCollision.instance.check( ray, servicePivots[ n ], servicePivots[ ( n + 1 ) % 4 ] ) ) return true;
		}
		return false;
	}
}
