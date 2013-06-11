package dwlab.shapes.maps.tilemaps;

import dwlab.base.Obj;
import dwlab.base.XMLObject;
import java.util.LinkedList;

public class TileCategory extends Obj {
	public String name;
	public int num;
	public LinkedList<TileRule> tileRules = new LinkedList<TileRule>();


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );

		name = xMLObject.manageStringAttribute( "name", name );
		tileRules = xMLObject.manageChildList( tileRules );
	}
}