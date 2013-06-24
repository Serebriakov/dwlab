/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.service.Service;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;

public class DrawingRayShape extends DrawingShape {
	public static DrawingRayShape instance = new DrawingRayShape();
	
	
	@Override
	public void perform( Sprite sprite, Color drawingColor, boolean empty ) {
		Camera.current.fieldToScreen( sprite, vector1 );
		double ang = Service.wrap( sprite.angle, 360.0 );
		if( ang < 45.0 || ang >= 315.0 ) {
			double width = Camera.current.viewport.rightX() - vector1.x;
			if( width > 0 ) Graphics.drawLine( vector1.x, vector1.y, vector1.x + width, vector1.y + width * Math.tan( ang ), drawingColor );
		} else if( ang < 135.0 ) {
			double height = Camera.current.viewport.bottomY() - vector1.y;
			if( height > 0 ) Graphics.drawLine( vector1.x, vector1.y, vector1.x + height / Math.tan( ang ), vector1.y + height, drawingColor );
		} else if( ang < 225.0 ) {
			double width = Camera.current.viewport.leftX() - vector1.x;
			if( width < 0 ) Graphics.drawLine( vector1.x, vector1.y, vector1.x + width, vector1.y + width * Math.tan( ang ), drawingColor );
		} else {
			double height = Camera.current.viewport.topY() - vector1.y;
			if( height < 0 ) Graphics.drawLine( vector1.x, vector1.y, vector1.x + height / Math.tan( ang ), vector1.y + height, drawingColor );
		}
	}
}
