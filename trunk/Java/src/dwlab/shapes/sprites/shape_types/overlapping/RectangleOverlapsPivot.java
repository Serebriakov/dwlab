package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class RectangleOverlapsPivot extends SpritesOverlapping {
	public static RectangleOverlapsPivot instance = new RectangleOverlapsPivot();

	
	@Override
	public boolean check( Sprite circle, Sprite pivot ) {
		if( 4d * circle.distance2to( pivot ) <= circle.getWidth() * circle.getWidth() ) return true; else return false;
	}
}
