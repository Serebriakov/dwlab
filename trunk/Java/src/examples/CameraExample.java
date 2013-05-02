package examples;
import dwlab.base.Graphics;
import dwlab.base.Image;
import dwlab.base.Project;
import dwlab.base.Service;
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
	
	private static CameraExample instance = new CameraExample();
	
	public static void main(String[] argv) {
		instance.act();
	}
	
	
	public final int tileMapWidth = 64;
	public final int tileMapHeight = 64;

	public TileMap tileMap = TileMap.create( new TileSet( new Image( "res/tiles.png", 8, 4 ) ), tileMapWidth, tileMapHeight );
	public Sprite player = new Sprite( ShapeType.OVAL, 0, 0, 0.5, 0.5 );
	public double z, baseK;

	private static final ButtonAction ZoomIn = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_A ), "zoom in" );
	private static final ButtonAction ZoomOut = ButtonAction.create( KeyboardKey.create( Keyboard.KEY_Z ), "zoom out" );
	
	
	@Override
	public void init() {
		initGraphics();
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

		if( ZoomIn.isDown() ) z += perSecond( 8.0 );
		if( ZoomOut.isDown() ) z -= perSecond( 8.0 );
		Camera.current.alterCameraZoom( z, baseK, 8.0 );
	}
	

	@Override
	public void render() {
		tileMap.draw();
		player.draw();
		Graphics.drawText( "Shift cursor by arrow keys and alter magnigication by A and Z keys.", 0, 0 );
		String message = "LTCamera, AlterCameraMagnification, ShiftCameraToShape example";
		Graphics.drawText( message, 400 - 4 * message.length(), 584 );
	}
}
