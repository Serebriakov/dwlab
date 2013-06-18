package examples;

import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Sys;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.maps.SpriteMap;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Color;
import dwlab.visualizers.ContourVisualizer;
import java.util.LinkedList;

public class SpriteMapExample extends Project {
	static SpriteMapExample instance = new SpriteMapExample();
	
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		instance.act();
		Graphics.initCamera();
	}
	
	
	static final int spritesQuantity = 800;
	static final int mapSize = 192;

	static final Sprite rectangle = new Sprite( 0, 0, mapSize, mapSize );
	static SpriteMap fieldSpriteMap = new SpriteMap( rectangle, 2d );
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
		for( int n = 1; n <= spritesQuantity; n++ ) Ball.create();
		rectangle.visualizer = new ContourVisualizer( 0.1d, "FF0000" );
		fieldSpriteMap.initialArraysSize = 2;
	}

	
	@Override
	public void logic() {
		Camera.current.move( 0.1d * ( Sys.mouseX() - 400 ), 0.1d * ( Sys.mouseY() - 300 ) );
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
			ball.setCoords( Service.random( -0.5d * ( mapSize - 2 ), 0.5d * ( mapSize - 2 ) ), Service.random( -0.5d * ( mapSize - 2 ), 0.5d * ( mapSize - 2 ) ) );
			ball.setDiameter( Service.random( 0.5d, 1.5d ) );
			ball.angle = Service.random( 360d );
			ball.velocity = Service.random( 3d, 7d );
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
			collisionsWithSpriteMap( fieldSpriteMap, collisionHandler );
		}
	}


	public static class ParticleArea extends Sprite {
		static int particlesQuantity = 30;
		static double fadingTime = 1d;

		LinkedList<Sprite> particles = new LinkedList<Sprite>();
		double startingTime;

		public static void create( Sprite ball1, Sprite ball2 ) {
			ParticleArea area = new ParticleArea();
			double diameters = ball1.getDiameter() + ball2.getDiameter();
			area.setCoords( ball1.getX() + ( ball2.getX() - ball1.getX() ) * ball1.getDiameter() / diameters, ball1.getY() + ( ball2.getY() - ball1.getY() ) * ball1.getDiameter() / diameters );
			area.setSize( 4d, 4d );
			area.startingTime = instance.time;
			double angle = ball1.directionTo( ball2 ) + 0.5d * Math.PI;
			for( int n = 1; n <= particlesQuantity; n++ ) {
				Sprite particle = new Sprite();
				particle.jumpTo( area );
				particle.angle = angle + Service.random( -Math.PI / 12d, Math.PI / 12d ) + ( n % 2 ) * Math.PI;
				particle.setDiameter( Service.random( 0.2d, 0.6d ) );
				particle.velocity = Service.random( 0.5d, 3d );
				area.particles.addLast( particle );
			}
			area.insertTo( fieldSpriteMap );
		}
		

		
		private Vector vector1 = new Vector(), vector2 = new Vector();
		
		@Override
		public void draw( Color drawingColor ) {
			double a = 1d - 1d * ( instance.time - startingTime ) / fadingTime;
			if( a >= 0 ) {
				Graphics.setCurrentColor( 1d, 0.75d, 0d, a );
				for( Sprite sprite : particles ) {
					double dX = Math.cos( sprite.angle ) * sprite.getDiameter() * 0.5d;
					double dY = Math.sin( sprite.angle ) * sprite.getDiameter() * 0.5d;
					
					Camera.current.fieldToScreen( sprite.getX() - dX, sprite.getY() - dY, vector1 );
					Camera.current.fieldToScreen( sprite.getX() + dX, sprite.getY() + dY, vector2 );
					Graphics.drawLine( vector1.x, vector1.y , vector2.x, vector2.y );
					sprite.moveForward();
				}
				Graphics.resetCurrentColor();
			}
		}
		

		@Override
		public void act() {
			if( instance.time > startingTime + fadingTime ) removeFrom( fieldSpriteMap );

			if( collidesWithSprite( Camera.current ) ) {
				for( Sprite sprite : particles ) sprite.moveForward();
			}
		}
	}
}