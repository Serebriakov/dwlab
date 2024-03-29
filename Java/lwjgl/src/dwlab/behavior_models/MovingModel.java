/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.shapes.sprites.Sprite;

public class MovingModel extends ChainedModel<Sprite> {
	public double x, y;


	public MovingModel( double x, double y ) {
		this.x = x;
		this.y = y;
	}


	@Override
	public void applyTo( Sprite sprite ) {
		sprite.moveTowards( x, y, sprite.velocity );
		if( sprite.isAtPositionOf( x, y ) ) sprite.removeModel( this );
	}
}
