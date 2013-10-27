/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class WedgingOffRectangleWithTriangle extends WedgingOffSprites {
	public static WedgingOffRectangleWithTriangle instance = new WedgingOffRectangleWithTriangle();
	
	
	
	@Override
	public void calculateVector( Sprite rectangle, Sprite triangle, Vector vector ) {
		double x;
		if( triangle.shapeType == ShapeType.topLeftTriangle || triangle.shapeType == ShapeType.bottomLeftTriangle ) {
			x = rectangle.leftX();
		} else {
			x = rectangle.rightX();
		}

		triangle.getHypotenuse( serviceLines[ 0 ] );
		if( triangle.shapeType == ShapeType.topLeftTriangle || triangle.shapeType == ShapeType.topRightTriangle ) {
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
