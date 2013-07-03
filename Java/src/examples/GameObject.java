package examples;

import dwlab.behavior_models.AnimationModel;
import dwlab.shapes.sprites.VectorSprite;

public class GameObject extends VectorSprite {
	BehaviorModelExample.OnLand onLand = new BehaviorModelExample.OnLand();
	BehaviorModelExample.Gravity gravity = new BehaviorModelExample.Gravity();
	AnimationModel jumpingAnimation;
	AnimationModel fallingAnimation;

	double health = 100.0;
}