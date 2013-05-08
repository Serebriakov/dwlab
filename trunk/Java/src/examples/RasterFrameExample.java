package examples;
import dwlab.base.*;
import java.lang.Math;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;

public class RasterFrameExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new RasterFrameExample() ).act();
	}
	
	
	public Sprite frame;
	public RasterFrame frameImage = RasterFrame.fromFileAndBorders( " incbinborder .png", 8, 8, 8, 8 );
	public Layer layer = new Layer();
	public CreateFrame createFrame = new CreateFrame();

	public void init() {
		initGraphics();
	}

	public void logic() {
		createFrame.execute();
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		layer.draw();
		if( frame ) frame.draw();
		printText( "Drag left mouse button to create frames" );
		printText( "LTRasterFrame, LTDrag example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}



public class CreateFrame extends Drag {
	public double startingX, double startingY;

	public int dragKey() {
		return mouseDown( 1 );
	}

	public void startDragging() {
		startingX = cursor.x;
		startingY = cursor.y;
		example.frame = Sprite.fromShape( cursor.x, cursor.y, 0, 0 );
		example.frame.visualizer.setRandomColor();
		example.frame.visualizer.image = example.frameImage;
	}

	public void dragging() {
		double cornerX, double cornerY;
		if( startingX < cursor.x ) cornerX = startingX; else cornerX = cursor.x;
		if( startingY < cursor.y ) cornerY = startingY; else cornerY = cursor.y;
		example.frame.setSize( Math.abs( startingX - cursor.x ), Math.abs( startingY - cursor.y ) );
		example.frame.setCornerCoords( cornerX, cornerY );
	}

	public void endDragging() {
		example.layer.addLast( example.frame );
		example.frame = null;
	}
}
