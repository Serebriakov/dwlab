package examples;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import org.lwjgl.input.Keyboard;

public class CameraExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new CameraExample() ).act();
	}
	
	
	int tileMapWidth = 64;
	int tileMapHeight = 64;

	TileMap tileMap = TileMap.create( new TileSet( new Image( "res/tiles.png", 8, 4 ) ), tileMapWidth, tileMapHeight );
	Sprite pLayer = new Sprite( ShapeType.oval, 0, 0, 0.5, 0.5 );
	double z, baseK;

	ButtonAction zoomIn = ButtonAction.create( KeyboardKey.create( Key.A ), "zoom in" );
	ButtonAction zoomOut = ButtonAction.create( KeyboardKey.create( Key.Z ), "zoom out" );
	
	
	@Override
	public void init() {
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y < tileMapHeight; y++ ) {
			for( int x = 0; x < tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}
		pLayer.visualizer.set( "FFBF7F" );
		baseK = Camera.current.k;
	}
	

	@Override
	public void logic() {
		pLayer.moveUsingArrows( 10.0 );
		Camera.current.shiftCameraTo( pLayer, 10.0 );

		if( zoomIn.isDown() ) z += perSecond( 8.0 );
		if( zoomOut.isDown() ) z -= perSecond( 8.0 );
		Camera.current.alterCameraZoom( z, baseK, 8.0 );
	}
	

	@Override
	public void render() {
		tileMap.draw();
		pLayer.draw();
		printText( "Shift cursor .y arrow Keys and alter magnigication .y A and Z Keys." );
		printText( "LTCamera, AlterCameraMagnification, ShiftCameraToShape example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
