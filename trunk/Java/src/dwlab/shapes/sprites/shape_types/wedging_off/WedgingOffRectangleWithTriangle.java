package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Service.Margins;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffRectangleWithTriangle extends WedgingOffSprites {
	public static WedgingOffRectangleWithTriangle instance = new WedgingOffRectangleWithTriangle();

	private Margins margins = new Margins();
	
	
	@Override
	public void calculateVector( Sprite rectangle, Sprite triangle, Vector vector ) {
		double x;
		if( triangle.shapeType.getNum() == Sprite.topLeftTriangle.getNum() || triangle.shapeType.getNum() == Sprite.bottomLeftTriangle.getNum() ) {
			x = rectangle.leftX();
		} else {
			x = rectangle.rightX();
		}

		triangle.getHypotenuse( serviceLines[ 0 ] );
		if( triangle.shapeType.getNum() == Sprite.topLeftTriangle.getNum() || triangle.shapeType.getNum() == Sprite.topRightTriangle.getNum() ) {
			dY = Math.min( serviceLines[ 0 ].getY( x ), triangle.bottomY() ) - rectangle.topY();
		} else {
			dY = Math.max( serviceLines[ 0 ].getY( x ), triangle.topY() ) - rectangle.bottomY();
		}

		dX1d, dY1d;
		WedgingOffRectangleWithRectangle.instance.wedgeOffSprites( rectangle, triangle, dX1, dY1 );
		if( distance2( dX1, dY1 ) < dY * dY ) {
			dX = dX1;
			dY = dY1;
		}
	}
}
