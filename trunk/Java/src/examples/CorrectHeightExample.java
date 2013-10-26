package examples;

import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.platform.LWJGL;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Visualizer;

public class CorrectHeightExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new CorrectHeightExample() ).act();
	}
	

	final int spritesQuantity = 50;

	Layer Layer = new Layer();
	
	ButtonAction fix = ButtonAction.create( KeyboardKey.create( Key.SPACE ), "clone" );

	
	@Override
	public void init() {
		Visualizer spriteVisualizer = new Visualizer( "res/mario.png", 4, 1 );
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( Service.random( -15d, 15d ), Service.random( -11d, 11d ), Service.random( 0.5d, 2d ), Service.random( 0.5d, 2d ) );
			sprite.visualizer = spriteVisualizer;
			Layer.addLast( sprite );
		}
	}

	
	@Override
	public void logic() {
		if( fix.wasPressed() ) {
			for( Shape shape : Layer.children ) shape.toSprite().correctHeight();
		}
	}
	

	@Override
	public void render() {
		Layer.draw();
		printText( "Press space to correct height" );
		printText( "CorrectHeight example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
