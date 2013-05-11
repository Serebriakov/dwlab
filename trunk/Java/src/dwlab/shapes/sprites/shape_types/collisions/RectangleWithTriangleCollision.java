package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.base.Service.Margins;
import dwlab.shapes.sprites.Sprite;

public class RectangleWithTriangleCollision extends SpritesCollision {
	public static RectangleWithTriangleCollision instance = new RectangleWithTriangleCollision();
	
	
	@Override
	public boolean check( Sprite rectangle, Sprite triangle ) {
		if( RectangleWithRectangleCollision.instance.check( rectangle, triangle ) ) {
			triangle.getHypotenuse( serviceLine1 );
			triangle.getRightAngleVertex( servicePivot1 );
			if( serviceLine1.pivotOrientation( rectangle ) == serviceLine1.pivotOrientation( servicePivot1 ) ) return true;
			rectangle.getBounds( margins );
			int o = serviceLine1.pointOrientation( margins.min.x, margins.min.y );
			if( o != serviceLine1.pointOrientation( margins.max.x, margins.min.y ) ) return true;
			if( o != serviceLine1.pointOrientation( margins.min.x, margins.max.y ) ) return true;
			if( o != serviceLine1.pointOrientation( margins.max.x, margins.max.y ) ) return true;
		}
		return false;
	}
}
