package examples;

import dwlab.base.Graphics;
import dwlab.behavior_models.AnimationModel;
import dwlab.behavior_models.FixedWaitingModel;
import dwlab.behavior_models.IsButtonActionDown;
import dwlab.behavior_models.IsModelActive;
import dwlab.behavior_models.ModelActivator;
import dwlab.behavior_models.ModelDeactivator;
import dwlab.behavior_models.ModelStack;
import dwlab.behavior_models.VectorSpriteCollisionsModel;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.visualizers.Color;

public class AwPossum extends GameObject {
	static final double jumpingAnimationSpeed = 0.2;
	static final double walkingAnimationSpeed = 0.2;
	static final double idleAnimationSpeed = 0.4;

	static final double jumpingPause = jumpingAnimationSpeed;
	static final double jumpingStrength = 6.0;
	static final double walkingSpeed = 5.0;

	static final double minAttack = 20.0;
	static final double maxAttack = 35.0;
	static final double minHealthGain = 3.0;
	static final double maxHealthGain = 6.0;

	static final double knockOutPeriod = 0.3;
	static final double immortalPeriod = 1.5;
	static final double hitPeriod = 0.2;
	static final double knockOutStrength = 2.0;
	static final double hitPauseTime = 0.5;

	AnimationModel movementAnimation = new AnimationModel( true, walkingAnimationSpeed, 4, 4 );
	AnimationModel hurtingAnimation = new AnimationModel( false, knockOutPeriod, 1, 14 );
	AnimationModel punchingAnimation = new AnimationModel( false, hitPeriod, 1, 15 );
	AnimationModel kickingAnimation = new AnimationModel( false, hitPeriod, 1, 11 );

	BehaviorModelExample.MovementControl movementControl = new BehaviorModelExample.MovementControl();
	FixedWaitingModel hitPause = FixedWaitingModel.create( hitPauseTime );

	ButtonAction moveLeftKey = ButtonAction.create( KeyboardKey.create( Key.LEFT ), "Move left" );
	ButtonAction moveRightKey = ButtonAction.create( KeyboardKey.create( Key.RIGHT ), "Move right" );
	ButtonAction jumpKey = ButtonAction.create( KeyboardKey.create( Key.UP ), "Jump" );
	ButtonAction hitKey = ButtonAction.create( KeyboardKey.create( Key.SPACE ), "Hit" );


	@Override
	public void init() {
		attachModel( gravity );


		ModelStack animationStack = new ModelStack();
		attachModel( animationStack );

		addToStack( hurtingAnimation );
		addToStack( punchingAnimation, false );
		addToStack( kickingAnimation, false );

		jumpingAnimation = new AnimationModel( false, jumpingAnimationSpeed, 3, 8 );
		addToStack( jumpingAnimation );

		fallingAnimation = new AnimationModel( true, jumpingAnimationSpeed, 1, 10 );
		jumpingAnimation.nextModels.addLast( new ModelActivator( fallingAnimation ) );
		addToStack( fallingAnimation );

		addToStack( movementAnimation );


		attachModel( movementControl );


		IsButtonActionDown jumpKeyDown = IsButtonActionDown.create( jumpKey );
		attachModel( jumpKeyDown );
		jumpKeyDown.falseModels.addLast( jumpKeyDown );

		IsModelActive onLandCondition = new IsModelActive( onLand );
		jumpKeyDown.trueModels.addLast( onLandCondition );

		onLandCondition.trueModels.addLast( new ModelActivator( jumpingAnimation ) );
		onLandCondition.trueModels.addLast( new ModelDeactivator( gravity ) );
		onLandCondition.falseModels.addLast( jumpKeyDown );

		FixedWaitingModel pauseBeforeJump = FixedWaitingModel.create( jumpingPause );
		pauseBeforeJump.nextModels.addLast( BehaviorModelExample.Jump.create( jumpingStrength, jumpingStrength ) );
		pauseBeforeJump.nextModels.addLast( new ModelActivator( gravity ) );
		pauseBeforeJump.nextModels.addLast( jumpKeyDown );
		onLandCondition.trueModels.addLast( pauseBeforeJump );

		addToStack( jumpingAnimation, false );


		IsButtonActionDown hitKeyDown = IsButtonActionDown.create( hitKey );
		attachModel( hitKeyDown );
		hitKeyDown.falseModels.addLast( hitKeyDown );

		IsModelActive hitPauseCondition = new IsModelActive( hitPause );
		hitPauseCondition.falseModels.addLast( hitPause );
		hitPauseCondition.trueModels.addLast( hitKeyDown );
		hitKeyDown.trueModels.addLast( hitPauseCondition );

		IsModelActive onLandCondition2 = new IsModelActive( onLand );
		onLandCondition2.trueModels.addLast( new ModelActivator( punchingAnimation ) );
		onLandCondition2.trueModels.addLast( BehaviorModelExample.HittingArea.create2( true ) );
		onLandCondition2.trueModels.addLast( hitKeyDown );
		onLandCondition2.falseModels.addLast( new ModelActivator( kickingAnimation ) );
		onLandCondition2.falseModels.addLast( BehaviorModelExample.HittingArea.create2( false ) );
		onLandCondition2.falseModels.addLast( hitKeyDown );
		hitPauseCondition.falseModels.addLast( onLandCondition2 );


		addTileMapCollisions( VectorSpriteCollisionsModel.Group.ALL, BehaviorModelExample.tileMap, BehaviorModelExample.pushFromWalls, null );


		attachModel( new ModelDeactivator( onLand, true ) );


		AnimationModel standingAnimation = new AnimationModel( true, idleAnimationSpeed, 4, 0, true );
		addToStack( standingAnimation );
	}


	@Override
	public void act() {
		super.act();
		collisionsWithLayer( BehaviorModelExample.layer, BehaviorModelExample.awPossumHurtingCollision );
		if( x > BehaviorModelExample.tileMap.rightX() ) BehaviorModelExample.instance.switchTo( new BehaviorModelExample.Restart() );
	}


	@Override
	public void draw( Color drawingColor ) {
		super.draw();
		Graphics.drawEmptyRectangle( 5, 580, 104, 15 );
		if( health >= 50.0 ) {
			Graphics.setCurrentColor( ( 100.0 - health ) * 255.0 / 50.0 , 255, 0 );
		} else {
			Graphics.setCurrentColor( 255, health * 255.0 / 50.0, 0 );
		}
		Graphics.drawRectangle( 7, 582, health, 11 );
		Graphics.resetCurrentColor();
	}
}