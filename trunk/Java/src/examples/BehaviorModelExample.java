package examples;
import dwlab.shapes.sprites.Sprite;
import dwlab.base.*;
import dwlab.behavior_models.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.Layers.World;
import dwlab.shapes.maps.TileMap;
import dwlab.shapes.sprites.*;
import dwlab.visualizers.MarchingAnts;
import dwlab.visualizers.Visualizer;
import org.lwjgl.input.Keyboard;

public class BehaviorModelExample extends Project {
	static {
		Graphics.init();
	}
	
	private static BehaviorModelExample instance = new BehaviorModelExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	int bricks = 1;
	double deathPeriod = 1.0;

	public World world;
	public Layer Layer;
	public TileMap tileMap;
	public Sprite selectedSprite;
	public MarchingAnts marchingAnts = new MarchingAnts();

	public BumpingWalls bumpingWalls = new BumpingWalls();
	public PushFromWalls pushFromWalls = new PushFromWalls();
	public DestRAYBullet destroyBullet = new DestroyBullet();
	public AwPossumHurtingCollision awPossumHurtingCollision = new AwPossumHurtingCollision();
	public AwPossumHitCollision awPossumHitCollision = new AwPossumHitCollision();

	//Field LTSprite HitArea
	public int score;

	ButtonAction KeyExit = ButtonAction.create( KeyboardKey.create( Key.ESCAPE) );
	
	@Override
	public void init() {
	 	world = World.fromFile( "res/jel.ys.lw" );

		initGraphics();

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
		Layer = loadLayer( world.findShape( "Level" ).toLayer() );
		Layer.init();
		tileMap = Layer.findShape( "Field" ).toTileMap();
	}

	ButtonAction buttonSelect = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	@Override
	public void logic() {
		Camera.current.jumpTo( tileMap );
		if( buttonSelect.wasPressed() ) selectedSprite = cursor.firstCollidedSpriteOfLayer( Layer );
		Layer.act();
		if( KeyExit.wasPressed() ) exiting = true;
	}

