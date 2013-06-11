package dwlab.shapes;

import dwlab.base.Obj;
import dwlab.base.XMLObject;

public class Parameter extends Obj {
	public String name;
	public String value;


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		xMLObject.manageStringAttribute( "name", name );
		xMLObject.manageStringAttribute( "value", value );
	}
}
