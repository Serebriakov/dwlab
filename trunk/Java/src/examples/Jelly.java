package examples;

import dwlab.base.service.Service;
import dwlab.behavior_models.AnimationModel;
import dwlab.behavior_models.FixedWaitingModel;
import dwlab.behavior_models.IsModelActive;
import dwlab.behavior_models.ModelActivator;
import dwlab.behavior_models.ModelDeactivator;
import dwlab.behavior_models.RandomWaitingModel;

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


	@Override
	public void init() {
		attachModel( gravity );

		jumpingAnimation = new AnimationModel( false, jumpingAnimationSpeed, 8, 8 );
		fallingAnimation = new AnimationModel( true, jumpingAnimationSpeed, 3, 13, true );
		AnimationModel firingAnimation = new AnimationModel( false, firingAnimationSpeed, 8, 16 );


		if( parameterExists( "jumping" ) ) {
			String parameters[] = getParameter( "jumping" ).split( "-" );
			RandomWaitingModel waitingForJump = RandomWaitingModel.create( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) );
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

			parameters = getParameter( "jumping_strength" ).split( "-" );
			FixedWaitingModel pauseBeforeJump = FixedWaitingModel.create( jumpingPause );
			pauseBeforeJump.nextModels.addLast( BehaviorModelExample.Jump.create( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) ) );
			pauseBeforeJump.nextModels.addLast( new ModelActivator( horizontalMovementModel() ) );
			pauseBeforeJump.nextModels.addLast( new ModelActivator( gravity ) );
			pauseBeforeJump.nextModels.addLast( waitingForJump );
			animationActive.falseModels.addLast( pauseBeforeJump );

			addToStack( jumpingAnimation );
			score += 200;
		}


		addToStack( fallingAnimation );


		if( parameterExists( "firing" ) ) {
			String parameters[] = getParameter( "firing" ).split( "-" );
			RandomWaitingModel waitingForFire = RandomWaitingModel.create( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) );
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

			parameters = getParameter( "firing_speed" ).split( "-" );
			FixedWaitingModel pauseBeforeBullet = FixedWaitingModel.create( bulletPause );
			pauseBeforeBullet.nextModels.addLast( BehaviorModelExample.CreateBullet.create( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) ) );
			pauseBeforeBullet.nextModels.addLast( waitingForFire );
			animationActive.falseModels.addLast( pauseBeforeBullet );

			addToStack( firingAnimation );
			score += 300;
		}

		AnimationModel movementAnimation = new AnimationModel( true, walkingAnimationSpeed, 3, 3, true );
		if( parameterExists( "moving" ) ) {
			String parameters[] = getParameter( "moving" ).split( "-" );
			dX *= Service.random( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) );
			addToStack( movementAnimation );
			score += 100;
		}


		attachModel( new ModelDeactivator( onLand, true ) );


		AnimationModel standingAnimation = new AnimationModel( true, idleAnimationSpeed, 3, 0, true );
		addToStack( standingAnimation );

		if( parameterExists( "score" ) ) score = getIntegerParameter( "score" );
		if( parameterExists( "health" ) ) health = getDoubleParameter( "health" );
	}
}
