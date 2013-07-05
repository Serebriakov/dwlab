/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.base.Obj;
import dwlab.base.Project;
import dwlab.shapes.Shape;

/**
 * Behavior model is the object which can be attached to the shape and affect its state.
 */
public class BehaviorModel<E extends Shape> extends Obj {
	public boolean active;


	/**
	 * Initialization method.
	 * It will be executed when model will be attached to shape.
	 * Fill it with model initialization commands. 
	 */
	public void init( E shape ) {
	}


	/**
	 * Activation method.
	 * It will be executed when model will be activated (and when attached too if you didn't set activation flag to False).
	 * 
	 * @see #deactivate, #activateAllModels, #deactivateAllModels, #activateModel, #deactivateModel
	 */
	public void activate( E shape ) {
	}


	/**
	 * Deactivation method.
	 * It will be executed when model will be activated (and when removed too if it was active).
	 * 
	 * @see #activate, #activateAllModels, #deactivateAllModels, #activateModel, #deactivateModel
	 */
	public void deactivate( E shape ) {
	}


	/**
	 * Watching method.
	 * This method will be executed by shape default Act() method if the model will be inactive.
	 * Fill it with commands which will check certain conditions and activate model.
	 * 
	 * @see #applyTo, #act
	 */
	public void watch( E shape ) {
	}


	/**
	 * Model applying method.
	 * This method will be executed by shape default Act() method if the model will be active.
	 * Fill it with commands which are affect shape in the way of corresponding behavior.
	 * 
	 * @see #watch, #act
	 */
	public void applyTo( E shape ) {
	}


	/**
	 * Activates behavior model.
	 * For use inside model's methods.
	 * 
	 * @see #activate, #deactivate, #activateAllModels, #deactivateAllModels, #deactivateModel
	 */
	public final void activateModel( E shape ) {
		if( ! active ) {
			activate( shape );
			active = true;
		}
	}


	/**
	 * Deactivates behavior model.
	 * For use inside model's methods.
	 * 
	 * @see #activate, #deactivate, #activateAllModels, #deactivateAllModels, #activateModel
	 */
	public final void deactivateModel( E shape ) {
		if( active ) {
			deactivate( shape );
			active = false;
		}
	}

	
	private class BehaviorModelRemover extends Obj {
		BehaviorModel model;
		Shape shape;
		
		public BehaviorModelRemover( Shape shape, BehaviorModel model ) {
			this.shape = shape;
			this.model = model;
		}
		
		@Override
		public void act() {
			shape.behaviorModels.remove( model );
		}
	}
	
	
	public final void remove( E shape ) {
		if( active ) deactivate( shape );
		Project.managers.add( new BehaviorModelRemover( shape, this ) );
	}
	

	public String info( E shape ) {
		return "";
	}
}
