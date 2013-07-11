package examples;

import dwlab.base.service.Service;
import dwlab.behavior_models.AnimationModel;
import dwlab.behavior_models.BehaviorModel;
import dwlab.behavior_models.FixedWaitingModel;
import dwlab.behavior_models.IsModelActive;
import dwlab.behavior_models.ModelActivator;
import dwlab.behavior_models.ModelDeactivator;
import dwlab.behavior_models.RandomWaitingModel;
import dwlab.behavior_models.VectorSpriteCollisionsModel.Group;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteAndTileCollisionHandler;
import dwlab.shapes.sprites.VectorSprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import static examples.BehaviorModelExample.*;
import static examples.GameObject.verticalCollisionHandler;

public class Jelly extends GameObject {
	static final double jumpingAnimationSpeed = 0.2;
	static final double firingAnimationSpeed = 0.1;
	static final double walkingAnimationSpeed = 0.2;
	static final double idleAnimationSpeed = 0.4;
	static final double minAttack = 10.0;
	static final double maxAttack = 20.0;
	static final double hurtingTime = 0.2;

	static final double jumpingPause = jumpingAnimationSpeed * 2.0;
	static final double bulletPause = firingAnimationSpeed * 5.0;

	int score = 100;

	static BumpingWalls bumpingWalls = new BumpingWalls();
	static DestroyBullet destroyBullet = new DestroyBullet();
	

	@Override
	public void init() {
		attachModelImmediately( gravity );
		
		attachModelImmediately( new ModelDeactivator( onLand, true ) );
		
		addTileMapCollisions( Group.HORIZONTAL, tileMap, bumpingWalls, null );
		addTileMapCollisions( Group.VERTICAL, tileMap, verticalCollisionHandler, null );

		jumpingAnimation = new AnimationModel( false, jumpingAnimationSpeed, 8, 8 );
		fallingAnimation = new AnimationModel( true, jumpingAnimationSpeed, 3, 13, true );
		AnimationModel firingAnimation = new AnimationModel( false, firingAnimationSpeed, 8, 16 );


		if( parameterExists( "jumping" ) ) {
			double[] parameters= getDoubleParameters( "jumping", "-" );
			RandomWaitingModel waitingForJump = new RandomWaitingModel( parameters[ 0 ], parameters[ 1 ] );
			attachModel( waitingForJump );

			IsModelActive onLandCondition = new IsModelActive( onLand );
			waitingForJump.nextModels.addLast( onLandCondition );

			IsModelActive animationActive = new IsModelActive( firingAnimation );
			onLandCondition.trueModels.addLast( animationActive );
			onLandCondition.falseModels.addLast( waitingForJump );

			animationActive.trueModels.addLast( waitingForJump );
			animationActive.falseModels.addLast( new ModelActivator( jumpingAnimation ) );
			animationActive.falseModels.addLast( new ModelDeactivator( horizontalMovementModel() ) );
			animationActive.falseModels.addLast( new ModelDeactivator( gravity ) );

			jumpingAnimation.nextModels.addLast( new ModelActivator( fallingAnimation ) );

			parameters= getDoubleParameters( "jumping_strength", "-" );
			FixedWaitingModel pauseBeforeJump = FixedWaitingModel.create( jumpingPause );
			pauseBeforeJump.nextModels.addLast( new Jump(  parameters[ 0 ], parameters[ 1 ] ) );
			pauseBeforeJump.nextModels.addLast( new ModelActivator( horizontalMovementModel() ) );
			pauseBeforeJump.nextModels.addLast( new ModelActivator( gravity ) );
			pauseBeforeJump.nextModels.addLast( waitingForJump );
			animationActive.falseModels.addLast( pauseBeforeJump );

			addToStack( jumpingAnimation );
			score += 200;
		}


		addToStack( fallingAnimation );


		if( parameterExists( "firing" ) ) {
			double[] parameters = getDoubleParameters( "firing", "-" );
			RandomWaitingModel waitingForFire = new RandomWaitingModel( parameters[ 0 ], parameters[ 1 ] );
			attachModel( waitingForFire );

			IsModelActive onLandCondition = new IsModelActive( onLand );
			waitingForFire.nextModels.addLast( onLandCondition );

			IsModelActive animationActive = new IsModelActive( jumpingAnimation );
			onLandCondition.trueModels.addLast( animationActive );
			onLandCondition.falseModels.addLast( waitingForFire );

			animationActive.trueModels.addLast( waitingForFire );
			animationActive.falseModels.addLast( new ModelActivator( firingAnimation ) );
			animationActive.falseModels.addLast( new ModelDeactivator( horizontalMovementModel() ) );

			firingAnimation.nextModels.addLast( new ModelActivator( horizontalMovementModel() ) );

			parameters = getDoubleParameters( "firing_speed", "-" );
			FixedWaitingModel pauseBeforeBullet = FixedWaitingModel.create( bulletPause );
			pauseBeforeBullet.nextModels.addLast( new CreateBullet( parameters[ 0 ], parameters[ 1 ] ) );
			pauseBeforeBullet.nextModels.addLast( waitingForFire );
			animationActive.falseModels.addLast( pauseBeforeBullet );

			addToStack( firingAnimation, false );
			score += 300;
		}

		AnimationModel movementAnimation = new AnimationModel( true, walkingAnimationSpeed, 3, 3, true );
		if( parameterExists( "moving" ) ) {
			double[] parameters = getDoubleParameters( "moving", "-" );
			dX = Math.signum( visualizer.xScale ) * Service.random( parameters[ 0 ], parameters[ 1 ] );
			addToStack( movementAnimation );
			score += 100;
		} else {
			dX = 0;
		}
		

		AnimationModel standingAnimation = new AnimationModel( true, idleAnimationSpeed, 3, 0, true );
		addToStack( standingAnimation );

		if( parameterExists( "score" ) ) score = getIntegerParameter( "score" );
		if( parameterExists( "health" ) ) health = getDoubleParameter( "health" );
	}
	

