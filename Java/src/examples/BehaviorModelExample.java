package examples;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.behavior_models.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.*;
import dwlab.visualizers.MarchingAnts;

public class BehaviorModelExample extends Project {
	static {
		Graphics.init();
	}
	
	private static BehaviorModelExample instance = new BehaviorModelExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	static int bricks = 1;
	static double deathPeriod = 1.0;

	static World world;
	static Layer Layer;
	static TileMap tileMap;
	static Sprite selectedSprite;
	static MarchingAnts marchingAnts = new MarchingAnts();

	static BumpingWalls bumpingWalls = new BumpingWalls();
	static PushFromWalls pushFromWalls = new PushFromWalls();
	static DestroyBullet destroyBullet = new DestroyBullet();
	static AwPossumHurtingCollision awPossumHurtingCollision = new AwPossumHurtingCollision();
	static AwPossumHitCollision awPossumHitCollision = new AwPossumHitCollision();

	//Field LTSprite HitArea
	static int score;

	ButtonAction KeyExit = ButtonAction.create( KeyboardKey.create( Key.ESCAPE) );
	
	
	@Override
	public void init() {
	 	world = World.fromFile( "res/jellys.lw" );

		Graphics.init();

		Sprite sprite = new Sprite( Camera.current );
		sprite.visualizer.image = new Image( "res/scheme2.png" );
		while ( !KeyExit.wasPressed() ) {
			sprite.draw();
			Graphics.swapBuffers();
		}

		sprite.visualizer.image = new Image( "res/scheme1.png" );
		while ( !KeyExit.wasPressed() ) {
			sprite.draw();
			Graphics.swapBuffers();
		}

		initLevel();
	}

	
	public void initLevel() {
		Layer = world.findShape( "Level" ).load().toLayer();
		Layer.init();
		tileMap = Layer.findShape( "Field" ).toTileMap();
	}

	
	ButtonAction buttonSelect = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	@Override
	public void logic() {
		Camera.current.jumpTo( tileMap );
		if( buttonSelect.wasPressed() ) selectedSprite = cursor.lastCollidedSpriteOfLayer( Layer );
		Layer.act();
		if( KeyExit.wasPressed() ) exiting = true;
	}

	
	@Override
	public void render() {
		Layer.draw();
		if( selectedSprite != null ) {
			selectedSprite.showModels( 100, "" );
			selectedSprite.drawUsingVisualizer( marchingAnts );
		}
		//If HitArea Then HitArea.Draw()
		showDebugInfo();
		printText( "Guide AwesomePossum to exit from maze using arrow and space keys", Align.TO_RIGHT, Align.TO_TOP );
		printText( "you can view sprite behavior models by clicking left mouse button on it", Align.TO_RIGHT, Align.TO_TOP, 1 );
		printText( " Score" + Service.firstZeroes( score, 6 ), Align.TO_RIGHT, Align.TO_BOTTOM );
		printText( "LTBehaviorModel example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	
	
	
	public static class GameObject extends VectorSprite {
		OnLand onLand = new OnLand();
		gravity gravity = new gravity();
		AnimationModel jumpingAnimation;
		AnimationModel fallingAnimation;

		double health = 100.0;
	}



	public static class Jelly extends GameObject {
		double jumpingAnimationSpeed = 0.2;
		double firingAnimationSpeed = 0.1;
		double walkingAnimationSpeed = 0.2;
		double idleAnimationSpeed = 0.4;
		double minAttack = 10.0;
		double maxAttack = 20.0;
		double hurtingTime = 0.2;

		double jumpingPause = jumpingAnimationSpeed * 2.0;
		double bulletPause = firingAnimationSpeed * 5.0;

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
				pauseBeforeJump.nextModels.addLast( Jump.create( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) ) );
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
				pauseBeforeBullet.nextModels.addLast( CreateBullet.create( Double.parseDouble( parameters[ 0 ] ), Double.parseDouble( parameters[ 1 ] ) ) );
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



	public class AwPossum extends GameObject {
		double jumpingAnimationSpeed = 0.2;
		double walkingAnimationSpeed = 0.2;
		double idleAnimationSpeed = 0.4;

		double jumpingPause = jumpingAnimationSpeed;
		double jumpingStrength = 6.0;
		double walkingSpeed = 5.0;

		double minAttack = 20.0;
		double maxAttack = 35.0;
		double minHealthGain = 3.0;
		double maxHealthGain = 6.0;

		double knockOutPeriod = 0.3;
		double immortalPeriod = 1.5;
		double hitPeriod = 0.2;
		double knockOutStrength = 2.0;
		double hitPauseTime = 0.5;

		public AnimationModel movementAnimation = new AnimationModel( true, walkingAnimationSpeed, 4, 4 );
		public AnimationModel hurtingAnimation = new AnimationModel( false, knockOutPeriod, 1, 14 );
		public AnimationModel punchingAnimation = new AnimationModel( false, hitPeriod, 1, 15 );
		public AnimationModel kickingAnimation = new AnimationModel( false, hitPeriod, 1, 11 );

		public MovementControl movementControl = new MovementControl();
		public FixedWaitingModel hitPause = FixedWaitingModel.create( hitPauseTime );

		public ButtonAction moveLeftKey = ButtonAction.create( KeyboardKey.create( Key.LEFT ), "Move left" );
		public ButtonAction moveRightKey = ButtonAction.create( KeyboardKey.create( Key.RIGHT ), "Move right" );
		public ButtonAction jumpKey = ButtonAction.create( KeyboardKey.create( Key.UP ), "Jump" );
		public ButtonAction hitKey = ButtonAction.create( KeyboardKey.create( Key.SPACE ), "Hit" );

		
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


			attachModel( HorizontalMovement.create( pushFromWalls ) );


			attachModel( new ModelDeactivator( onLand, true ) );


			AnimationModel standingAnimation = new AnimationModel( true, idleAnimationSpeed, 4, 0, true );
			addToStack( standingAnimation );
		}


		@Override
		public void act() {
			super.act();
			this.
			collisionsWithLayer( Layer, awPossumHurtingCollision );
			if( x > tileMap.rightX() ) switchTo( new Restart() );
		}


		@Override
		public void draw() {
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

	public static class OnLand extends BehaviorModel {
	}


	public static class gravity extends BehaviorModel<VectorSprite> {
		double gravity = 8.0d;

		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.alterCoords( 0d, perSecond( gravity ) );
		}
	}


	public static class HorizontalMovement extends BehaviorModel {
		public SpriteAndTileCollisionHandler collisionHandler;

		public static HorizontalMovement create( SpriteAndTileCollisionHandler collisionHandler ) {
			HorizontalMovement horizontalMovement = new HorizontalMovement();
			horizontalMovement.collisionHandler = collisionHandler;
			return horizontalMovement;
		}

		public void applyTo( Shape shape ) {
			VectorSprite sprite = VectorSprite( shape );
			sprite.move( sprite.dX, 0 );
			sprite.collisionsWithTileMap( example.tileMap, collisionHandler );
		}

		public String info( Shape shape ) {
			return Service.trim( VectorSprite( shape ).dX );
		}
	}


	public static class BumpingWalls extends SpriteAndTileCollisionHandler {
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			sprite.pushFromTile( tileMap, tileX, tileY );
			VectorSprite( sprite ).dX *= -1;
			sprite.visualizer.getX()Scale *= -1;
		}
	}


	public static class PushFromWalls extends SpriteAndTileCollisionHandler {
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( tileMap.getTile( tileX, tileY ) == example.bricks ) sprite.pushFromTile( tileMap, tileX, tileY );
		}
	}


