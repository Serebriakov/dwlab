package dwlab.behavior_models;

import dwlab.shapes.sprites.VectorSprite;

public class HorizontalMovementModel extends BehaviorModel<VectorSprite> {
	@Override
	public void applyTo( VectorSprite sprite ) {
		sprite.move( sprite.dX, 0d );
	}
}