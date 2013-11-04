/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;

public class RectangleWithRectangleCollision extends SpritesCollision {
	public static RectangleWithRectangleCollision instance = new RectangleWithRectangleCollision();

	
	@Override
	public boolean check( Sprite rectangle1, Sprite rectangle2 ) {
		if( 2d * Math.abs( rectangle1.getX() - rectangle2.getX() ) < rectangle1.getWidth() + rectangle2.getWidth() - inaccuracy && 
				2d * Math.abs( rectangle1.getY() - rectangle2.getY() ) < rectangle1.getHeight() + rectangle2.getHeight() - inaccuracy )return true; else return false;
	}
}
