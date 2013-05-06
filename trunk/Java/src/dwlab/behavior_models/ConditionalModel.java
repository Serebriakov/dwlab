/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.shapes.Shape;
import java.util.LinkedList;

public class ConditionalModel<E extends Shape> extends BehaviorModel<E> {
	public LinkedList<BehaviorModel> trueModels = new LinkedList<BehaviorModel>();
	public LinkedList<BehaviorModel> falseModels = new LinkedList<BehaviorModel>();


	public boolean condition( E shape ) {
		return false;
	}


	@Override
	public void applyTo( E shape ) {
		shape.removeModel( this );
		if( condition( shape ) ) {
			shape.attachModels( trueModels );
		} else {
			shape.attachModels( falseModels );
		}
	}
}
