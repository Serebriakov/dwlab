/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;

public class WedgingOffRectangleWithRectangle extends WedgingOffSprites {
	public static WedgingOffRectangleWithRectangle instance = new WedgingOffRectangleWithRectangle();

	
	@Override
	public void calculateVector( Sprite rectangle1, Sprite rectangle2, Vector vector ) {
		vector.x = 0.5 * ( rectangle1.getWidth() + rectangle2.getWidth() ) - Math.abs( rectangle1.getX() - rectangle2.getX() );
		vector.y = 0.5 * ( rectangle1.getHeight() + rectangle2.getHeight() ) - Math.abs( rectangle1.getY() - rectangle2.getY() );

		if( vector.x < vector.y ) {
			vector.x *= Math.signum( rectangle1.getX() - rectangle2.getX() );
			vector.y = 0;
		} else {
			vector.x = 0;
			vector.y *= Math.signum( rectangle1.getY() - rectangle2.getY() );
		}
	}
}
