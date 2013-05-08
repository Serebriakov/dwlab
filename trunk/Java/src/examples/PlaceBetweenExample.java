package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.LineSegment;
import dwlab.visualizers.ContourVisualizer;
import dwlab.shapes.sprites.Sprite;

public class PlaceBetweenExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new PlaceBetweenExample() ).act();
	}
	
	
	public Sprite pivot1 = Sprite.fromShape( 0, 0, 0, 0, Sprite.pivot );
	public Sprite pivot2 = Sprite.fromShape( 0, 0, 0, 0, Sprite.pivot );
	public Sprite oval1 = Sprite.fromShape( 0, 0, 0.75, 0.75, Sprite.oval );
	public Sprite oval2 = Sprite.fromShape( 0, 0, 0.75, 0.75, Sprite.oval );
	public LineSegment lineSegment = LineSegment.fromPivots( pivot1, pivot2 );

	public void init() {
		initGraphics();
		lineSegment.visualizer = ContourVisualizer.fromWidthAndHexColor( 0.2, "0000FF", 1.0, 2.0 );
		oval1.visualizer.Graphics.setColorFromHex( "FF0000" );
		oval2.visualizer.Graphics.setColorFromHex( "00FF00" );
	}

	public void logic() {
		pivot2.setMouseCoords();
		oval1.placeBetween( pivot1, pivot2, 0.5 );
		oval2.placeBetween( pivot1, pivot2, 0.3 );
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		lineSegment.draw();
		oval1.draw();
		oval2.draw();
		printText( "PlaceBetween example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
