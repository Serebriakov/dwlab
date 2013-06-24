/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;

public class DrawingPivotShape extends DrawingShape {
	public static DrawingPivotShape instance = new DrawingPivotShape();
	
	
	@Override
	public void perform( Sprite sprite, Color drawingColor, boolean empty ) {
		Camera.current.fieldToScreen( sprite, vector1 );
		Graphics.drawOval( vector1.x, vector1.y, 5d, 5d, 0d, drawingColor, false );
	}
}
