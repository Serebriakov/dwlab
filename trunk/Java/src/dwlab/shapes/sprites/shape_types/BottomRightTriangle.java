/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types;

public class BottomRightTriangle extends TopLeftTriangle {
	protected BottomRightTriangle() {
	}
	
	
	@Override
	public int getNum() {
		return 7;
	}
	

	@Override
	public String getName() {
		return "Bottom-right triangle";
	}
}
