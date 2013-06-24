package examples;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.ContourVisualizer;

public class LimitByWindowShapeExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new LimitByWindowShapeExample() ).act();
	}
	
	
	Sprite ball1 = new Sprite( 0, 0, 3 );
	Sprite ball2 = new Sprite( 0, 0, 2 );
	Sprite ball3 = new Sprite( 0, 0, 3 );

	Sprite rectangle1 = new Sprite( 10, 0, 10, 10 );
	Sprite rectangle2 = new Sprite( -10, 5, 6, 9 );
	Sprite rectangle3 = new Sprite( -10, -5, 6, 9 );
	Sprite rectangle4 = new Sprite( -3, 0, 6, 8 );

	Sprite rectangleArray[] = { rectangle2, rectangle3, rectangle4 };

	
	@Override
	public void init() {
		ball1.visualizer.set( "FF0000" );
		ball2.visualizer.set( "FFFF00" );
		ball3.visualizer.set( "0000FF" );
		ball1.limitByWindowShape( rectangle1 );
		ball3.limitByWindowShapes( rectangleArray );
		rectangle1.visualizer = new ContourVisualizer( 0.1, "00FF00" );
		rectangle2.visualizer = new ContourVisualizer( 0.1, "00FF00" );
		rectangle3.visualizer = new ContourVisualizer( 0.1, "00FF00" );
		rectangle4.visualizer = new ContourVisualizer( 0.1, "00FF00" );
	}

	
	@Override
	public void logic() {
		ball1.setMouseCoords();
		ball2.setMouseCoords();
		ball3.setMouseCoords();
	}

	
	@Override
	public void render() {
		rectangle1.draw();
		rectangle2.draw();
		rectangle3.draw();
		rectangle4.draw();

		ball1.draw();
		ball3.draw();
		ball2.draw();

		printText( "Move cursor to see how balls are limited by rectangles" );
		printText( "limitByWindowShape, limitByWindowShapes example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
