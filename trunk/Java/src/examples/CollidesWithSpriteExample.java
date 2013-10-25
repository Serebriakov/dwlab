package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.images.Image;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class CollidesWithSpriteExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new CollidesWithSpriteExample() ).act();
	}
	

	Layer sprites = new Layer();
	Image image = new Image( "res/spaceship.png" );
	
	ButtonAction change = ButtonAction.create( MouseButton.create( MouseButton.RIGHT_BUTTON ), "clone" );
	
	int shapeTypeNum = 3;

	
	@Override
	public void init() {
		for( int n = 0; n < 9; n++ ) {
			ShapeType shapeType = ShapeType.shapeTypes.get( n );
			Sprite sprite = new Sprite( shapeType, ( n % 3 ) * 8d - 8d, Math.floor( n / 3 ) * 6d - 6d, 6d, 4d );
			if( shapeType == ShapeType.raster ) sprite.visualizer.image = image;
			sprite.visualizer.set( "7FFF7F" );
			sprite.angle = 60;
			sprites.addLast( sprite );
		}
		cursor.setSize( 5d, 7d );
		cursor.angle = 45;
		cursor.visualizer.set( "7FFFFF7F" );
		cursor.shapeType = ShapeType.ray;
	}
	

	@Override
	public void logic() {
		if( change.wasPressed() ) {
			shapeTypeNum = ( shapeTypeNum + 1 ) % 9;
			cursor.shapeType = ShapeType.shapeTypes.get( shapeTypeNum );
			if( cursor.shapeType == ShapeType.raster ) cursor.visualizer.image = image; else cursor.visualizer.image = null;
		}
	}
	

	@Override
	public void render() {
		sprites.draw();
		for( Shape shape : sprites.children ) {
			Sprite sprite = shape.toSprite();
			if( cursor.collidesWith( sprite ) ) {
				sprite.visualizer.set( "FF7F7F" );
				Sprite wedgedCursor = cursor.clone();
				wedgedCursor.wedgeOffWith( sprite, 0, 1 );
				wedgedCursor.visualizer.set( "7F7FFF7F" );
				wedgedCursor.draw();
			} else {
				sprite.visualizer.set( "7FFF7F" );
			}
		}
		cursor.draw();

		printText( "Press right mouse button to change shape ", Align.TO_CENTER, Align.TO_TOP );
		printText( "ColldesWithSprite example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
