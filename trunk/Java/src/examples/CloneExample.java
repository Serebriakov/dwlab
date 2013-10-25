package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class CloneExample extends Project {
	static {
		Platform.current.init();
	}
	
	private static CloneExample instance = new CloneExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	
	int spritesQuantity = 50;

	public Layer sprites = new Layer();
	public Image spriteImage = new Image( "res/kolobok.png" );

	private static final ButtonAction clone = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ), "clone" );
	
	
	@Override
	public void init() {
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( ShapeType.oval, Service.random( -15, 15 ), Service.random( -11, 11 ), 1, 1 );
			sprite.setDiameter( Service.random( 1, 3 ) );
			sprite.displayingAngle= Service.random( 360 );
			sprite.visualizer.setRandomColor();
			sprite.visualizer.image = spriteImage;
			sprite.visualizer.setVisualizerScales( 1.3 );
			sprites.addLast( sprite );
		}
	}

	
	@Override
	public void logic() {
		if( clone.wasPressed() ) {
			Sprite sprite = cursor.lastCollidedSpriteOf( sprites );
			if( sprite != null ) {
				Sprite newSprite = sprite.clone();
				newSprite.alterCoords( Service.random( -2, 2 ), Service.random( -2, 2 ) );
				newSprite.alterDiameter( Service.random( 0.75, 1.5 ) );
				newSprite.alterAngle( Service.random( -45, 45 ) ) ;
				sprites.addLast( newSprite );
			}
		}
	}
	

	@Override
	public void render() {
		sprites.draw();
		printText( "Clone sprites with left mouse button" );
		printText( "AlterAngle, AlterCoords, AlterDiameter, Clone example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
