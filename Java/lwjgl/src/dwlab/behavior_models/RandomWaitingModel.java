/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.base.service.Service;
import dwlab.shapes.Shape;

/**
 * This model is for waiting random period of time in some interval.
 * Same as LTFixedWaitingModel, but you can specify interval in which time period will be selected.

 * @see #fixedWaitingModel, #lTBehaviorModel example.
 */
public class RandomWaitingModel extends TemporaryModel {
	public double timeFrom, timeTo;

	public RandomWaitingModel( double timeFrom, double timeTo ) {
		this.timeFrom = timeFrom;
		this.timeTo = timeTo;
	}

	@Override
	public void init( Shape shape ) {
		super.init( shape );
		period = Service.random( timeFrom, timeTo );
	}
}
