/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.behavior_models;

import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;

/**
 * This model plays animation of the sprite.
 * You can specify parameters from LTSprite's Animate() method and add models which will be executed after animation will end to the
 * NextModels parameter. Though if animation is looped it will be played forever.

 * @see #lTModelStack, #lTBehaviorModel example.
 */
public class AnimationModel extends ChainedModel<Sprite> {
	public double startingTime;
	public boolean looped;
	public double speed;
	public int framesQuantity;
	public int frameStart;
	public boolean pingPong;


	public AnimationModel( boolean looped, double speed, int framesQuantity, int frameStart, boolean pingPong ) {
		this.speed = speed;
		this.looped = looped;
		this.framesQuantity = framesQuantity;
		this.frameStart = frameStart;
		this.pingPong = pingPong;
	}

	
	public AnimationModel( boolean looped, double speed, int framesQuantity, int frameStart ) {
		this.speed = speed;
		this.looped = looped;
		this.framesQuantity = framesQuantity;
		this.frameStart = frameStart;
		this.pingPong = false;
	}
	

	@Override
	public void activate( Sprite sprite ) {
		startingTime = Project.current.time;
	}


	@Override
	public void applyTo( Sprite sprite ) {
		if( ! looped ) {
			if( Project.current.time > startingTime + speed * ( framesQuantity + ( pingPong ? framesQuantity - 2 : 0 ) ) ) {
				deactivateModel( sprite );
				return;
			}
		}
		sprite.animate( speed, framesQuantity, frameStart, startingTime, pingPong );
	}


	@Override
	public String info( Sprite shape ) {
		return String.valueOf( shape.frame );
	}
}
