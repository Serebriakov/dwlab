package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Service;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class WedgingOffRectangleWithTriangle extends WedgingOffSprites {
	public static WedgingOffRectangleWithTriangle instance = new WedgingOffRectangleWithTriangle();
	
	
	
	@Override
	public void calculateVector( Sprite rectangle, Sprite triangle, Vector vector ) {
		double x;
		if( triangle.shapeType.getNum() == ShapeType.topLeftTriangle.getNum() || triangle.shapeType.getNum() == ShapeType.bottomLeftTriangle.getNum() ) {
			x = rectangle.leftX();
		} else {
			x = rectangle.rightX();
		}

		triangle.getHypotenuse( serviceLines[ 0 ] );
		if( triangle.shapeType.getNum() == ShapeType.topLeftTriangle.getNum() || triangle.shapeType.getNum() == ShapeType.topRightTriangle.getNum() ) {
			vector.y = Math.min( serviceLines[ 0 ].getY( x ), triangle.bottomY() ) - rectangle.topY();
		} else {
			vector.y = Math.max( serviceLines[ 0 ].getY( x ), triangle.topY() ) - rectangle.bottomY();
		}

		WedgingOffRectangleWithRectangle.instance.calculateVector( rectangle, triangle, vector1 );
		if( Service.distance2( vector1.x, vector1.y ) < vector.y * vector.y ) {
			vector.x = vector1.x;
			vector.y = vector1.y;
		}
	}
}
