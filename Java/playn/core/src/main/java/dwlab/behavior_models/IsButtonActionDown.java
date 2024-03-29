/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.controllers.ButtonAction;
import dwlab.shapes.Shape;

/**
 * This model checks if button action is down and attaches corresponding lists of models to the shape.
 * Same as LTIsModelActive, but TrueModels will be attached if button action is down and FalseModels otherwise.

 * @see #lTBehaviorModel example.
 */
public class IsButtonActionDown extends ConditionalModel {
	public ButtonAction buttonAction;


	public static IsButtonActionDown create( ButtonAction buttonAction ) {
		IsButtonActionDown behaviorModel = new IsButtonActionDown();
		behaviorModel.buttonAction = buttonAction;
		return behaviorModel;
	}


	@Override
	public boolean condition( Shape shape ) {
		return buttonAction.isDown();
	}


	@Override
	public String info( Shape shape ) {
		return buttonAction.name;
	}
}