	static class BumpingWalls extends SpriteAndTileCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			sprite.pushFrom( tileMap, tileX, tileY );
			( (VectorSprite) sprite ).dX *= -1;
			sprite.visualizer.xScale *= -1;
		}
	}


	public static class CreateBullet extends BehaviorModel<VectorSprite> {
		public double fromSpeed, toSpeed;

		public CreateBullet( double fromSpeed, double toSpeed ) {
			this.fromSpeed = fromSpeed;
			this.toSpeed = toSpeed;
		}

		@Override
		public void applyTo( VectorSprite sprite ) {
			Bullet.create( sprite, Service.random( fromSpeed, toSpeed ) );
			remove( sprite );
		}
	}


	public static class Bullet extends VectorSprite {
		static double minAttack = 5d;
		static double maxAttack = 10d;

		public boolean collisions = true;

		public static void create( VectorSprite Jelly, double speed ) {
			Bullet bullet = new Bullet();
			bullet.setCoords( Jelly.getX() + Math.signum( Jelly.dX ) * Jelly.getWidth() * 2.2d, Jelly.getY() - 0.15d * Jelly.getHeight() );
			bullet.setSize( 0.45d * Jelly.getWidth(), 0.45d * Jelly.getWidth() );
			bullet.shapeType = ShapeType.oval;
			bullet.dX = Math.signum( Jelly.visualizer.xScale ) * speed;
			bullet.visualizer.setVisualizerScale( 12d, 4d );
			bullet.visualizer.image = Jelly.visualizer.image;
			bullet.frame = 6;
			bullet.insertTo( layer );
		}
			

		@Override
		public void act() {
			moveForward();
			if( collisions ) collisionsWith( tileMap, destroyBullet );
			super.act();
		}
		

		public static void disable( Sprite sprite ) {
			Bullet bullet = (Bullet) sprite;
			if( bullet.collisions ) {
				bullet.attachModel( new Death() );
				bullet.attachModel( new Gravity() );
				bullet.reverseDirection();
				bullet.collisions = false;
				bullet.dX *= 0.25;
			}
		}
	}


	public static class DestroyBullet extends SpriteAndTileCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( tileMap.getTile( tileX, tileY ) == bricks ) Bullet.disable( sprite );
		}
	}


	public static class JellyHurt extends FixedWaitingModel<VectorSprite> {
		@Override
		public void init( VectorSprite sprite ) {
			period = Jelly.hurtingTime;
			sprite.horizontalMovementModel().deactivate( sprite );
		}

		@Override
		public void applyTo( VectorSprite sprite ) {
			super.applyTo( sprite );
			double intensity = ( instance.time - startingTime ) / period;
			if( intensity <= 1.0 ) sprite.visualizer.set( 1.0, intensity, intensity, 1.0 );
		}

		@Override
		public void deactivate( VectorSprite sprite ) {
			sprite.horizontalMovementModel().activate( sprite );
			sprite.visualizer.set( "FFFFFF" );
		}
	}
}