	public static class VerticalMovement extends BehaviorModel {
		public VerticalCollisionHandler handler = new VerticalCollisionHandler();

		public static VerticalMovement create( int forPLayer ) {
			VerticalMovement verticalMovement = new VerticalMovement();
			verticalMovement.handler.forPLayer = forPLayer;
			return verticalMovement;
		}

		public void applyTo( Shape shape ) {
			VectorSprite sprite = VectorSprite( shape );
			sprite.move( 0, sprite..y );
			sprite.collisionsWithTileMap( example.tileMap, handler );
		}

		public String info( Shape shape ) {
			return Service.trim( VectorSprite( shape )..y );
		}
	}


	public static class VerticalCollisionHandler extends SpriteAndTileCollisionHandler {
		public int forPLayer;

		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( forPLayer ) if tileMap.getTile( tileX, tileY ) != example.bricks then return;
			GameObject gameObject = GameObject( sprite );
			gameObject.pushFromTile( tileMap, tileX, tileY );
			if( gameObject..y > 0 ) {
				gameObject.onLand.activateModel( sprite );
				gameObject.fallingAnimation.deactivateModel( sprite );
				gameObject.jumpingAnimation.deactivateModel( sprite );
			}
			gameObject..y = 0;
		}
	}


	public static class Jump extends BehaviorModel {
		public double fromStrength, double toStrength;

		public static Jump create( double fromStrength, double toStrength ) {
			Jump jump = new Jump();
			jump.fromStrength = fromStrength;
			jump.toStrength = toStrength;
			return jump;
		}

		public void applyTo( Shape shape ) {
			VectorSprite( shape )..y = -Service.random( fromStrength, toStrength );
			remove( shape );
		}
	}


	public static class CreateBullet extends BehaviorModel {
		public double fromSpeed, double toSpeed;

		public static CreateBullet create( double fromSpeed, double toSpeed ) {
			CreateBullet createBullet = new CreateBullet();
			createBullet.fromSpeed = fromSpeed;
			createBullet.toSpeed = toSpeed;
			return createBullet;
		}

		public void applyTo( Shape shape ) {
			Bullet.create( VectorSprite( shape ), Service.random( fromSpeed, toSpeed ) );
			remove( shape );
		}
	}


	public static class Bullet extends VectorSprite {
		double minAttack = 5.0;
		double maxAttack = 10.0;

		public int collisions = true;

		public static void create( VectorSprite Jelly, double speed ) {
			Bullet bullet = new Bullet();
			bullet.setCoords( Jelly.getX() + sgn( Jelly.dX ) * Jelly.width * 2.2, Jelly.y - 0.15 * Jelly.height );
			bullet.setSize( 0.45 * Jelly.width, 0.45 * Jelly.width );
			bullet.shapeType = Sprite.oval;
			bullet.dX = sgn( Jelly.dX ) * speed;
			bullet.visualizer.setVisualizerScale( 12, 4 );
			bullet.visualizer.image = Jelly.visualizer.image;
			bullet.frame = 6;
			example.Layer.addLast( bullet );
		}

		public void act() {
			moveForward();
			if( collisions ) collisionsWithTileMap( example.tileMap, example.destRAYBullet );
			super.act();
		}

		public static void disable( Sprite sprite ) {
			Bullet bullet = Bullet( sprite );
			if( bullet.collisions ) {
				bullet.attachModel( new Death() );
				bullet.attachModel( new gravity() );
				bullet.reverseDirection();
				bullet.collisions = false;
				bullet.dX *= 0.25;
			}
		}
	}


	public static class DestroyBullet extends SpriteAndTileCollisionHandler {
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( tileMap.getTile( tileX, tileY ) == example.bricks ) Bullet.disable( sprite );
		}
	}


	public static class MovementControl extends BehaviorModel {
		public void applyTo( Shape shape ) {
			AwPossum awPossum = AwPossum( shape );
			if( awPossum.gravity.active ) {
				if( awPossum.moveLeftKey.isDown() ) {
					awPossum.setFacing( Sprite.leftFacing );
					awPossum.dX = -awPossum.walkingSpeed;
				} else if( awPossum.moveRightKey.isDown() ) {
					awPossum.setFacing( Sprite.rightFacing );
					awPossum.dX = awPossum.walkingSpeed;
				} else {
					awPossum.dX = 0;
				}
			} else {
				awPossum.dX = 0;
			}

			if( awPossum.dX && awPossum.onLand.active ) {
				awPossum.movementAnimation.activateModel( shape );
			} else {
				awPossum.movementAnimation.deactivateModel( shape );
			}
		}
	}


	public static class AwPossumHurtingCollision extends SpriteCollisionHandler {
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			if( sprite1.findModel( "TImmortality" ) ) return;
			if( sprite2.findModel( "TDeath" ) ) return;

			double damage = 0;
			if( Jelly( sprite2 ) ) damage = Service.random( Jelly.minAttack, Jelly.maxAttack );
			Bullet bullet = Bullet( sprite2 );
			if( bullet ) {
				if( bullet.collisions ) {
					damage = Service.random( Bullet.minAttack, Bullet.maxAttack ) * sprite2.getDiameter() / 0.45;
					example.Layer.remove( sprite2 );
				}
			}
			if( damage ) {
				AwPossum awPossum = AwPossum( sprite1 );
				awPossum.health -= damage;
				if( awPossum.health > 0.0 ) {
					sprite1.attachModel( new Immortality() );
					sprite1.attachModel( new KnockOut() );
				} else if( ! sprite1.findModel( "TDeath" ) ) {
					sprite1.behaviorModels.clear();
					sprite1.attachModel( new Death() );
				}
			}
		}
	}


	public static class Immortality extends FixedWaitingModel {
		double blinkingSpeed = 0.05;

		public void init( Shape shape ) {
			period = AwPossum.immortalPeriod;
		}

		public void applyTo( Shape shape ) {
			shape.visible = Math.floor( currentProject.time / blinkingSpeed ) % 2;
			super.applyTo( shape );
		}

		public void deactivate( Shape shape ) {
			shape.visible = true;
		}
	}


	public static class KnockOut extends FixedWaitingModel {
		public void init( Shape shape ) {
			AwPossum awPossum = AwPossum( shape );
			period = awPossum.knockOutPeriod;
			awPossum.dX = -awPossum.getFacing() * awPossum.knockOutStrength;
			awPossum.movementControl.deactivateModel( shape );
			awPossum.hurtingAnimation.activateModel( shape );
		}

		public void applyTo( Shape shape ) {
			VectorSprite( shape ).dX *= 0.9;
			super.applyTo( shape );
		}

		public void deactivate( Shape shape ) {
			AwPossum awPossum = AwPossum( shape );
			awPossum.hurtingAnimation.deactivateModel( shape );
			awPossum.movementControl.activateModel( shape );
		}
	}


	public static class HittingArea extends FixedWaitingModel {
		public Sprite Area;
		public int punch;

		public static HittingArea create2( int punch ) {
			HittingArea Area = new HittingArea();
			Area.punch = punch;
			return Area;
		}

		public void init( Shape shape ) {
			Area = new Sprite();
			Area.shapeType = Sprite.oval;
			Area.setDiameter( 0.3 );
			period = AwPossum.hitPeriod;
			example.awPossumHitCollision.collided = false;
		}

		public void applyTo( Shape shape ) {
			if( punch ) {
				Area.setCoords( shape.getX() + shape.getFacing() * 0.95, shape.y + 0.15 );
			} else {
				Area.setCoords( shape.getX() + shape.getFacing() * 0.95, shape.y - 0.1 );
			}
			//Example.HitArea = Area
			Area.collisionsWithLayer( example.Layer, example.awPossumHitCollision );
			if( example.awPossumHitCollision.collided ) remove( shape );
			super.applyTo( shape );
		}

		//Method Deactivate( LTShape Shape )
		//	Example.HitArea = Null
		//End Method
	}


	public static class AwPossumHitCollision extends SpriteCollisionHandler {
		public int collided ;

		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			Jelly Jelly = Jelly( sprite2 );
			if( Jelly ) {
				Jelly.health -= Service.random( AwPossum.minAttack, AwPossum.maxAttack );
				if( Jelly.health > 0 ) {
					Jelly.attachModel( new JellyHurt() );
				} else if( ! Jelly.findModel( "TDeath" ) ) {
					Score.create( Jelly, Jelly.score );

					AwPossum awPossum = AwPossum( example.Layer.findShapeWithType( "TAwPossum" ) );
					awPossum.health = Math.min( awPossum.health + Service.random( AwPossum.minHealthGain, AwPossum.maxHealthGain ), 100.0 );

					Jelly.behaviorModels.clear();
					Jelly.attachModel( new Death() );
				}
				collided = true;
			} else if( Bullet( sprite2 ) ) {
				if( ! sprite2.findModel( "TDeath" ) ) {
					Bullet.disable( sprite2 );
					Score.create( sprite2, 50 );
				}
			}
		}
	}


	public static class JellyHurt extends FixedWaitingModel {
		public void init( Shape shape ) {
			period = Jelly.hurtingTime;
			shape.deactivateModel( "THorizontalMovement" );
		}

		public void applyTo( Shape shape ) {
			super.applyTo( shape );
			double intensi.y = ( currentProject.time - startingTime ) / period;
			if( intensi.y <= 1.0 ) shape.visualizer.Graphics.setColorFromRGB( 1.0, intensi.y, intensi.y );
		}

		public void deactivate( Shape shape ) {
			shape.activateModel( "THorizontalMovement" );
			shape.visualizer.set( "FFFFFF" );
		}
	}


	public static class Death extends FixedWaitingModel {
		public void init( Shape shape ) {
			period = example.deathPeriod;
		}

		public void applyTo( Shape shape ) {
			super.applyTo( shape );
			double alpha = 1.0 - ( currentProject.time - startingTime ) / period;
			if( alpha >= 0.0 ) shape.visualizer.alpha = alpha;
		}

		public void deactivate( Shape shape ) {
			example.Layer.remove( shape );
		}
	}


	public static class Score extends Sprite {
		double speed = 2.0;
		double period = 3.0;

		public int amount;
		public double startingTime;

		public static void create( Sprite sprite, int amount ) {
			Score score = new Score();
			score.setCoords( sprite.getX(), sprite.topY() );
			score.amount = amount;
			score.setDiameter( 0 );
			score.startingTime = currentProject.time;
			example.score += amount;
			example.Layer.addLast( score );
		}

		public void act() {
			move( 0, -speed );
			if( currentProject.time > startingTime + period ) example.Layer.remove( this );
		}

		public void draw() {
			printText( "+" + amount, , Align.TO_BOTTOM, , , true );
		}
	}


	public static class Restart extends Project {
		public int startingTime = System.currentTimeMillis();
		public int initialized;

		public void render() {
			if( System.currentTimeMillis() < startingTime + 2000 ) {
				example.render();
				Camera.current.darken( 0.0005 * ( System.currentTimeMillis() - startingTime ) );
			} else if( System.currentTimeMillis() < startingTime + 4000 ) {
				if( ! initialized ) {
					example.initLevel();
					initialized = true;
				}
				example.render();
				Camera.current.darken( 0.0005 * ( 4000 - System.currentTimeMillis() + startingTime ) );
			} else {
				exiting = true;
			}
		}
	}
}