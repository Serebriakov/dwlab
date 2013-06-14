package dwlab.shapes;

import dwlab.base.Obj;
import dwlab.base.XMLObject;

public class Parameter extends Obj {
	public String name;
	public String value;


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		name = xMLObject.manageStringAttribute( "name", name );
		value = xMLObject.manageStringAttribute( "value", value );
	}
}
