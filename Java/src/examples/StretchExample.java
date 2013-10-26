package examples;

import dwlab.platform.Platform;

import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.platform.LWJGL;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;


public class StretchExample extends Project {
	static int tileMapWidth = 4;
	static int tileMapHeight = 3;
	
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		TileSet tileSet = new TileSet( new Image( "res/tiles.png", 8, 4 ) );
		TileMap tileMap = TileMap.create( tileSet, tileMapWidth, tileMapHeight );

		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y < tileMapHeight; y++ ) {
			for( int x = 0; x < tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}

		for( int n = 1; n <= 3; n++ ) {
			Platform.current.clearScreen();
			tileMap.draw();
			printText( "Press any Key to stretch tilemap by 2 times" );
			printText( "Stretch example", Align.TO_CENTER, Align.TO_BOTTOM );
			Platform.current.swapBuffers();
			Platform.current.waitForKey();
			tileMap.stretch( 2, 2 );
			tileMap.alterSize( 2, 2 );
		}
	}
}