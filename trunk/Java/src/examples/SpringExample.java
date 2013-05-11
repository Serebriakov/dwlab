package examples;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.VectorSprite;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.Line;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.visualizers.ContourVisualizer;
import dwlab.shapes.sprites.Sprite;

public class SpringExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new SpringExample() ).act();
	}
	
	
	double gravity = 10.0;

	public Layer Layer = new Layer();
	public Sprite rectangle = new Sprite( 0, 0, 30, 20 );

	
	@Override
	public void init() {
		VectorSprite pivots[] = new VectorSprite[ 4 ];
		for( int y = 0; y <= 1; y++ ) {
			for( int x = 0; x <= 1; x++ ) {
				pivots[ x +.getY() * 2 ] = new VectorSprite( x * 2 - 1, y * 2 - 1, 0.3, 0.3 );
				Layer.addLast( pivots[ x +.getY() * 2 ] );
			}
		}
		for( int n = 0; n <= 2; n++ ) {
			for( int m = n + 1; m <= 3; m++ ) {
				Spring spring = Spring.fromPivotsAndResistances( pivots[ n ], pivots[ m ] );
				spring.visualizer = new ContourVisualizer( 0.3, "FF0000" );
				Layer.addLast( spring );
			}
		}

		pivots[ 0 ].dX = 5.0;

		rectangle.visualizer = new ContourVisualizer( 0.1, "FF0000" );
	}

	
	@Override
	public void logic() {
		for( VectorSprite pivot : Layer ) {
			pivot.dY += perSecond( gravity );
			pivot.moveForward();
			pivot.bounceInside( rectangle );
		}
		Layer.act();
	}

	
	@Override
	public void render() {
		rectangle.draw();
		Layer.draw();
	}
	
	
	public static class Spring extends LineSegment {
		public double movingResistance[] = new double[ 2 ];
		public double distance;

		
		public Spring( Sprite pivot1, Sprite pivot2 ) {
			pivot[ 0 ] = pivot1;
			pivot[ 1 ] = pivot2;
			movingResistance[ 0 ] = pivot1movingResistance;
			movingResistance[ 1 ] = pivot2movingResistance;
			distance = pivot1.distanceTo( pivot2 );
		}
		

		@Override
		public void act() {
			VectorSprite pivot0 = VectorSprite( pivot[ 0 ] );
			VectorSprite pivot1 = VectorSprite( pivot[ 1 ] );
			double k = 1.0 - distance / pivot0.distanceTo( pivot1 );
			pivot0.dX += deltaTime * 20.0 * ( pivot1.getX() - pivot0.getX() ) * k;
			pivot0..y += deltaTime * 20.0 * ( pivot1.getY() - pivot0.getY() ) * k;
			pivot1.dX -= deltaTime * 20.0 * ( pivot1.getX() - pivot0.getX() ) * k;
			pivot1..y -= deltaTime * 20.0 * ( pivot1.getY() - pivot0.getY() ) * k;
		}
	}
}


