package dwlab.behavior_models;

import dwlab.shapes.sprites.VectorSprite;

public class VerticalMovementModel extends BehaviorModel<VectorSprite> {
	@Override
	public void applyTo( VectorSprite sprite ) {
		sprite.move( 0d, sprite.dY );
	}
}