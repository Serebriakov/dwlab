/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.base.Project;
import dwlab.shapes.Shape;

public class TimedMovementModel extends TemporaryModel {
	public double initialX, initialY;
	public double destinationX, destinationY;


	public TimedMovementModel( double time, double destinationX, double destinationY ) {
		this.period = time;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}


	@Override
	public void init( Shape shape ) {
		initialX = shape.getX();
		initialY = shape.getY();
	}


	@Override
	public void applyTo( Shape shape ) {
		double k = ( Project.current.time - startingTime ) / period;
		shape.setCoords( initialX + k * ( destinationX - initialX ), initialY + k * ( destinationY - initialY ) );
		super.applyTo( shape );
	}
}
