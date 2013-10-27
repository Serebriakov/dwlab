/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class RectangleOverlapsRectangle extends SpritesOverlapping {
	public static RectangleOverlapsRectangle instance = new RectangleOverlapsRectangle();


	@Override
	public boolean check( Sprite rectangle1, Sprite rectangle2 ) {
		if( ( rectangle1.getX() - 0.5d * rectangle1.getWidth() <= rectangle2.getX() - 0.5d * rectangle2.getWidth() ) &&
				( rectangle1.getY() - 0.5d * rectangle1.getHeight() <= rectangle2.getY() - 0.5d * rectangle2.getHeight() ) &&
				( rectangle1.getX() + 0.5d * rectangle1.getWidth() >= rectangle2.getX() + 0.5d * rectangle2.getWidth() ) &&
				( rectangle1.getY() + 0.5d * rectangle1.getHeight() >= rectangle2.getY() + 0.5d * rectangle2.getHeight() ) ) return true; else return false;
	}
}
