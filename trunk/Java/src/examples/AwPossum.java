package examples;

import dwlab.base.Graphics;
import dwlab.base.service.Service;
import dwlab.behavior_models.AnimationModel;
import dwlab.behavior_models.BehaviorModel;
import dwlab.behavior_models.FixedWaitingModel;
import dwlab.behavior_models.IsButtonActionDown;
import dwlab.behavior_models.IsModelActive;
import dwlab.behavior_models.ModelActivator;
import dwlab.behavior_models.ModelDeactivator;
import dwlab.behavior_models.ModelStack;
import dwlab.behavior_models.VectorSpriteCollisionsModel.Group;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Color;
import static examples.BehaviorModelExample.*;

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

	MovementControl movementControl = new MovementControl();
	FixedWaitingModel hitPause = FixedWaitingModel.create( hitPauseTime );
	static PushFromWalls pushFromWalls = new PushFromWalls();
	static AwPossumHurtingCollision awPossumHurtingCollision = new AwPossumHurtingCollision();
	static AwPossumHitCollision awPossumHitCollision = new AwPossumHitCollision();

	ButtonAction moveLeftKey = ButtonAction.create( KeyboardKey.create( Key.LEFT ), "Move left" );
	ButtonAction moveRightKey = ButtonAction.create( KeyboardKey.create( Key.RIGHT ), "Move right" );
	ButtonAction jumpKey = ButtonAction.create( KeyboardKey.create( Key.UP ), "Jump" );
	ButtonAction hitKey = ButtonAction.create( KeyboardKey.create( Key.SPACE ), "Hit" );

	final int[] bricks = { BehaviorModelExample.bricks };

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
		pauseBeforeJump.nextModels.addLast( Jump.create( jumpingStrength, jumpingStrength ) );
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
		onLandCondition2.trueModels.addLast( HittingArea.create2( true ) );
		onLandCondition2.trueModels.addLast( hitKeyDown );
		onLandCondition2.falseModels.addLast( new ModelActivator( kickingAnimation ) );
		onLandCondition2.falseModels.addLast( HittingArea.create2( false ) );
		onLandCondition2.falseModels.addLast( hitKeyDown );
		hitPauseCondition.falseModels.addLast( onLandCondition2 );


		addTileMapCollisions( Group.HORIZONTAL, BehaviorModelExample.tileMap, pushFromWalls, bricks );
		addTileMapCollisions( Group.VERTICAL, BehaviorModelExample.tileMap, verticalCollisionHandler, bricks );
		addLayerCollisions( Group.ALL, BehaviorModelExample.layer, awPossumHurtingCollision );


		attachModel( new ModelDeactivator( onLand, true ) );


		AnimationModel standingAnimation = new AnimationModel( true, idleAnimationSpeed, 4, 0, true );
		addToStack( standingAnimation );
	}


	@Override
	public void act() {
		super.act();
		collisionsWith( BehaviorModelExample.layer, awPossumHurtingCollision );
		if( x > BehaviorModelExample.tileMap.rightX() ) BehaviorModelExample.instance.switchTo( new BehaviorModelExample.Restart() );
	}


	@Override
	public void draw( Color drawingColor ) {
		super.draw( drawingColor );
		if( health >= 50.0 ) {
			Graphics.setCurrentColor( ( 100d - health ) / 50d , 1d, 0d );
		} else {
			Graphics.setCurrentColor( 1d, health / 50d, 0d );
		}
		Graphics.drawRectangle( 5, 580, health, 15 );
		Graphics.resetCurrentColor();
		Graphics.drawEmptyRectangle( 5, 580, 100, 15 );
	}


	public static class MovementControl extends BehaviorModel<AwPossum> {
		@Override
		public void applyTo( AwPossum awPossum ) {
			if( awPossum.gravity.active ) {
				if( awPossum.moveLeftKey.isDown() ) {
					awPossum.setFacing( Facing.LEFT );
					awPossum.dX = -walkingSpeed;
				} else if( awPossum.moveRightKey.isDown() ) {
					awPossum.setFacing( Facing.RIGHT );
					awPossum.dX = walkingSpeed;
				} else {
					awPossum.dX = 0;
				}
			} else {
				awPossum.dX = 0;
			}

			if( awPossum.dX != 0 && awPossum.onLand.active ) {
				awPossum.movementAnimation.activateModel( awPossum );
			} else {
				awPossum.movementAnimation.deactivateModel( awPossum );
			}
		}
	}


	public static class AwPossumHurtingCollision extends SpriteCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			if( sprite1.findModel( Immortality.class ) != null ) return;
			if( sprite2.findModel( Death.class ) != null  ) return;

			double damage = 0;
			if( sprite2 instanceof Jelly ) damage = Service.random( Jelly.minAttack, Jelly.maxAttack );
			Jelly.Bullet bullet = (Jelly.Bullet) sprite2;
			if( bullet != null ) {
				if( bullet.collisions ) {
					damage = Service.random( Jelly.Bullet.minAttack, Jelly.Bullet.maxAttack ) * sprite2.getDiameter() / 0.45;
					sprite2.removeFrom( layer );
				}
			}
			if( damage != 0 ) {
				AwPossum awPossum = (AwPossum) sprite1;
				awPossum.health -= damage;
				if( awPossum.health > 0.0 ) {
					sprite1.attachModel( new Immortality() );
					sprite1.attachModel( new KnockOut() );
				} else if( sprite1.findModel( Death.class ) == null ) {
					sprite1.behaviorModels.clear();
					sprite1.attachModel( new Death() );
				}
			}
		}
	}


	public static class Immortality extends FixedWaitingModel {
		static double blinkingSpeed = 0.05;

		@Override
		public void init( Shape shape ) {
			period = AwPossum.immortalPeriod;
		}

		@Override
		public void applyTo( Shape shape ) {
			shape.visible = ( Math.floor( instance.time / blinkingSpeed ) % 2 ) != 0;
			super.applyTo( shape );
		}

		@Override
		public void deactivate( Shape shape ) {
			shape.visible = true;
		}
	}


	public static class KnockOut extends FixedWaitingModel<AwPossum> {
		@Override
		public void init( AwPossum awPossum ) {
			period = AwPossum.knockOutPeriod;
			awPossum.dX = ( awPossum.getFacing() == Facing.LEFT ? 1 : -1 )* AwPossum.knockOutStrength;
			awPossum.movementControl.deactivateModel( awPossum );
			awPossum.hurtingAnimation.activateModel( awPossum );
		}

		@Override
		public void applyTo( AwPossum awPossum ) {
			awPossum.dX *= 0.9;
			super.applyTo( awPossum );
		}

		@Override
		public void deactivate( AwPossum awPossum ) {
			awPossum.hurtingAnimation.deactivateModel( awPossum );
			awPossum.movementControl.activateModel( awPossum );
		}
	}


	public static class HittingArea extends FixedWaitingModel {
		public Sprite area;
		public boolean punch;

		
		public static HittingArea create2( boolean punch ) {
			HittingArea area = new HittingArea();
			area.punch = punch;
			return area;
		}
		

		@Override
		public void init( Shape shape ) {
			area = new Sprite();
			area.shapeType = ShapeType.oval;
			area.setDiameter( 0.3 );
			period = AwPossum.hitPeriod;
			awPossumHitCollision.collided = false;
		}
		

		@Override
		public void applyTo( Shape shape ) {
			if( punch ) {
				area.setCoords( shape.getX() + Math.signum( shape.visualizer.xScale ) * 0.95d, shape.getY() + 0.15d );
			} else {
				area.setCoords( shape.getX() + Math.signum( shape.visualizer.xScale ) * 0.95d, shape.getY() - 0.1d );
			}
			area.collisionsWith( layer, awPossumHitCollision );
			if( awPossumHitCollision.collided ) remove( shape );
			super.applyTo( shape );
		}
	}


	public static class AwPossumHitCollision extends SpriteCollisionHandler {
		public boolean collided;

		@Override
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			Jelly jelly = (Jelly) sprite2;
			if( jelly != null ) {
				jelly.health -= Service.random( AwPossum.minAttack, AwPossum.maxAttack );
				if( jelly.health > 0 ) {
					jelly.attachModel( new Jelly.JellyHurt() );
				} else if( jelly.findModel( Death.class ) == null ) {
					Score.create( jelly, jelly.score );

					AwPossum awPossum = (AwPossum) layer.findShape( AwPossum.class );
					awPossum.health = Math.min( awPossum.health + Service.random( AwPossum.minHealthGain, AwPossum.maxHealthGain ), 100.0 );

					jelly.behaviorModels.clear();
					jelly.attachModel( new Death() );
				}
				collided = true;
			} else if( sprite2 instanceof Jelly.Bullet ) {
				if( sprite2.findModel( Death.class ) == null ) {
					Jelly.Bullet.disable( sprite2 );
					Score.create( sprite2, 50 );
				}
			}
		}
	}
}