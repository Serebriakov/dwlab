/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.behavior_models;

import dwlab.base.Project;
import dwlab.shapes.Shape;

public class ValueChangingModel<E extends Shape> extends TemporaryModel<E> {
	public double initialValue, destinationValue;


	@Override
	public void applyTo( E shape ) {
		changeValue( shape, initialValue + ( Project.current.time - startingTime ) / period * ( destinationValue - initialValue ) );
		super.applyTo( shape );
	}


	public void changeValue( E shape, double newValue ) {
	}
}
