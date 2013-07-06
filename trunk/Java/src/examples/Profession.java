package examples;

import dwlab.base.Obj;
import dwlab.base.XMLObject;

public class Profession extends Obj {
	public String name;

	public static Profession create( String name ) {
		Profession profession = new Profession();
		profession.name = name;
		XMLIOExample.professions.addLast( profession );
		return profession;
	}

	@Override
	public void xMLIO( XMLObject xMLObject ) {
		super.xMLIO( xMLObject );
		xMLObject.manageStringAttribute( "name", name );
	}
}
