package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Image;
import dwlab.base.Project;
import dwlab.shapes.sprites.Sprite;

public class SetFacingExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new SetFacingExample() ).act();
	}
	
	
	public Sprite sprite = Sprite.fromShape( 0, 0, 8, 8 );

	public void init() {
		initGraphics();
		sprite.visualizer.image = Image.fromFile( " incbinkolobok .png" );
	}

	public void logic() {
		if( keyHit( key_Left ) ) sprite.setFacing( Sprite.leftFacing );
		if( keyHit( key_Right ) ) sprite.setFacing( Sprite.rightFacing );
		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		sprite.draw();
		printText( "Press left and right arrows to change sprite facing" );
		printText( "SetFacing example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
