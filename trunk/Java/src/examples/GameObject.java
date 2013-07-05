package examples;

import static dwlab.base.Obj.perSecond;
import dwlab.base.service.Service;
import dwlab.behavior_models.AnimationModel;
import dwlab.behavior_models.BehaviorModel;
import dwlab.behavior_models.FixedWaitingModel;
import dwlab.shapes.Shape;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteAndTileCollisionHandler;
import dwlab.shapes.sprites.VectorSprite;
import static examples.BehaviorModelExample.deathPeriod;
import static examples.BehaviorModelExample.instance;

public class GameObject extends VectorSprite {
	static VerticalCollisionHandler verticalCollisionHandler = new VerticalCollisionHandler();
	
	OnLand onLand = new OnLand();
	Gravity gravity = new Gravity();
	AnimationModel jumpingAnimation;
	AnimationModel fallingAnimation;

	double health = 100.0;


	public static class OnLand extends BehaviorModel {
	}


	public static class Gravity extends BehaviorModel<VectorSprite> {
		double gravity = 8.0d;

		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.alterCoords( 0d, perSecond( gravity ) );
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
			if( gameObject.dY > 0 ) {
				gameObject.onLand.activateModel( sprite );
				gameObject.fallingAnimation.deactivateModel( sprite );
				gameObject.jumpingAnimation.deactivateModel( sprite );
			}
			gameObject.dY = 0;
		}
	}


	public static class Jump extends BehaviorModel<VectorSprite> {
		public double fromStrength, toStrength;

		public static Jump create( double fromStrength, double toStrength ) {
			Jump jump = new Jump();
			jump.fromStrength = fromStrength;
			jump.toStrength = toStrength;
			return jump;
		}

		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.dY = -Service.random( fromStrength, toStrength );
			remove( sprite );
		}
	}


	public static class Death extends FixedWaitingModel {
		@Override
		public void init( Shape shape ) {
			period = deathPeriod;
		}

		@Override
		public void applyTo( Shape shape ) {
			super.applyTo( shape );
			double alpha = 1.0 - ( instance.time - startingTime ) / period;
			if( alpha >= 0.0 ) shape.visualizer.alpha = alpha;
		}

		@Override
		public void deactivate( Shape shape ) {
			remove( shape );
		}
	}
}