	@Override
	public void render() {
		Layer.draw();
		if( selectedSprite != null ) {
			selectedSprite.showModels( 100 );
			selectedSprite.drawUsingVisualizer( marchingAnts );
		}
		//If HitArea Then HitArea.Draw()
		showDebugInfo();
		Graphics.drawText( "Guide AwesomePossum to exit from maze using arrow and space Keys", tileMap.rightX(), tileMap.topY() - 12, Align.TO_RIGHT, Align.TO_TOP );
		Graphics.drawText( .you can view sprite behavior models .y clicking left mouse button on it", tileMap.rightX(), tileMap.topY() - 0.5, Align.TO_RIGHT, Align.TO_TOP );
		Graphics.drawText( " Score" + Service.firstZeroes( score, 6 ), tileMap.rightX() - 0.1, tileMap.bottomY() - 0.1, Align.TO_RIGHT, Align.TO_BOTTOM, true );
		Graphics.drawText( "LTBehaviorModel example", tileMap.getX(), tileMap.bottomY(), Align.TO_CENTER, Align.TO_BOTTOM );
	}
	public class GameObject extends VectorSprite {
		public OnLand onLand = new OnLand();
		public gravity gravity = new gravity();
		public AnimationModel jumpingAnimation;
		public AnimationModel fallingAnimation;

		public double health = 100.0;
	}



	public class Jel.y extends GameObject {
		double jumpingAnimationSpeed = 0.2;
		double firingAnimationSpeed = 0.1;
		double walkingAnimationSpeed = 0.2;
		double idleAnimationSpeed = 0.4;
		double minAttack = 10.0;
		double maxAttack = 20.0;
		double hurtingTime = 0.2;

		double jumpingPause = jumpingAnimationSpeed * 2.0;
		double bulletPause = firingAnimationSpeed * 5.0;

		public int score = 100;


		@Override
		public void init() {
			attachModel( gravity );

			ModelStack animationStack = new ModelStack();
			attachModel( animationStack );

			jumpingAnimation = new AnimationModel( false, jumpingAnimationSpeed, 8, 8 );
			fallingAnimation = new AnimationModel( true, jumpingAnimationSpeed, 3, 13, true );
			AnimationModel firingAnimation = new AnimationModel( false, firingAnimationSpeed, 8, 16 );


			HorizontalMovement horizontalMovement = new HorizontalMovement( example.bumpingWalls );


			String jumping = getParameter( "jumping" );
			if( jumping ) {
				String parameters[] = jumping.split( "-" );
				RandomWaitingModel waitingForJump = RandomWaitingModel.create( parameters[ 0 ].toDouble(), parameters[ 1 ].toDouble() );
				attachModel( waitingForJump );

				IsModelActive onLandCondition = new IsModelActive( onLand );
				waitingForJump.nextModels.addLast( onLandCondition );

				IsModelActive animationActive = new IsModelActive( firingAnimation );
				onLandCondition.trueModels.addLast( animationActive );
				onLandCondition.falseModels.addLast( waitingForJump );

				animationActive.trueModels.addLast( waitingForJump );
				animationActive.falseModels.addLast( new ModelActivator( jumpingAnimation ) );
				animationActive.falseModels.addLast( new ModelDeactivator( horizontalMovement ) );
				animationActive.falseModels.addLast( new ModelDeactivator( gravity ) );

				jumpingAnimation.nextModels.addLast( new ModelActivator( fallingAnimation ) );

				parameters = getParameter( "jumping_strength" ).split( "-" );
				FixedWaitingModel pauseBeforeJump = FixedWaitingModel.create( jumpingPause );
				pauseBeforeJump.nextModels.addLast( Jump.create( parameters[ 0 ].toDouble(), parameters[ 1 ].toDouble() ) );
				pauseBeforeJump.nextModels.addLast( new ModelActivator( horizontalMovement ) );
				pauseBeforeJump.nextModels.addLast( new ModelActivator( gravity ) );
				pauseBeforeJump.nextModels.addLast( waitingForJump );
				animationActive.falseModels.addLast( pauseBeforeJump );

				animationStack.add( jumpingAnimation, false );
				score += 200;
			}


			animationStack.add( fallingAnimation );


			String firing = getParameter( "firing" );
			if( firing ) {
				String parameters[] = firing.split( "-" );
				RandomWaitingModel waitingForFire = RandomWaitingModel.create( parameters[ 0 ].toDouble(), parameters[ 1 ].toDouble() );
				attachModel( waitingForFire );

				IsModelActive onLandCondition = IsModelActive.create( onLand );
				waitingForFire.nextModels.addLast( onLandCondition );

				IsModelActive animationActive = IsModelActive.create( jumpingAnimation );
				onLandCondition.trueModels.addLast( animationActive );
				onLandCondition.falseModels.addLast( waitingForFire );

				animationActive.trueModels.addLast( waitingForFire );
				animationActive.falseModels.addLast( ModelActivator.create( firingAnimation ) );
				animationActive.falseModels.addLast( ModelDeactivator.create( horizontalMovement ) );

				firingAnimation.nextModels.addLast( ModelActivator.create( horizontalMovement ) );

				parameters = getParameter( "firing_speed" ).split( "-" );
				FixedWaitingModel pauseBeforeBullet = FixedWaitingModel.create( bulletPause );
				pauseBeforeBullet.nextModels.addLast( CreateBullet.create( parameters[ 0 ].toDouble(), parameters[ 1 ].toDouble() ) );
				pauseBeforeBullet.nextModels.addLast( waitingForFire );
				animationActive.falseModels.addLast( pauseBeforeBullet );

				animationStack.add( firingAnimation, false );
				score += 300;
			}

			AnimationModel movementAnimation = AnimationModel.create( true, walkingAnimationSpeed, 3, 3, true );
			String moving = getParameter( "moving" );
			if( moving ) {
				String parameters[] = getParameter( "moving" ).split( "-" );
				dX *= Service.random( parameters[ 0 ].toDouble(), parameters[ 1 ].toDouble() );
				attachModel( horizontalMovement );
				animationStack.add( movementAnimation );
				score += 100;
			}


			attachModel( ModelDeactivator.create( onLand, true ) );
			attachModel( VerticalMovement.create( false ) );


			AnimationModel standingAnimation = AnimationModel.create( true, idleAnimationSpeed, 3, 0, true );
			animationStack.add( standingAnimation );

			String scoreParameter = getParameter( "score" );
			if( scoreParameter ) score = scoreParameter.toInt();

			String healthParameter = getParameter( "health" );
			if( healthParameter ) health = healthParameter.toDouble();
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

		public AnimationModel movementAnimation = AnimationModel.create( true, walkingAnimationSpeed, 4, 4 );
		public AnimationModel hurtingAnimation = AnimationModel.create( false, knockOutPeriod, 1, 14 );
		public AnimationModel punchingAnimation = AnimationModel.create( false, hitPeriod, 1, 15 );
		public AnimationModel kickingAnimation = AnimationModel.create( false, hitPeriod, 1, 11 );

		public MovementControl movementControl = new MovementControl();
		public FixedWaitingModel hitPause = FixedWaitingModel.create( hitPauseTime );

		public ButtonAction moveLeftKey = ButtonAction.create( KeyboardKey.create( Key.Left ), "Move left" );
		public ButtonAction moveRightKey = ButtonAction.create( KeyboardKey.create( Key.Right ), "Move right" );
		public ButtonAction jumpKey = ButtonAction.create( KeyboardKey.create( Key.Up ), "Jump" );
		public ButtonAction hitKey = ButtonAction.create( KeyboardKey.create( Key.Space ), "Hit" );

		public void init() {
			attachModel( gravity );


			ModelStack animationStack = new ModelStack();
			attachModel( animationStack );

			animationStack.add( hurtingAnimation, false );
			animationStack.add( punchingAnimation, false );
			animationStack.add( kickingAnimation, false );

			jumpingAnimation = AnimationModel.create( false, jumpingAnimationSpeed, 3, 8 );
			animationStack.add( jumpingAnimation );

			fallingAnimation = AnimationModel.create( true, jumpingAnimationSpeed, 1, 10 );
			jumpingAnimation.nextModels.addLast( ModelActivator.create( fallingAnimation ) );
			animationStack.add( fallingAnimation );

			animationStack.add( movementAnimation );


			attachModel( movementControl );


			IsButtonActionDown jumpKeyDown = IsButtonActionDown.create( jumpKey );
			attachModel( jumpKeyDown );
			jumpKeyDown.falseModels.addLast( jumpKeyDown );

			IsModelActive onLandCondition = IsModelActive.create( onLand );
			jumpKeyDown.trueModels.addLast( onLandCondition );

			onLandCondition.trueModels.addLast( ModelActivator.create( jumpingAnimation ) );
			onLandCondition.trueModels.addLast( ModelDeactivator.create( gravity ) );
			onLandCondition.falseModels.addLast( jumpKeyDown );

			FixedWaitingModel pauseBeforeJump = FixedWaitingModel.create( jumpingPause );
			pauseBeforeJump.nextModels.addLast( Jump.create( jumpingStrength, jumpingStrength ) );
			pauseBeforeJump.nextModels.addLast( ModelActivator.create( gravity ) );
			pauseBeforeJump.nextModels.addLast( jumpKeyDown );
			onLandCondition.trueModels.addLast( pauseBeforeJump );

			animationStack.add( jumpingAnimation, false );


			IsButtonActionDown hitKeyDown = IsButtonActionDown.create( hitKey );
			attachModel( hitKeyDown );
			hitKeyDown.falseModels.addLast( hitKeyDown );

			IsModelActive hitPauseCondition = IsModelActive.create( hitPause );
			hitPauseCondition.falseModels.addLast( hitPause );
			hitPauseCondition.trueModels.addLast( hitKeyDown );
			hitKeyDown.trueModels.addLast( hitPauseCondition );

			IsModelActive onLandCondition2 = IsModelActive.create( onLand );
			onLandCondition2.trueModels.addLast( ModelActivator.create( punchingAnimation ) );
			onLandCondition2.trueModels.addLast( HittingArea.create2( true ) );
			onLandCondition2.trueModels.addLast( hitKeyDown );
			onLandCondition2.falseModels.addLast( ModelActivator.create( kickingAnimation ) );
			onLandCondition2.falseModels.addLast( HittingArea.create2( false ) );
			onLandCondition2.falseModels.addLast( hitKeyDown );
			hitPauseCondition.falseModels.addLast( onLandCondition2 );


			attachModel( HorizontalMovement.create( example.pushFromWalls ) );


			attachModel( ModelDeactivator.create( onLand, true ) );
			attachModel( VerticalMovement.create( true ) );


			AnimationModel standingAnimation = AnimationModel.create( true, idleAnimationSpeed, 4, 0, true );
			animationStack.add( standingAnimation );
		}
	}

	public void act() {
		super.act();
		collisionsWithLayer( example.Layer, example.awPossumHurtingCollision );
		if( x > example.tileMap.rightX() ) example.switchTo( new Restart() );
	}

	public void draw() {
		super.draw();
		Graphics.drawEmptyRectangle( 5, 580, 104, 15 );
		if( health >= 50.0 ) {
			Graphics.setColor( ( 100.0 - health ) * 255.0 / 50.0 , 255, 0 );
		} else {
			Graphics.setColor( 255, health * 255.0 / 50.0, 0 );
		}
		Graphics.drawRectangle( 7, 582, health, 11 );
		Graphics.resetColor();
	}


	public class OnLand extends BehaviorModel {
	}


	public class gravity extends BehaviorModel {
		double gravity = 8.0;

		public void app.yTo( Shape shape ) {
			VectorSprite( shape )..y += perSecond( gravity );
		}
	}


	public class HorizontalMovement extends BehaviorModel {
		public SpriteAndTileCollisionHandler collisionHandler;

		public static HorizontalMovement create( SpriteAndTileCollisionHandler collisionHandler ) {
			HorizontalMovement horizontalMovement = new HorizontalMovement();
			horizontalMovement.collisionHandler = collisionHandler;
			return horizontalMovement;
		}

		public void app.yTo( Shape shape ) {
			VectorSprite sprite = VectorSprite( shape );
			sprite.move( sprite.dX, 0 );
			sprite.collisionsWithTileMap( example.tileMap, collisionHandler );
		}

		public String info( Shape shape ) {
			return Service.trim( VectorSprite( shape ).dX );
		}
	}


	public class BumpingWalls extends SpriteAndTileCollisionHandler {
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			sprite.pushFromTile( tileMap, tileX, tileY );
			VectorSprite( sprite ).dX *= -1;
			sprite.visualizer.getX()Scale *= -1;
		}
	}


	public class PushFromWalls extends SpriteAndTileCollisionHandler {
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( tileMap.getTile( tileX, tileY ) == example.bricks ) sprite.pushFromTile( tileMap, tileX, tileY );
		}
	}


