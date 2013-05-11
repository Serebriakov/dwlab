package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class OvalOverlapsTriangle extends SpritesOverlapping {
	public static OvalOverlapsTriangle instance = new OvalOverlapsTriangle();


	public boolean check( Sprite circle, Sprite triangle ) {
		triangle.getRightAngleVertex( servicePivot1 );
		if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
		triangle.getOtherVertices( servicePivot1, servicePivot2 );
		if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
		if( ! OvalOverlapsPivot.instance.check( circle, servicePivot2 ) ) return false;
		return true;
	}


}
