package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;

public class MoveUsingKeysExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new MoveUsingKeysExample() ).act();
	}
	
	
	public Sprite ball1 = Sprite.fromShape( -8, 0, 1, 1, Sprite.oval, 0, 7 );
	public Sprite ball2 = Sprite.fromShape( 0, 0, 2, 2, Sprite.oval, 0, 3 );
	public Sprite ball3 = Sprite.fromShape( 8, 0, 1.5, 1.5, Sprite.oval, 0, 5 );

	public void init() {
		ball1.visualizer = Visualizer.fromHexColor( "FF0000" );
		ball2.visualizer = Visualizer.fromHexColor( "00FF00" );
		ball3.visualizer = Visualizer.fromHexColor( "0000FF" );
		initGraphics();
	}

	public void logic() {
		ball1.moveUsingWSAD( ball1.velocity );
		ball2.moveUsingArrows( ball2.velocity );
		ball3.moveUsingKeys( key_I, key_K, key_J, key_L, ball3.velocity );
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		printText( "Move red ball using WSAD keys" );
		drawText( "Move green ball using arrow keys", 0, 16 );
		drawText( "Move blue ball using IJKL keys", 0, 32 );
		ball1.draw();
		ball2.draw();
		ball3.draw();
		printText( "MoveUsingKeys example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
