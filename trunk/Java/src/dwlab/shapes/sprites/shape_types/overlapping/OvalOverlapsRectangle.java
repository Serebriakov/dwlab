package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class OvalOverlapsRectangle extends SpritesOverlapping {
	public static OvalOverlapsRectangle instance = new OvalOverlapsRectangle();


	@Override
	public boolean check( Sprite circle, Sprite rectangle ) {
		if( RectangleOverlapsRectangle.instance.check( circle, rectangle ) ) {
			rectangle.getBounds( margins );
			servicePivot1.setCoords( margins.min.x, margins.min.y );
			if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
			servicePivot1.setX( margins.max.x );
			if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
			servicePivot1.setY( margins.max.y );
			if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
			servicePivot1.setX( margins.min.x );
			if( OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return true;
		}
		return false;
	}


}
