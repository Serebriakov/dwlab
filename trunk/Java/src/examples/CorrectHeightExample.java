package examples;
import java.lang.Math;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;
import org.lwjgl.input.Keyboard;

public class CorrectHeightExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new CorrectHeightExample() ).act();
	}
	

	final int spritesQuantity = 50;

	Layer layer = new Layer();
	
	ButtonAction fix = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_SPACE ), "clone" );

	
	@Override
	public void init() {
		Visualizer spriteVisualizer = new Visualizer( "res/mario.png", 4, 1 );
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( Service.random( -15d, 15d ), Service.random( -11d, 11d ), Service.random( 0.5d, 2d ), Service.random( 0.5d, 2d ) );
			sprite.visualizer = spriteVisualizer;
			layer.addLast( sprite );
		}
	}

	
	@Override
	public void logic() {
		if( fix.wasPressed() ) {
			for( Shape shape : layer.children ) shape.toSprite().correctHeight();
		}
	}
	

	@Override
	public void render() {
		layer.draw();
		printText( "Press space to correct height" );
		printText( "CorrectHeight example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
