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
import examples.AwPossum.*;
import examples.Jelly.*;

public class BehaviorModelExample extends Project {
	static {
		Graphics.init();
	}
	
	static BehaviorModelExample instance = new BehaviorModelExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	public static int bricks = 1;
	public static double deathPeriod = 1.0;

	public static World world;
	public static Layer layer;
	public static TileMap tileMap;
	public static Sprite selectedSprite;
	public static MarchingAnts marchingAnts = new MarchingAnts();

	public static BumpingWalls bumpingWalls = new BumpingWalls();
	public static PushFromWalls pushFromWalls = new PushFromWalls();
	public static DestroyBullet destroyBullet = new DestroyBullet();
	public static AwPossumHurtingCollision awPossumHurtingCollision = new AwPossumHurtingCollision();
	public static AwPossumHitCollision awPossumHitCollision = new AwPossumHitCollision();

	public static int score;

	public ButtonAction KeyExit = ButtonAction.create( KeyboardKey.create( Key.ESCAPE) );
	
	
	@Override
	public void init() {
	 	world = World.fromFile( "res/jellys.lw" );

		Graphics.init();

		Sprite sprite = new Sprite( Camera.current );
		sprite.visualizer.image = new Image( "res/scheme2.png" );
		while ( !KeyExit.wasPressed() ) {
			sprite.draw();
			processEvents();
			Graphics.swapBuffers();
		}

		sprite.visualizer.image = new Image( "res/scheme1.png" );
		while ( !KeyExit.wasPressed() ) {
			sprite.draw();
			processEvents();
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
		showDebugInfo();
		printText( "Guide AwesomePossum to exit from maze using arrow and space keys", Align.TO_RIGHT, Align.TO_TOP );
		printText( "you can view sprite behavior models by clicking left mouse button on it", Align.TO_RIGHT, Align.TO_TOP, 1 );
		printText( "Score" + Service.firstZeroes( score, 6 ), Align.TO_RIGHT, Align.TO_BOTTOM );
		printText( "LTBehaviorModel example", Align.TO_CENTER, Align.TO_BOTTOM );
	}



	public static class OnLand extends BehaviorModel {
	}


	public static class Gravity extends BehaviorModel<VectorSprite> {
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


	public static class MovementControl extends BehaviorModel {
		@Override
		public void applyTo( Shape shape  ) {
			AwPossum awPossum = (AwPossum) shape;
			if( awPossum.gravity.active ) {
				if( awPossum.moveLeftKey.isDown() ) {
					awPossum.setFacing( Facing.LEFT );
					awPossum.dX = -AwPossum.walkingSpeed;
				} else if( awPossum.moveRightKey.isDown() ) {
					awPossum.setFacing( Facing.RIGHT );
					awPossum.dX = AwPossum.walkingSpeed;
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


	public static class Score extends Sprite {
		static double speed = 2.0;
		static double period = 3.0;

		int amount;
		double startingTime;

		public static void create( Sprite sprite, int amount ) {
			Score scoreObject = new Score();
			scoreObject.setCoords( sprite.getX(), sprite.topY() );
			scoreObject.amount = amount;
			scoreObject.setDiameter( 0 );
			scoreObject.startingTime = BehaviorModelExample.instance.time;
			scoreObject.insertTo( layer );
			score += amount;
		}

		@Override
		public void act() {
			move( 0, -speed );
			if( BehaviorModelExample.instance.time > startingTime + period ) removeFrom( layer );
		}

		@Override
		public void draw( Color drawingColor ) {
			printText( "+" + amount, Align.TO_CENTER, Align.TO_BOTTOM );
		}
	}


	public static class Restart extends Project {
		public long startingTime2 = System.currentTimeMillis();
		public boolean initialized;

		@Override
		public void render() {
			if( System.currentTimeMillis() < startingTime2 + 2000 ) {
				render();
				Camera.current.darken( 0.0005 * ( System.currentTimeMillis() - startingTime2 ) );
			} else if( System.currentTimeMillis() < startingTime2 + 4000 ) {
				if( ! this.initialized ) {
					instance.initLevel();
					initialized = true;
				}
				render();
				Camera.current.darken( 0.0005 * ( 4000 - System.currentTimeMillis() + startingTime2 ) );
			} else {
				exiting = true;
			}
		}
	}
}