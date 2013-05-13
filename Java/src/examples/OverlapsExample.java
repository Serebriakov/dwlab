package examples;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class OverlapsExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new OverlapsExample() ).act();
	}
	
	
	public Sprite pivot = new Sprite( 6, 0 );
	public Sprite oval = new Sprite( ShapeType.oval, -4, -2, 3, 5 );
	public Sprite rectangle = new Sprite( 0, 5, 4, 4 );
	public Sprite triangle = new Sprite( ShapeType.topLeftTriangle, 4, 4, 3, 5 );
	public String text;

	ButtonAction changeShape = ButtonAction.create( MouseButton.create( MouseButton.RIGHT_BUTTON ) );

	
	@Override
	public void init() {
		cursor = new Sprite( 0, 0, 16, 16 );
		pivot.visualizer.set( "FF0000" );
		oval.visualizer.set( "00FF00" );
		rectangle.visualizer.set( "0000FF" );
		triangle.visualizer.set( "007FFF" );
		cursor.visualizer.alpha = 0.5;
	}
	

	@Override
	public void logic() {
		cursor.setMouseCoords();
		text = "";
		if( cursor.overlaps( pivot ) ) text = ", pivot";
		if( cursor.overlaps( oval ) ) text += ", oval";
		if( cursor.overlaps( rectangle ) ) text += ", rectangle";
		if( cursor.overlaps( triangle ) ) text += ", triangle";
		if( text.isEmpty() ) text = ", nothing";
		if( changeShape.wasPressed() ) {
			if( cursor.shapeType == ShapeType.rectangle ) {
				cursor.shapeType = ShapeType.oval;
			}  else {
				cursor.shapeType = ShapeType.rectangle;
			}
		}
	}
	

	@Override
	public void render() {
		pivot.draw();
		oval.draw();
		rectangle.draw();
		triangle.draw();
		cursor.draw();
		printText( "Press right mouse button to change cursor shape" );
		printText( "Cursor rectangle ful.y overlaps " + text.substring( 2 ), 1 );
		printText( "Overlaps example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
