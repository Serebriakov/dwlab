package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.shapes.sprites.SpriteCollisionHandler;
import dwlab.visualizers.MarchingAnts;

public class MarchingAntsExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new MarchingAntsExample() ).act();
	}
	
	
	int spritesQuantity = 50;

	public Layer Layer = new Layer();
	public Image spriteImage = new Image( "res/kolobok.png" );
	public Sprite selected;
	public MarchingAnts marchingAnts = new MarchingAnts();

	ButtonAction select = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );

	
	@Override
	public void init() {
		cursor = new Sprite(){
			SpriteCollisionHandler handler = new SpriteCollisionHandler() {
				@Override
				public void handleCollision( Sprite sprite1, Sprite sprite2 ) {
					selected = sprite2;
				}
			};

			@Override
			public void act() {
				setMouseCoords();
				if( select.wasPressed() ) {
					selected = null;
					collisionsWith( Layer, handler );
				}
			}
		};
		
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( ShapeType.oval, Service.random( -15, 15 ), Service.random( -11, 11 ), 0d, 0d, 1d, Service.random( 360 ) );
			sprite.setDiameter( Service.random( 1, 3 ) );
			sprite.visualizer.setRandomColor();
			sprite.visualizer.image = spriteImage;
			Layer.addLast( sprite );
		}

		cursor.setDiameter( 0.5 );
	}
	

	@Override
	public void logic() {
		cursor.act();
	}
	

	@Override
	public void render() {
		Layer.draw();
		if( selected != null ) selected.drawUsingVisualizer( marchingAnts );
		printText( "Select Sprite .y left-clicking on it" );
		printText( "LTMarchingAnts example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}