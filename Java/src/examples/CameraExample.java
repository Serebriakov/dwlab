package examples;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.maps.TileMap;
import dwlab.shapes.maps.TileSet;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.Sprite.ShapeType;
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
	Sprite player = new Sprite( ShapeType.OVAL, 0, 0, 0.5, 0.5 );
	double z, baseK;

	ButtonAction zoomIn = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_A ), "zoom in" );
	ButtonAction zoomOut = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_Z ), "zoom out" );
	
	
	@Override
	public void init() {
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y < tileMapHeight; y++ ) {
			for( int x = 0; x < tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}
		player.visualizer.set( "FFBF7F" );
		baseK = Camera.current.k;
	}
	

	@Override
	public void logic() {
		player.moveUsingArrows( 10.0 );
		Camera.current.shiftCameraTo( player, 10.0 );

		if( zoomIn.isDown() ) z += perSecond( 8.0 );
		if( zoomOut.isDown() ) z -= perSecond( 8.0 );
		Camera.current.alterCameraZoom( z, baseK, 8.0 );
	}
	

	@Override
	public void render() {
		tileMap.draw();
		player.draw();
		printText( "Shift cursor by arrow keys and alter magnigication by A and Z keys." );
		printText( "LTCamera, AlterCameraMagnification, ShiftCameraToShape example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
