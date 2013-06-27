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
import dwlab.shapes.Shape.Facing;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.*;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Color;
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
	static Layer layer;
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
		layer = world.findShape( "Level" ).load().toLayer();
		layer.init();
		tileMap = layer.findShape( "Field" ).toTileMap();
	}

	
	ButtonAction buttonSelect = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	@Override
	public void logic() {
		Camera.current.jumpTo( tileMap );
		if( buttonSelect.wasPressed() ) selectedSprite = cursor.lastCollidedSpriteOfLayer( layer );
		layer.act();
		if( KeyExit.wasPressed() ) exiting = true;
	}

	
	@Override
	public void render() {
		layer.draw();
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
		static double jumpingAnimationSpeed = 0.2;
		static double firingAnimationSpeed = 0.1;
		static double walkingAnimationSpeed = 0.2;
		static double idleAnimationSpeed = 0.4;
		static double minAttack = 10.0;
		static double maxAttack = 20.0;
		static double hurtingTime = 0.2;

		static double jumpingPause = jumpingAnimationSpeed * 2.0;
		static double bulletPause = firingAnimationSpeed * 5.0;

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



	public static class AwPossum extends GameObject {
		static final double jumpingAnimationSpeed = 0.2;
		static double walkingAnimationSpeed = 0.2;
		static double idleAnimationSpeed = 0.4;

		static double jumpingPause = jumpingAnimationSpeed;
		static double jumpingStrength = 6.0;
		static double walkingSpeed = 5.0;

		static double minAttack = 20.0;
		static double maxAttack = 35.0;
		static double minHealthGain = 3.0;
		static double maxHealthGain = 6.0;

		static double knockOutPeriod = 0.3;
		static double immortalPeriod = 1.5;
		static double hitPeriod = 0.2;
		static double knockOutStrength = 2.0;
		static double hitPauseTime = 0.5;

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


			addTileMapCollisions( VectorSpriteCollisionsModel.Group.ALL, tileMap, pushFromWalls, null );


			attachModel( new ModelDeactivator( onLand, true ) );


			AnimationModel standingAnimation = new AnimationModel( true, idleAnimationSpeed, 4, 0, true );
			addToStack( standingAnimation );
		}


		@Override
		public void act() {
			super.act();
			collisionsWithLayer( layer, awPossumHurtingCollision );
			if( x > tileMap.rightX() ) switchTo( new Restart() );
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

	public static class OnLand extends BehaviorModel {
	}


	public static class gravity extends BehaviorModel<VectorSprite> {
		double gravity = 8.0d;

		@Override
		public void applyTo( VectorSprite sprite ) {
			sprite.alterCoords( 0d, perSecond( gravity ) );
		}
	}


	public static class BumpingWalls extends SpriteAndTileCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			sprite.pushFromTile( tileMap, tileX, tileY );
			( (VectorSprite) sprite ).dX *= -1;
			sprite.visualizer.xScale *= -1;
		}
	}


	public static class PushFromWalls extends SpriteAndTileCollisionHandler {
		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( tileMap.getTile( tileX, tileY ) == bricks ) sprite.pushFromTile( tileMap, tileX, tileY );
		}
	}
	

	public static class VerticalCollisionHandler extends SpriteAndTileCollisionHandler {
		public boolean forPLayer;

		@Override
		public void handleCollision( Sprite sprite, TileMap tileMap, int tileX, int tileY, Sprite collisionSprite ) {
			if( forPLayer ) if( tileMap.getTile( tileX, tileY ) != bricks ) return;
			GameObject gameObject = (GameObject) sprite;
			gameObject.pushFromTile( tileMap, tileX, tileY );
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


	public static class CreateBullet extends BehaviorModel<VectorSprite> {
		public double fromSpeed, toSpeed;

		public static CreateBullet create( double fromSpeed, double toSpeed ) {
			CreateBullet createBullet = new CreateBullet();
			createBullet.fromSpeed = fromSpeed;
			createBullet.toSpeed = toSpeed;
			return createBullet;
		}

		@Override
		public void applyTo( VectorSprite sprite ) {
			Bullet.create( sprite, Service.random( fromSpeed, toSpeed ) );
			remove( sprite );
		}
	}


	public static class Bullet extends VectorSprite {
		static double minAttack = 5.0;
		static double maxAttack = 10.0;

		public boolean collisions = true;

		public static void create( VectorSprite Jelly, double speed ) {
			Bullet bullet = new Bullet();
			bullet.setCoords( Jelly.getX() + Math.signum( Jelly.dX ) * Jelly.getWidth() * 2.2, Jelly.getY() - 0.15 * Jelly.getHeight() );
			bullet.setSize( 0.45 * Jelly.getWidth(), 0.45 * Jelly.getWidth() );
			bullet.shapeType = ShapeType.oval;
			bullet.dX = Math.signum( Jelly.dX ) * speed;
			bullet.visualizer.setVisualizerScale( 12, 4 );
			bullet.visualizer.image = Jelly.visualizer.image;
			bullet.frame = 6;
			layer.addLast( bullet );
		}
		

		@Override
		public void act() {
			moveForward();
			if( collisions ) collisionsWithTileMap( tileMap, destroyBullet );
			super.act();
		}
		

		public static void disable( Sprite sprite ) {
			Bullet bullet = (Bullet) sprite;
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
			if( tileMap.getTile( tileX, tileY ) == bricks ) Bullet.disable( sprite );
		}
	}


	public static class MovementControl extends BehaviorModel<AwPossum> {
		public void applyTo( AwPossum awPossum ) {
			if( awPossum.gravity.active ) {
				if( awPossum.moveLeftKey.isDown() ) {
					awPossum.setFacing( Facing.LEFT );
					awPossum.dX = -awPossum.walkingSpeed;
				} else if( awPossum.moveRightKey.isDown() ) {
					awPossum.setFacing( Facing.RIGHT );
					awPossum.dX = awPossum.walkingSpeed;
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
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			if( sprite1.findModel( Immortality.class ) != null ) return;
			if( sprite2.findModel( Death.class ) != null  ) return;

			double damage = 0;
			if( ( (Jelly) sprite2 ) != null ) damage = Service.random( Jelly.minAttack, Jelly.maxAttack );
			Bullet bullet = (Bullet) sprite2;
			if( bullet != null ) {
				if( bullet.collisions ) {
					damage = Service.random( Bullet.minAttack, Bullet.maxAttack ) * sprite2.getDiameter() / 0.45;
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
			shape.visible = ( Math.floor( time / blinkingSpeed ) % 2 ) != 0;
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
		public Sprite Area;
		public boolean punch;

		public static HittingArea create2( boolean punch ) {
			HittingArea Area = new HittingArea();
			Area.punch = punch;
			return Area;
		}

		@Override
		public void init( Shape shape ) {
			Area = new Sprite();
			Area.shapeType = ShapeType.oval;
			Area.setDiameter( 0.3 );
			period = AwPossum.hitPeriod;
			awPossumHitCollision.collided = false;
		}

		@Override
		public void applyTo( Shape shape ) {
			if( punch ) {
				Area.setCoords( shape.getX() + Math.signum( shape.visualizer.xScale ) * 0.95d, shape.getY() + 0.15d );
			} else {
				Area.setCoords( shape.getX() + Math.signum( shape.visualizer.xScale ) * 0.95d, shape.getY() - 0.1d );
			}
			Area.collisionsWithLayer( layer, awPossumHitCollision );
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
					jelly.attachModel( new JellyHurt() );
				} else if( jelly.findModel( Death.class ) == null ) {
					Score.create( jelly, jelly.score );

					AwPossum awPossum = (AwPossum) layer.findShape( AwPossum.class );
					awPossum.health = Math.min( awPossum.health + Service.random( AwPossum.minHealthGain, AwPossum.maxHealthGain ), 100.0 );

					jelly.behaviorModels.clear();
					jelly.attachModel( new Death() );
				}
				collided = true;
			} else if( sprite2 instanceof Bullet ) {
				if( sprite2.findModel( Death.class ) == null ) {
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
			double intensi.getY() = ( currentProject.time - startingTime ) / period;
			if( intensi.getY() <= 1.0 ) shape.visualizer.Graphics.setColorFromRGB( 1.0, intensi.getY(), intensi.getY() );
		}

		public void deactivate( Shape shape ) {
			shape.activateModel( "THorizontalMovement" );
			shape.visualizer.set( "FFFFFF" );
		}
	}


	public static class Death extends FixedWaitingModel {
		public void init( Shape shape ) {
			period = deathPeriod;
		}

		public void applyTo( Shape shape ) {
			super.applyTo( shape );
			double alpha = 1.0 - ( currentProject.time - startingTime ) / period;
			if( alpha >= 0.0 ) shape.visualizer.alpha = alpha;
		}

		public void deactivate( Shape shape ) {
			Layer.remove( shape );
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
			score += amount;
			Layer.addLast( score );
		}

		public void act() {
			move( 0, -speed );
			if( currentProject.time > startingTime + period ) Layer.remove( this );
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
				render();
				Camera.current.darken( 0.0005 * ( System.currentTimeMillis() - startingTime ) );
			} else if( System.currentTimeMillis() < startingTime + 4000 ) {
				if( ! initialized ) {
					initLevel();
					initialized = true;
				}
				render();
				Camera.current.darken( 0.0005 * ( 4000 - System.currentTimeMillis() + startingTime ) );
			} else {
				exiting = true;
			}
		}
	}
}