package examples;
import java.lang.Math;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.LineSegment;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;

public class DrawUsingLineExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DrawUsingLineExample() ).act();
	}
	
	
	public Layer lineSegments = new Layer();

	public void init() {
		initGraphics();
		currentCamera.setMagnification( 75.0 );
		Blazing visualizer = new Blazing();
		for local int pivots[] = eachin [ [ -4, -2, -2, -2 ], [ -4, -2, -4, 0 ], [ -4, 0, -4, 2 ], [ -4, 0, -3, 0 ], [ 1, -2, -1, -2 ], [ -1, -2, -1, 0 ], [ -1, 0, 1, 0 ], 
				[ 1, 0, 1, 2 ], [ 1, 2, -1, 2 ], [ 4, -2, 2, -2 ], [ 2, -2, 2, 0 ], [ 2, 0, 2, 2 ], [ 2, 0, 3, 0 ] ];
			LineSegment lineSegment = LineSegment.fromPivots( Sprite.fromShape( pivots[ 0 ], pivots[ 1 ] ), Sprite.fromShape( pivots[ 2 ], pivots[ 3 ] ) );
			lineSegment.visualizer = visualizer;
			lineSegments.addLast( lineSegment );
		}
	}

	public void logic() {
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		lineSegments.draw();
		printText( "Free Software Forever!" );
		printText( "DrawUsingLine example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}



public class Blazing extends Visualizer {
	public final double chunkSize = 25;
	public final double deformationRadius = 15;
	public void drawUsingLineSegment( LineSegment lineSegment ) {
		sX1d, sY1d, sX2d, sY2d;
		currentCamera.fieldToScreen( lineSegment.pivot[ 0 ].x, lineSegment.pivot[ 0 ].y, sX1, sY1 );
		currentCamera.fieldToScreen( lineSegment.pivot[ 1 ].x, lineSegment.pivot[ 1 ].y, sX2, sY2 );
		int chunkQuantity = Math.max( 1, round( 1.0 * distance( sX2 - sX1, sY2 - sY1 ) / chunkSize ) );
		double oldX, double oldY;
		for( int n = 0; n <= chunkQuantity; n++ ) {
			double radius = 0;
			if( n > 0 && n < chunkQuantity ) radius = Service.random( 0.0, deformationRadius );

			double angle = Service.random( 0.0, 360.0 );
			int x = sX1 + ( sX2 - sX1 ) * n / chunkQuantity + Math.cos( angle ) * radius;
			int y = sY1 + ( sY2 - sY1 ) * n / chunkQuantity + Math.sin( angle ) * radius;

			setLineWidth( 9 );
			Graphics.setColor( 0, 255, 255 );
			Graphics.drawOval( x - 4, y - 4, 9, 9 );
			if( n > 0 ) {
				Graphics.drawOval( oldX - 4, oldY - 4, 9, 9 );
				drawLine( x, y, oldX, oldY );
			}
			setLineWidth( 4 );
			Graphics.setColor( 255, 255, 255 );
			Graphics.drawOval( x - 2, y - 2, 5, 5 );
			if( n > 0 ) {
				Graphics.drawOval( oldX - 2, oldY - 2, 5, 5 );
				drawLine( x, y, oldX, oldY );
			}

			oldX = x;
			oldY = y;
		}
	}
}
