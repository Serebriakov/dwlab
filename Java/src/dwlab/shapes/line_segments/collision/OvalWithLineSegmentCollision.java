package dwlab.shapes.line_segments.collision;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.collisions.OvalWithOvalCollision;
import dwlab.shapes.sprites.shape_types.collisions.PivotWithOvalCollision;

public class OvalWithLineSegmentCollision extends CollisionWithLineSegment {
	public static OvalWithLineSegmentCollision instance = new OvalWithLineSegmentCollision();

	
	public boolean check( Sprite oval, Sprite lSPivot1, Sprite lSPivot2 ) {
		serviceOval1.setCoords( 0.5d * ( lSPivot1.getX() + lSPivot2.getX() ), 0.5d * ( lSPivot1.getY() + lSPivot2.getY() ) );
		serviceOval1.setWidth( 0.5 * lSPivot1.distanceTo( lSPivot2 ) );
		if( OvalWithOvalCollision.instance.check( oval, serviceOval1 ) ) {
			serviceLine1.usePivots( lSPivot1, lSPivot2 );
			oval = oval.toCircleUsingLine( serviceLine1, serviceOval2 );
			if( serviceLine1.distanceTo( oval ) < 0.5 * Math.max( oval.getWidth(), oval.getHeight() ) - inaccuracy ) {
				serviceLine1.pivotProjection( oval, servicePivot1 );
				if( PivotWithOvalCollision.instance.check( servicePivot1, serviceOval1 ) &&
								servicePivot1.distanceTo( serviceOval2 ) < serviceOval1.getWidth() - inaccuracy ) return true;
			}
		}
		return false;
	}
}
