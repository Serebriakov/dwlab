/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class OvalOverlapsTriangle extends SpritesOverlapping {
	public static OvalOverlapsTriangle instance = new OvalOverlapsTriangle();


	@Override
	public boolean check( Sprite circle, Sprite triangle ) {
		triangle.getRightAngleVertex( servicePivot1 );
		if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
		triangle.getOtherVertices( servicePivot1, servicePivot2 );
		if( ! OvalOverlapsPivot.instance.check( circle, servicePivot1 ) ) return false;
		if( ! OvalOverlapsPivot.instance.check( circle, servicePivot2 ) ) return false;
		return true;
	}
}
