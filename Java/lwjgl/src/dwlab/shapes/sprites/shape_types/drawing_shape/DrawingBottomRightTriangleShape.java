/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.platform.Platform;
import dwlab.visualizers.Color;

public class DrawingBottomRightTriangleShape extends DrawingShape {
	public static DrawingBottomRightTriangleShape instance = new DrawingBottomRightTriangleShape();
	
	
	@Override
	public void perform( Color drawingColor, boolean empty ) {
		vector2.x *= 0.5d;
		vector2.y *= 0.5d;
		Platform.current.startPolygon( 3, drawingColor, empty );
		Platform.current.addPolygonVertex( vector1.x + vector2.x, vector1.y - vector2.y );
		Platform.current.addPolygonVertex( vector1.x + vector2.x, vector1.y + vector2.y );
		Platform.current.addPolygonVertex( vector1.x - vector2.x, vector1.y + vector2.y );
		Platform.current.drawPolygon();
	}
}
