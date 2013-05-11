package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class OvalOverlapsOval extends SpritesOverlapping {
	public static OvalOverlapsOval instance = new OvalOverlapsOval();


	@Override
	public boolean check( Sprite circle, Sprite oval ) {
		if( oval.getWidth() == oval.getHeight() ) return circleAndCircle( circle, serviceOval1 );

		if( oval.getWidth() > oval.getHeight() ) {
			double dWidth = oval.getWidth() - oval.getHeight();
			serviceOval1.setCoords( oval.getX() - dWidth, oval.getY() );
			serviceOval1.setWidth( oval.getHeight() );
			if( ! circleAndCircle( circle, serviceOval1 ) ) return false;
			serviceOval1.setX( oval.getX() + dWidth );
		} else {
			double dHeight = oval.getHeight() - oval.getWidth();
			serviceOval1.setCoords( oval.getX(), oval.getY() - dHeight );
			serviceOval1.setWidth( oval.getWidth() );
			if( !circleAndCircle( circle, serviceOval1 ) ) return false;
			serviceOval1.setY( oval.getY() + dHeight );
		}
		return circleAndCircle( circle, serviceOval1 );
	}

	public boolean circleAndCircle( Sprite circle1, Sprite circle2 ) {
		double diameters = circle1.getWidth() + circle2.getWidth();
		if( 4d * circle1.distance2to( circle2 ) <= diameters * diameters ) return true; else return false;
	}
}
