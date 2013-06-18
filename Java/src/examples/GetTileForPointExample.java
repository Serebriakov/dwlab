package examples;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.service.IntVector;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.controllers.MouseButton;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.maps.tilemaps.TileSet;

public class GetTileForPointExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new GetTileForPointExample() ).act();
	}
	
	
	int tileMapWidth = 16;
	int tileMapHeight = 12;

	TileSet tileSet = new TileSet( new Image( "res/tiles.png", 8, 4 ) );
	TileMap tileMap = TileMap.create( tileSet, tileMapWidth, tileMapHeight );

	ButtonAction paint = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	ButtonAction changeBrush = ButtonAction.create( MouseButton.create( MouseButton.RIGHT_BUTTON ) );
	
	@Override
	public void init() {
		cursor = new Sprite( 0, 0, 2, 2 );
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y < tileMapHeight; y++ ) {
			for( int x = 0; x < tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}
		cursor.visualizer.image = tileMap.tileSet.image;
		cursor.frame = 1;
	}
	

	@Override
	public void logic() {
		cursor.setMouseCoords();
		IntVector tile = new IntVector();
		tileMap.getTileForPoint( cursor.getX(), cursor.getY(), tile );
		int tileX = (int) tile.x;
		int tileY = (int) tile.y;
		if( tileX >= 0 && tileY >= 0 && tileX < tileMap.xQuantity && tileY < tileMap.yQuantity ) {
			if( paint.isDown() ) tileMap.setTile( tileX, tileY, cursor.frame );
			if( changeBrush.wasPressed() ) cursor.setAsTile( tileMap, tileX, tileY );
		}
	}
	

	@Override
	public void render() {
		tileMap.draw();
		cursor.draw();
		printText( "Press right mouse button to select brush, left button to draw." );
		printText( "GetTileForPoint, SetTile, SetAsTile example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
