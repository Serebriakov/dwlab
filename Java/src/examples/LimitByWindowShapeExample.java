package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.visualizers.ContourVisualizer;
import dwlab.shapes.sprites.Sprite;

public class LimitByWindowShapeExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new LimitByWindowShapeExample() ).act();
	}
	
	
	public Sprite ball1 = Sprite.fromShape( 0, 0, 3, 3, Sprite.oval );
	public Sprite ball2 = Sprite.fromShape( 0, 0, 2, 2, Sprite.oval );
	public Sprite ball3 = Sprite.fromShape( 0, 0, 3, 3, Sprite.oval );

	public Sprite rectangle1 = Sprite.fromShape( 10, 0, 10, 10 );
	public Sprite rectangle2 = Sprite.fromShape( -10, 5, 6, 9 );
	public Sprite rectangle3 = Sprite.fromShape( -10, -5, 6, 9 );
	public Sprite rectangle4 = Sprite.fromShape( -3, 0, 6, 8 );

	public Sprite rectangleArray[] = [ rectangle2, rectangle3, rectangle4 ];

	public void init() {
		initGraphics();
		ball1.visualizer.Graphics.setColorFromHex( "FF0000" );
		ball2.visualizer.Graphics.setColorFromHex( "FFFF00" );
		ball3.visualizer.Graphics.setColorFromHex( "0000FF" );
		ball1.limitByWindowShape( rectangle1 );
		ball3.limitByWindowShapes( rectangleArray );
		rectangle1.visualizer = ContourVisualizer.fromWidthAndHexColor( 0.1, "00FF00" );
		rectangle2.visualizer = ContourVisualizer.fromWidthAndHexColor( 0.1, "00FF00" );
		rectangle3.visualizer = ContourVisualizer.fromWidthAndHexColor( 0.1, "00FF00" );
		rectangle4.visualizer = ContourVisualizer.fromWidthAndHexColor( 0.1, "00FF00" );
	}

	public void logic() {
		ball1.setMouseCoords();
		ball2.setMouseCoords();
		ball3.setMouseCoords();
		if( appTerminate() || keyHit( kEY_ESCAPE ) ) exiting = true;
	}

	public void render() {
		rectangle1.draw();
		rectangle2.draw();
		rectangle3.draw();
		rectangle4.draw();

		ball1.draw();
		ball3.draw();
		ball2.draw();

		printText( "Move cursor to see how balls are limited by rectangles" );
		printText( "LimitByWindowShape, LimitByWindowShapes example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
