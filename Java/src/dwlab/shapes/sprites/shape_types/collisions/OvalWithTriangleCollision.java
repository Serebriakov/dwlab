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
