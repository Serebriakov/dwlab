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
