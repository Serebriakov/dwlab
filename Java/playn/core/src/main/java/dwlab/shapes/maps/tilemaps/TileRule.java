/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.maps.tilemaps;

import dwlab.base.Obj;
import dwlab.base.XMLObject;
import java.util.LinkedList;

public class TileRule extends Obj {
	public int tileNums[];
	public LinkedList<TilePos> tilePositions = new LinkedList<TilePos>();
	public int x, y;
	public int xDivider = 1, yDivider = 1;


	public int tilesQuantity() {
		return tileNums.length;
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		tileNums = xMLObject.manageIntArrayAttribute( "tilenums", tileNums );
		x = xMLObject.manageIntAttribute( "x", x );
		y = xMLObject.manageIntAttribute( "y", y );
		xDivider = xMLObject.manageIntAttribute( "xdiv", xDivider, 1 );
		yDivider = xMLObject.manageIntAttribute( "ydiv", yDivider, 1 );
		tilePositions = xMLObject.manageChildList( tilePositions );
	}
}