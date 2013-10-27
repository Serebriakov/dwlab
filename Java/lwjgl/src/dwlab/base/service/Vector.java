/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base.service;

import dwlab.base.Obj;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.Camera;

public class Vector extends Obj {
	public double x, y;
	
	
	public Vector( double x, double y ) {
		this.x = x;
		this.y = y;
	}
		
	
	public Vector() {
		this.x = 0d;
		this.y = 0d;
	}
		
	
	public static Vector fieldToScreen( Shape shape ) {
		Vector vector = new Vector();
		Camera.current.fieldToScreen( shape, vector );
		return vector;
	}
		
	
	public static Vector fieldToScreen( double x, double y ) {
		Vector vector = new Vector();
		Camera.current.fieldToScreen( x, y, vector );
		return vector;
	}
	
	
	public Vector set( double x, double y ) {
		this.x = x;
		this.y = y;
		return this;
	}


	public void roundCoords() {
		x = Math.round( x );
		y = Math.round( y );
	}

	
	public double length2() {
		return x * x + y * y;
	}
	
	
	public double distanceTo( Vector vector ) {
		double dX = x - vector.x;
		double dY = y - vector.y;
		return Math.sqrt( dX * dX + dY * dY );
	}
}
