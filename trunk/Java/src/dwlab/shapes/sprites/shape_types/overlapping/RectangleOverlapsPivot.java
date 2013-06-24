/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class RectangleOverlapsPivot extends SpritesOverlapping {
	public static RectangleOverlapsPivot instance = new RectangleOverlapsPivot();

	
	@Override
	public boolean check( Sprite circle, Sprite pivot ) {
		if( 4d * circle.distance2to( pivot ) <= circle.getWidth() * circle.getWidth() ) return true; else return false;
	}
}
