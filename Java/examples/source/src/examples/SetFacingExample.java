package examples;

import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.platform.LWJGL;
import dwlab.shapes.Shape.Facing;
import dwlab.shapes.sprites.Sprite;

public class SetFacingExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new SetFacingExample() ).act();
	}
	
	
	Sprite sprite = new Sprite( 0, 0, 8, 8 );

	ButtonAction setLeftFacing = ButtonAction.create( KeyboardKey.create( Key.LEFT ) );
	ButtonAction setRightFacing = ButtonAction.create( KeyboardKey.create( Key.RIGHT ) );

	
	@Override
	public void init() {
		sprite.visualizer.image = new Image( "res/kolobok.png" );
	}

	
	@Override
	public void logic() {
		if( setLeftFacing.wasPressed() ) sprite.setFacing( Facing.LEFT );
		if( setRightFacing.wasPressed() ) sprite.setFacing( Facing.RIGHT );
	}

	
	@Override
	public void render() {
		sprite.draw();
		printText( "Press left and right arrows to change sprite facing" );
		printText( "SetFacing example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
