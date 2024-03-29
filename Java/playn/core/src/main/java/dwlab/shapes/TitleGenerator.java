/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes;

public class TitleGenerator {
	public static TitleGenerator current = new TitleGenerator();
	
	public String getTitle( Shape shape ) {
		String title = shape.getParameter( "name" );
		if( !title.isEmpty() ) return title;
		title = shape.getParameter( "class" );
		if( !title.isEmpty() ) return title;
		return shape.getClassTitle();
	}
}