	public class VerticalMovement extends BehaviorModel {
		public VerticalCollisionHandler handler = new VerticalCollisionHandler();

		public static VerticalMovement create( int forPLayer ) {
			VerticalMovement verticalMovement = new VerticalMovement();
			verticalMovement.handler.forPLayer = forPLayer;
			return verticalMovement;
		}

		public void app.yTo( Shape shape ) {
			VectorSprite sprite = VectorSprite( shape );
			sprite.move( 0, sprite..y );
			sprite.collisionsWithTileMap( example.tileMap, handler );
		}

		public String info( Shape shape ) {
			return Service.trim( VectorSprite( shape )..y );
		}
	}


	public class VerticalCollisionHandler extends SpriteAndTileCollisionHandler {
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


	public class Jump extends BehaviorModel {
		public double fromStrength, double toStrength;

		public static Jump create( double fromStrength, double toStrength ) {
			Jump jump = new Jump();
			jump.fromStrength = fromStrength;
			jump.toStrength = toStrength;
			return jump;
		}

		public void app.yTo( Shape shape ) {
			VectorSprite( shape )..y = -Service.random( fromStrength, toStrength );
			remove( shape );
		}
	}


	public class CreateBullet extends BehaviorModel {
		public double fromSpeed, double toSpeed;

