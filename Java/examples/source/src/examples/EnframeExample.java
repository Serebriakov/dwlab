package examples;

import static dwlab.platform.Functions.*;

import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.platform.LWJGL;
import dwlab.shapes.layers.World;
import dwlab.shapes.maps.DoubleMap;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;

public class EnframeExample extends Project {
	static int mapSize = 64;
	static int filledTileNum = 20;
	
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		Platform.setClearingColor( 0.25d, 0.5d, 0d );

		clearScreen();
		DoubleMap doubleMap = new DoubleMap( mapSize, mapSize );
		drawDoubleMap( doubleMap );
		printText( "Step 1: creating Double map and set its resolution" );
		swapBuffers();
		waitForKey();

		clearScreen();
		doubleMap.perlinNoise( 16, 16, 0.25, 0.5, 4 );
		drawDoubleMap( doubleMap );
		printText( "Step 2: filling DoubleMap with Perlin noise" );
		swapBuffers();
		waitForKey();

		clearScreen();
		World.fromFile( "res/tileset.lw" );
		TileSet tileSet = World.editorData.tilesets.getFirst();
		TileMap tileMap = TileMap.create( tileSet, mapSize, mapSize );
		tileMap.setSize( 400d / 25d, 400d / 25d );
		Project.printText( "Step 3: loading world, extract tileset from there and" );
		Project.printText( "creating tilemap with same size and this tileset", 1 );
		drawDoubleMap( doubleMap );
		swapBuffers();
		waitForKey();

		clearScreen();
		doubleMap.extractTo( tileMap, 0.5d, 1d, filledTileNum );
		Project.printText( "Step 4: setting tiles number of tilemap to FilledTileNum" );
		Project.printText( "if corresponding value of Double map is higher than 0.5", 1 );
		tileMap.draw();
		drawSignature();
		swapBuffers();
		waitForKey();

		clearScreen();
		for( int y = 0; y < mapSize; y++ ) {
			for( int x = 0; x < mapSize; x++ ) {
				fix( tileMap, x, y );
			}
		}
		printText( "Step 5: preparing tilemap by fixing some unmanaged cell positions" );
		tileMap.draw();
		drawSignature();
		swapBuffers();
		waitForKey();

		clearScreen();
		tileMap.enframe();
		printText( "Step 6a: enframing tile map" );
		tileMap.draw();
		drawSignature();
		swapBuffers();
		waitForKey();

		clearScreen();
		TileSet.prolongTiles = false;
		tileMap.enframe() ;
		printText( "Step 6b: enframing tile map with prolonging tiles off" );
		tileMap.draw();
		drawSignature();
		swapBuffers();
		waitForKey();


		Platform.setClearingColor( 0, 0, 0 );
	}

	
	public static void drawDoubleMap( DoubleMap map ) {
		Image image = map.toNewImage();
		image.draw( 0, 400, 300, 400, 400 );
		drawSignature();
	}


	public static void drawSignature() {
		printText( "PerlinNoise, ExtractTo, Enframe, L_ProlongTiles, example", Align.TO_CENTER, Align.TO_BOTTOM );
	}



	public static void fix( TileMap tileMap, int x, int y ) {
		if( tileMap.getTile( x, y ) == filledTileNum ) return;
		boolean doFix = false;

		boolean fixHorizontal = true;
		if( x > 0 && x < mapSize - 1 ) {
			if( tileMap.getTile( x - 1, y ) == filledTileNum && tileMap.getTile( x + 1, y ) == filledTileNum ) doFix = true;
		} else {
			fixHorizontal = false;
		}

		boolean fixVertical = true;
		if( y > 0 && y < mapSize - 1 ) {
			if( tileMap.getTile( x, y - 1 ) == filledTileNum && tileMap.getTile( x, y + 1 ) == filledTileNum ) doFix = true;
		} else {
			fixVertical = false;
		}

		if( doFix ) {
			tileMap.setTile( x, y, filledTileNum );
			if( fixHorizontal ) {
				fix( tileMap, x - 1, y );
				fix( tileMap, x + 1, y );
			}
			if( fixVertical ) {
				fix( tileMap, x, y - 1 );
				fix( tileMap, x, y + 1 );
			}
		}
	}
}