package examples;
import dwlab.base.*;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.visualizers.ContourVisualizer;
import java.util.LinkedList;

public class SpriteMapExample extends Project {
	static SpriteMapExample instance = new SpriteMapExample();
	
	public static void main(String[] argv) {
		Graphics.init();
		instance.act();
	}
	
	
	static final int spritesQuantity = 800;
	static final int mapSize = 192;

	static final Sprite rectangle = new Sprite( 0, 0, mapSize, mapSize );
	static final SpriteMap fieldSpriteMap = new SpriteMap( rectangle, 2.0 );
	static SpriteCollisionHandler collisionHandler = new SpriteCollisionHandler() {
		@Override
		public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
			if( ParticleArea.class.isInstance( sprite2 ) ) return;
			sprite1.pushFromSprite( sprite2 );
			sprite1.angle = sprite2.directionTo( sprite1 );
			sprite2.angle = sprite1.directionTo( sprite2 );
			ParticleArea.create( sprite1, sprite2 );
		}
	};

	
	@Override
	public void init() {
		cursor = new Sprite( 0, 0 );
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Ball.create();
		}
		rectangle.visualizer = new ContourVisualizer( 0.1, "FF0000" );
		fieldSpriteMap.initialArraysSize = 2;
	}

	
	@Override
	public void logic() {
		Camera.current.move( 0.1 * ( Sys.mouseX() - 400 ), 0.1 * ( Sys.mouseY() - 300 ) );
		fieldSpriteMap.act();
	}

	
	@Override
	public void render() {
		fieldSpriteMap.draw();
		rectangle.draw();
		Graphics.drawOval( 400, 300, 5, 5 );
		cursor.draw();
		printText( "LTSpriteMap, CollisionsWithSpriteMap example", Align.TO_CENTER, Align.TO_BOTTOM );
		showDebugInfo();
	}



	public static class Ball extends Sprite {
		public static Ball create() {
			Ball ball = new Ball();
			ball.setCoords( Service.random( -0.5 * ( mapSize - 2 ), 0.5 * ( mapSize - 2 ) ), Service.random( -0.5 * ( mapSize - 2 ), 0.5 * ( mapSize - 2 ) ) );
			ball.setDiameter( Service.random( 0.5, 1.5 ) );
			ball.angle = Service.random( 360 );
			ball.velocity = Service.random( 3, 7 );
			ball.shapeType = ShapeType.oval;
			ball.visualizer.setRandomColor();
			fieldSpriteMap.insertSprite( ball );
			return ball;
		}
		

		@Override
		public void act() {
			super.act();
			Camera.current.bounceInside( rectangle );
			moveForward();
			bounceInside( rectangle );
			collisionsWithSpriteMap( fieldSpriteMap, collisionHandler, null );
		}
	}


	public static class ParticleArea extends Sprite {
		static int particlesQuantity = 30;
		static double fadingTime = 1.0;

		LinkedList<Sprite> particles = new LinkedList<Sprite>();
		double startingTime;

		public static void create( Sprite ball1, Sprite ball2 ) {
			ParticleArea Area = new ParticleArea();
			double diameters = ball1.getDiameter() + ball2.getDiameter();
			Area.setCoords( ball1.getX() + ( ball2.getX() - ball1.getX() ) * ball1.getDiameter() / diameters, ball1.getY() + ( ball2.getY() - ball1.getY() ) * ball1.getDiameter() / diameters );
			Area.setSize( 4, 4 );
			Area.startingTime = instance.time;
			double angle = ball1.directionTo( ball2 ) + 90;
			for( int n = 0; n < particlesQuantity; n++ ) {
				Sprite particle = new Sprite();
				particle.jumpTo( Area );
				particle.angle = angle + Service.random( -15, 15 ) + ( n % 2 ) * 180;
				particle.setDiameter( Service.random( 0.2, 0.6 ) );
				particle.velocity = Service.random( 0.5, 3 );
				Area.particles.addLast( particle );
			}
			fieldSpriteMap.insertSprite( Area );
		}
		

		@Override
		public void draw() {
			double a = 1d - ( instance.time - startingTime ) / fadingTime;
			if( a >= 0 ) {
				Graphics.setColor( 1d, 0.75d, 0d, a );
				for( Sprite sprite : particles ) {
					double dX = Math.cos( sprite.angle ) * sprite.getDiameter() * 0.5d;
					double dY = Math.sin( sprite.angle ) * sprite.getDiameter() * 0.5d;
					Vector vector1 = new Vector(), vector2 = new Vector();
					Camera.current.fieldToScreen( sprite.getX() - dX, sprite.getY() - dY, vector1 );
					Camera.current.fieldToScreen( sprite.getX() + dX, sprite.getY() + dY, vector2 );
					Graphics.drawLine( vector1.x, vector1.y , vector2.x, vector2.y );
					sprite.moveForward();
				}
				Graphics.resetColor();
			}
		}
		

		@Override
		public void act() {
			if( instance.time > startingTime + fadingTime ) fieldSpriteMap.removeSprite( this, true );

			if( collidesWithSprite( Camera.current ) ) {
				for( Sprite sprite : particles ) sprite.moveForward();
			}
		}
	}
}