package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.line_segments.collision.RayWithLineSegmentCollision;
import dwlab.shapes.sprites.Sprite;

public class RayWithTriangleCollision extends SpritesCollision {
	public static RayWithTriangleCollision instance = new RayWithTriangleCollision();

	
	@Override
	public boolean check( Sprite ray, Sprite triangle ) {
		triangle.getOtherVertices( servicePivots[ 0 ], servicePivots[ 1 ] );
		triangle.getRightAngleVertex( servicePivots[ 2 ] );
		for( int n = 0; n <= 2; n++ ) {
			if( RayWithLineSegmentCollision.instance.check( ray, servicePivots[ n ], servicePivots[ ( n + 1 ) % 3 ] ) ) return true;
		}
		return false;
	}
}
