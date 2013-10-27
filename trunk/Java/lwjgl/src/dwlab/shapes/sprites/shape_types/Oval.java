/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types;

import dwlab.shapes.sprites.Sprite;

public class Oval extends ShapeType {
	private static Sprite serviceOval = new Sprite( ShapeType.oval );

	
	protected Oval() {
	}
	
	
	@Override
	public int getNum() {
		return 1;
	}

	
	@Override
	public String getName() {
		return "Oval";
	}

	
	@Override
	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		serviceOval.setCoords( sprite.getX() * xScale + dX, sprite.getY() * yScale + dY );
		serviceOval.setSize( sprite.getWidth() * xScale, sprite.getHeight() * yScale );
		return serviceOval;
	}
}
