package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.service.Vector;
import dwlab.base.service.Service;
import dwlab.base.*;
import dwlab.platform.LWJGL;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;

public class DrawUsingLineExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new DrawUsingLineExample() ).act();
		Platform.current.initCamera();
	}
	
	
	public Layer lineSegments = new Layer();

	@Override
	public void init() {
		Camera.current.setZoom( 75d );
		Visualizer visualizer = new Visualizer(){
			double chunkSize = 25;
			double deformationRadius = 15;
			
			
			@Override
			public void drawUsingLineSegment( LineSegment lineSegment, Color drawingColor ) {
				Vector vector1 = new Vector();
				Vector vector2 = new Vector();
				Camera.current.fieldToScreen( lineSegment.pivot[ 0 ].getX(), lineSegment.pivot[ 0 ].getY(), vector1 );
				Camera.current.fieldToScreen( lineSegment.pivot[ 1 ].getX(), lineSegment.pivot[ 1 ].getY(), vector2 );
				int chunkQuantity = Math.max( 1, Service.round( 1.0 * Service.distance( vector2.x - vector1.x, vector2.y - vector1.y ) / chunkSize ) );
				double oldX = 0, oldY = 0;
				for( int n = 0; n <= chunkQuantity; n++ ) {
					double radius = 0;
					if( n > 0 && n < chunkQuantity ) radius = Service.random( 0.0, deformationRadius );

					double angle = Service.random( 0.0, 360.0 );
					double x = vector1.x + ( vector2.x - vector1.x ) * n / chunkQuantity + Math.cos( angle ) * radius;
					double y = vector1.y + ( vector2.y - vector1.y ) * n / chunkQuantity + Math.sin( angle ) * radius;

					Platform.lineWidth = 9;
					Platform.setCurrentColor( 0, 1d, 1d );
					Platform.current.drawOval( x, y, 9, 9 );
					if( n > 0 ) {
						Platform.current.drawOval( oldX, oldY, 9, 9 );
						Platform.current.drawLine( x, y, oldX, oldY );
					}
					Platform.lineWidth = 4;
					Platform.setCurrentColor( 1d, 1d, 1d );
					Platform.current.drawOval( x, y, 5, 5 );
					if( n > 0 ) {
						Platform.current.drawOval( oldX, oldY, 5, 5 );
						Platform.current.drawLine( x, y, oldX, oldY );
					}

					oldX = x;
					oldY = y;
				}
			}
		};

		int allPivots[][] = { { -4, -2, -2, -2 }, { -4, -2, -4, 0 }, { -4, 0, -4, 2 }, { -4, 0, -3, 0 }, { 1, -2, -1, -2 }, { -1, -2, -1, 0 }, { -1, 0, 1, 0 }, 
				{ 1, 0, 1, 2 }, { 1, 2, -1, 2 }, { 4, -2, 2, -2 }, { 2, -2, 2, 0 }, { 2, 0, 2, 2 }, { 2, 0, 3, 0 } };
		for( int[] pivots : allPivots ) {
			LineSegment lineSegment = new LineSegment( new Sprite( pivots[ 0 ], pivots[ 1 ] ), new Sprite( pivots[ 2 ], pivots[ 3 ] ) );
			lineSegment.visualizer = visualizer;
			lineSegments.addLast( lineSegment );
		}
	}
	

	@Override
	public void render() {
		lineSegments.draw();
		printText( "Free Software Forever!" );
		printText( "DrawUsingLine example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}