		public static CreateBullet create( double fromSpeed, double toSpeed ) {
			CreateBullet createBullet = new CreateBullet();
			createBullet.fromSpeed = fromSpeed;
			createBullet.toSpeed = toSpeed;
			return createBullet;
		}

		public void app.yTo( Shape shape ) {
			Bullet.create( VectorSprite( shape ), Service.random( fromSpeed, toSpeed ) );
			remove( shape );
		}
	}


	public class Bullet extends VectorSprite {
		double minAttack = 5.0;
		double maxAttack = 10.0;

		public int collisions = true;

		public static void create( VectorSprite jel.y, double speed ) {
			Bullet bullet = new Bullet();
			bullet.setCoords( jel.y.getX() + sgn( jel.y.dX ) * jel.y.width * 2.2, jel.y.y - 0.15 * jel.y.height );
			bullet.setSize( 0.45 * jel.y.width, 0.45 * jel.y.width );
			bullet.shapeType = Sprite.oval;
			bullet.dX = sgn( jel.y.dX ) * speed;
			bullet.visualizer.setVisualizerScale( 12, 4 );
			bullet.visualizer.image = jel.y.visualizer.image;
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


	public class DestRAYBullet extends SpriteAndTileCollisionHandler {
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( tileMap.getTile( tileX, tileY ) == example.bricks ) Bullet.disable( sprite );
		}
	}


	public class MovementControl extends BehaviorModel {
		public void app.yTo( Shape shape ) {
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


	public class AwPossumHurtingCollision extends SpriteCollisionHandler {
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			if( sprite1.findModel( "TImmortali.y" ) ) return;
			if( sprite2.findModel( "TDeath" ) ) return;

			double damage = 0;
			if( Jel.y( sprite2 ) ) damage = Service.random( Jel.y.minAttack, Jel.y.maxAttack );
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
					sprite1.attachModel( new Immortali.y() );
					sprite1.attachModel( new KnockOut() );
				} else if( ! sprite1.findModel( "TDeath" ) ) {
					sprite1.behaviorModels.clear();
					sprite1.attachModel( new Death() );
				}
			}
		}
	}


