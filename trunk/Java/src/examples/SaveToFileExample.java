package examples;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class SaveToFileExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new SaveToFileExample() ).act();
	}
	
	
	int spritesQuantity = 70;

	Layer layer = new Layer();
	double ang;
	Sprite oldSprite;

	ButtonAction save = ButtonAction.create( KeyboardKey.create( Key.F2 ) );
	ButtonAction load = ButtonAction.create( KeyboardKey.create( Key.F3 ) );


	@Override
	public void init() {
		for( int n = 1; n <= spritesQuantity; n++ ) {
			oldSprite = new Sprite( ShapeType.oval, Service.random( -15, 15 ), Service.random( -11, 11 ), 0d, 0d, Service.random( 360 ), 5 );
			oldSprite.setDiameter( Service.random( 0.5, 1.5 ) );
			oldSprite.visualizer.setRandomColor();
			layer.addLast( oldSprite );
		}
	}
	

	@Override
	public void logic() {
		ang = 1500 * Math.sin( 7 * time );
		for( Shape shape : layer.children ) {
			Sprite sprite = shape.toSprite();
			oldSprite.directTo( sprite );
			oldSprite.angle += perSecond( ang ) + Service.random( -45, 45 );
			sprite.moveForward();
			oldSprite = sprite;
		}

		if( save.wasPressed() ) layer.saveToFile( "sprites.lw" );
		if( load.wasPressed() ) layer = (Layer) Obj.loadFromFile( "sprites.lw" );
	}
	

	@Override
	public void render() {
		layer.draw();
		printText( "Press F2 to save and F3 to load position of sprites" );
		printText( "LoadFromFile, SaveToFile example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
