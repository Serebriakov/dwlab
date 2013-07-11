package examples;

import dwlab.base.service.Service;
import dwlab.behavior_models.AnimationModel;
import dwlab.behavior_models.BehaviorModel;
import dwlab.behavior_models.FixedWaitingModel;
import dwlab.behavior_models.ModelStack;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteAndTileCollisionHandler;
import dwlab.shapes.sprites.VectorSprite;
import static examples.BehaviorModelExample.*;

public class GameObject extends VectorSprite {
	static VerticalCollisionHandler verticalCollisionHandler = new VerticalCollisionHandler();
	
	BehaviorModel onLand = new BehaviorModel();
	Gravity gravity = new Gravity();
	AnimationModel jumpingAnimation;
	AnimationModel fallingAnimation;

	double health = 100d;


	public static class Gravity extends BehaviorModel<VectorSprite> {
		double gravity = 8d;

		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.dY += perSecond( gravity );
		}
	}


	public static class PushFromWalls extends SpriteAndTileCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			sprite.pushFrom( tileMap, tileX, tileY );
		}
	}
	

	public static class VerticalCollisionHandler extends SpriteAndTileCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			GameObject gameObject = (GameObject) sprite;
			gameObject.pushFrom( tileMap, tileX, tileY );
			if( gameObject.dY > 0d ) {
				gameObject.onLand.activateModel( sprite );
				gameObject.fallingAnimation.deactivateModel( sprite );
				gameObject.jumpingAnimation.deactivateModel( sprite );
			}
			gameObject.dY = 0d;
		}
	}


	public static class Jump extends BehaviorModel<VectorSprite> {
		public double fromStrength, toStrength;

		public Jump( double fromStrength, double toStrength ) {
			this.fromStrength = fromStrength;
			this.toStrength = toStrength;
		}

		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.dY = -Service.random( fromStrength, toStrength );
			remove( sprite );
		}
	}


	public static class Death extends FixedWaitingModel<VectorSprite> {
		@Override
		public void init( VectorSprite shape ) {
			period = deathPeriod;
			if( shape.collisionModel() != null ) {
				shape.horizontalMovementModel().deactivateModel( shape );
				shape.verticalMovementModel().deactivateModel( shape );
				shape.deactivateModel( ModelStack.class );
			}
		}

		@Override
		public void applyTo( VectorSprite shape ) {
			super.applyTo( shape );
			double alpha = 1d - ( instance.time - startingTime ) / period;
			if( alpha >= 0d ) shape.visualizer.alpha = alpha;
		}

		@Override
		public void deactivate( VectorSprite shape ) {
			shape.removeFrom( layer );
		}
	}
}