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

public class TilePos extends Obj {
	public int dX, dY;
	public int tileNum;
	public int category;


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		dX = xMLObject.manageIntAttribute( "dx", dX );
		dY = xMLObject.manageIntAttribute( "dy", dY );
		tileNum = xMLObject.manageIntAttribute( "tilenum", tileNum );
	}
}
