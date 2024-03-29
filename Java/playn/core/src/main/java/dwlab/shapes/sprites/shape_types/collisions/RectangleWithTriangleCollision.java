/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.base.service.Service.Margins;
import dwlab.shapes.sprites.Sprite;

public class RectangleWithTriangleCollision extends SpritesCollision {
	public static RectangleWithTriangleCollision instance = new RectangleWithTriangleCollision();
	
	
	@Override
	public boolean check( Sprite rectangle, Sprite triangle ) {
		if( RectangleWithRectangleCollision.instance.check( rectangle, triangle ) ) {
			triangle.getHypotenuse( serviceLine1 );
			triangle.getRightAngleVertex( servicePivot1 );
			if( serviceLine1.pivotOrientation( rectangle ) == serviceLine1.pivotOrientation( servicePivot1 ) ) return true;
			rectangle.getBounds( margins );
			int o = serviceLine1.pointOrientation( margins.min.x, margins.min.y );
			if( o != serviceLine1.pointOrientation( margins.max.x, margins.min.y ) ) return true;
			if( o != serviceLine1.pointOrientation( margins.min.x, margins.max.y ) ) return true;
			if( o != serviceLine1.pointOrientation( margins.max.x, margins.max.y ) ) return true;
		}
		return false;
	}
}