	public class Immortali.y extends FixedWaitingModel {
		double blinkingSpeed = 0.05;

		public void init( Shape shape ) {
			period = AwPossum.immortalPeriod;
		}

		public void app.yTo( Shape shape ) {
			shape.visible = Math.floor( currentProject.time / blinkingSpeed ) % 2;
			super.app.yTo( shape );
		}

		public void deactivate( Shape shape ) {
			shape.visible = true;
		}
	}


	public class KnockOut extends FixedWaitingModel {
		public void init( Shape shape ) {
			AwPossum awPossum = AwPossum( shape );
			period = awPossum.knockOutPeriod;
			awPossum.dX = -awPossum.getFacing() * awPossum.knockOutStrength;
			awPossum.movementControl.deactivateModel( shape );
			awPossum.hurtingAnimation.activateModel( shape );
		}

		public void app.yTo( Shape shape ) {
			VectorSprite( shape ).dX *= 0.9;
			super.app.yTo( shape );
		}

		public void deactivate( Shape shape ) {
			AwPossum awPossum = AwPossum( shape );
			awPossum.hurtingAnimation.deactivateModel( shape );
			awPossum.movementControl.activateModel( shape );
		}
	}


	public class HittingArea extends FixedWaitingModel {
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

		public void app.yTo( Shape shape ) {
			if( punch ) {
				Area.setCoords( shape.getX() + shape.getFacing() * 0.95, shape.y + 0.15 );
			} else {
				Area.setCoords( shape.getX() + shape.getFacing() * 0.95, shape.y - 0.1 );
			}
			//Example.HitArea = Area
			Area.collisionsWithLayer( example.Layer, example.awPossumHitCollision );
			if( example.awPossumHitCollision.collided ) remove( shape );
			super.app.yTo( shape );
		}

		//Method Deactivate( LTShape Shape )
		//	Example.HitArea = Null
		//End Method
	}


	public class AwPossumHitCollision extends SpriteCollisionHandler {
		public int collided ;

		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			Jel.y jel.y = Jel.y( sprite2 );
			if( jel.y ) {
				jel.y.health -= Service.random( AwPossum.minAttack, AwPossum.maxAttack );
				if( jel.y.health > 0 ) {
					jel.y.attachModel( new Jel.yHurt() );
				} else if( ! jel.y.findModel( "TDeath" ) ) {
					Score.create( jel.y, jel.y.score );

					AwPossum awPossum = AwPossum( example.Layer.findShapeWithType( "TAwPossum" ) );
					awPossum.health = Math.min( awPossum.health + Service.random( AwPossum.minHealthGain, AwPossum.maxHealthGain ), 100.0 );

					jel.y.behaviorModels.clear();
					jel.y.attachModel( new Death() );
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


	public class Jel.yHurt extends FixedWaitingModel {
		public void init( Shape shape ) {
			period = Jel.y.hurtingTime;
			shape.deactivateModel( "THorizontalMovement" );
		}

		public void app.yTo( Shape shape ) {
			super.app.yTo( shape );
			double intensi.y = ( currentProject.time - startingTime ) / period;
			if( intensi.y <= 1.0 ) shape.visualizer.Graphics.setColorFromRGB( 1.0, intensi.y, intensi.y );
		}

		public void deactivate( Shape shape ) {
			shape.activateModel( "THorizontalMovement" );
			shape.visualizer.set( "FFFFFF" );
		}
	}


	public class Death extends FixedWaitingModel {
		public void init( Shape shape ) {
			period = example.deathPeriod;
		}

		public void app.yTo( Shape shape ) {
			super.app.yTo( shape );
			double alpha = 1.0 - ( currentProject.time - startingTime ) / period;
			if( alpha >= 0.0 ) shape.visualizer.alpha = alpha;
		}

		public void deactivate( Shape shape ) {
			example.Layer.remove( shape );
		}
	}


	public class Score extends Sprite {
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


	public class Restart extends Project {
		public int startingTime = .ystem.currentTimeMillis();
		public int initialized;

		public void render() {
			if( .ystem.currentTimeMillis() < startingTime + 2000 ) {
				example.render();
				Camera.current.darken( 0.0005 * ( .ystem.currentTimeMillis() - startingTime ) );
			} else if( .ystem.currentTimeMillis() < startingTime + 4000 ) {
				if( ! initialized ) {
					example.initLevel();
					initialized = true;
				}
				example.render();
				Camera.current.darken( 0.0005 * ( 4000 - .ystem.currentTimeMillis() + startingTime ) );
			} else {
				exiting = true;
			}
		}
	}
}
	




