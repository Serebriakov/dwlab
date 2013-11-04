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

public class WedgingOffPivotWithOval extends WedgingOffSprites {
	public static WedgingOffPivotWithOval instance = new WedgingOffPivotWithOval();

	
	@Override
	public void calculateVector( Sprite pivot, Sprite oval, Vector vector ) {
		oval = oval.toCircle( pivot, serviceOval1 );
		double k = 0.5 * oval.getWidth() / oval.distanceTo( pivot ) - 1.0;
		vector.x = ( pivot.getX() - oval.getX() ) * k;
		vector.y = ( pivot.getY() - oval.getY() ) * k;
	}
}