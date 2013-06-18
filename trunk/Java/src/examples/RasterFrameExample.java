package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Drag;
import dwlab.base.images.RasterFrame;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import java.lang.Math;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;

public class RasterFrameExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new RasterFrameExample() ).act();
	}
	

	ButtonAction set = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	
	public Sprite frame;
	public RasterFrame frameImage = new RasterFrame( "res/border.png", 8, 8, 8, 8 );
	public Layer Layer = new Layer();
	public Drag createFrame = new Drag(){
		double startingX, startingY;

		
		@Override
		public boolean dragKey() {
			return set.isDown();
		}
		

		@Override
		public void startDragging() {
			startingX = cursor.getX();
			startingY = cursor.getY();
			frame = new Sprite( cursor.getX(), cursor.getY(), 0, 0 );
			frame.visualizer.setRandomColor();
			frame.visualizer.image = frameImage;
		}
		

		@Override
		public void dragging() {
			double cornerX, cornerY;
			if( startingX < cursor.getX() ) cornerX = startingX; else cornerX = cursor.getX();
			if( startingY < cursor.getY() ) cornerY = startingY; else cornerY = cursor.getY();
			frame.setSize( Math.abs( startingX - cursor.getX() ), Math.abs( startingY - cursor.getY() ) );
			frame.setCornerCoords( cornerX, cornerY );
		}
		

		@Override
		public void endDragging() {
			Layer.addLast( frame );
			frame = null;
		}
	};

	
	@Override
	public void logic() {
		createFrame.act();
	}

	
	@Override
	public void render() {
		Layer.draw();
		if( frame != null ) frame.draw();
		printText( "Drag left mouse button to create frames" );
		printText( "LTRasterFrame, LTDrag example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}