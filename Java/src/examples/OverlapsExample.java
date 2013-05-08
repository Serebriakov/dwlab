package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;

public class OverlapsExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new OverlapsExample() ).act();
	}
	
	
	public Sprite pivot = Sprite.fromShape( 6, 0, 0, 0, Sprite.pivot );
	public Sprite oval = Sprite.fromShape( -4, -2, 3, 5, Sprite.oval );
	public Sprite rectangle = Sprite.fromShape( 0, 5, 4, 4, Sprite.rectangle );
	public Sprite triangle = Sprite.fromShape( 4, 4, 3, 5, Sprite.topLeftTriangle );
	public Sprite cursor = Sprite.fromShape( 0, 0, 16, 16 );
	public String text;

	public void init() {
		pivot.visualizer.Graphics.setColorFromHex( "FF0000" );
		oval.visualizer.Graphics.setColorFromHex( "00FF00" );
		rectangle.visualizer.Graphics.setColorFromHex( "0000FF" );
		triangle.visualizer.Graphics.setColorFromHex( "007FFF" );
		cursor.visualizer.alpha = 0.5;
		initGraphics();
	}

	public void logic() {
		cursor.setMouseCoords();
		text = "";
		if( cursor.overlaps( pivot ) ) text = ", pivot";
		if( cursor.overlaps( oval ) ) text += ", oval";
		if( cursor.overlaps( rectangle ) ) text += ", rectangle";
		if( cursor.overlaps( triangle ) ) text += ", triangle";
		if( ! text ) text = ", nothing";
		if( mouseHit( 2 ) ) cursor.shapeType = 3 - cursor.shapeType;
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		pivot.draw();
		oval.draw();
		rectangle.draw();
		triangle.draw();
		cursor.draw();
		printText( "Press right mouse button to change cursor shape" );
		drawText( "Cursor rectangle fully overlaps " + text[ 2.. ], 0, 16 );
		printText( "Overlaps example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
