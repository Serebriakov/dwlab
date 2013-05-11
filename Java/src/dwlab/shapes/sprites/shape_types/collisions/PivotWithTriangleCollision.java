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