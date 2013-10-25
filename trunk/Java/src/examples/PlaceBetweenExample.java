package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.visualizers.ContourVisualizer;
import dwlab.shapes.sprites.Sprite;

public class PlaceBetweenExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new PlaceBetweenExample() ).act();
	}
	
	
	public Sprite pivot1 = new Sprite( 0, 0 );
	public Sprite pivot2 = new Sprite( 0, 0 );
	public Sprite oval1 = new Sprite( 0, 0, 0.75 );
	public Sprite oval2 = new Sprite( 0, 0, 0.75 );
	public LineSegment lineSegment = new LineSegment( pivot1, pivot2 );

	
	@Override
	public void init() {
		lineSegment.visualizer = new ContourVisualizer( 0.2, "0000FF", 2.0, true );
		oval1.visualizer.set( "FF0000" );
		oval2.visualizer.set( "00FF00" );
	}

	
	@Override
	public void logic() {
		pivot2.setMouseCoords();
		oval1.placeBetween( pivot1, pivot2, 0.5 );
		oval2.placeBetween( pivot1, pivot2, 0.3 );
	}

	
	@Override
	public void render() {
		lineSegment.draw();
		oval1.draw();
		oval2.draw();
		printText( "PlaceBetween example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
