package dwlab.shapes.line_segments.collision;

import dwlab.shapes.sprites.Sprite;

public class RayWithLineSegmentCollision extends CollisionWithLineSegment {
	public static RayWithLineSegmentCollision instance = new RayWithLineSegmentCollision();

	@Override
	public boolean check( Sprite ray, Sprite lSPivot1, Sprite lSPivot2 ) {
		ray.toLine( serviceLine1 );
		if( serviceLine1.intersectionWithLineSegment( lSPivot1, lSPivot2, servicePivot1 ) != null ) {
			if( ray.hasPivot( servicePivot1 ) ) return true;
		}
		return false;
	}
}
