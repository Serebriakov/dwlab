package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class RayWithRayCollision extends SpritesCollision {
	public static RayWithRayCollision instance = new RayWithRayCollision();

	@Override
	public boolean check( Sprite ray1, Sprite ray2 ) {
		ray1.toLine( serviceLine1 );
		ray2.toLine( serviceLine2 );
		serviceLine1.intersectionWithLine( serviceLine2, servicePivot1 );
		if( ! ray1.hasPivot( servicePivot1 ) ) return false;
		if( ray2.hasPivot( servicePivot1 ) ) return true; else return false;
	}
}
