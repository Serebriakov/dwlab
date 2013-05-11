package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Sys;
import dwlab.shapes.maps.TileMap;
import dwlab.shapes.Layers.EditorData;
import dwlab.shapes.Layers.World;
import dwlab.shapes.maps.DoubleMap;
import dwlab.shapes.maps.TileSet;

public class EnframeExample extends Project {
	static int mapSize = 64;
	static double mapScale = 8;
	static int filledTileNum = 20;
	
	public static void main(String[] argv) {
		Graphics.init();
		
		editorData = new EditorData();
		Graphics.setClearingColor( 64, 128, 0 );

		Graphics.clearScreen();
		DoubleMap doubleMap = new DoubleMap();
		doubleMap.setResolution( mapSize, mapSize );
		drawDoubleMap( doubleMap );
		printText( "Step creating 1 Double map and set its resolution" );
		Graphics.swapBuffers();
		Sys.waitForKey();

		Graphics.clearScreen();
		doubleMap.perlinNoise( 16, 16, 0.25, 0.5, 4 );
		drawDoubleMap( doubleMap );
		printText( "Step filling 2 DoubleMap with Perlin noise" );
		Graphics.swapBuffers();
		Sys.waitForKey();

		Graphics.clearScreen();
		World world = World.fromFile( "res/tileset.lw" );
		TileSet tileSet = TileSet( editorData.tilesets.getFirst() );
		TileMap tileMap = TileMap.create( tileSet, mapSize, mapSize );
		tileMap.setSize( mapSize * mapScale / 25.0, mapSize * mapScale / 25.0 );
		Project.printText( "Step loading 3 world, extract tileset from there and" );
		Project.printText( "creating tilemap with same size and this tileset", 1 );
		drawDoubleMap( doubleMap );
		Graphics.swapBuffers();
		Sys.waitForKey();


		Graphics.clearScreen();
		doubleMap.extractTo( tileMap, 0.5, 1.0, filledTileNum );
		Project.printText( "Step setting 4 tiles number of tilemap to FilledTileNum" );
		Project.printText( "if corresponding value of Double map is higher than 0.5", 1 );
		tileMap.draw();
		drawSignature();
		Graphics.swapBuffers();
		Sys.waitForKey();

		Graphics.clearScreen();
		for( int y = 0; y < mapSize; y++ ) {
			for( int x = 0; x < mapSize; x++ ) {
				fix( tileMap, x, y );
			}
		}
		printText( "Step preparing 5 tilemap .y fixing some unmanaged cell positions" );
		tileMap.draw();
		drawSignature();
		Graphics.swapBuffers();
		Sys.waitForKey();

		Graphics.clearScreen();
		tileMap.enframe();
		printText( "Step enframing 6a tile map" );
		tileMap.draw();
		drawSignature();
		Graphics.swapBuffers();
		Sys.waitForKey();


		Graphics.clearScreen();
		prolongTiles = false;
		tileMap.enframe() ;
		printText( "Step enframing 6b tile map with prolonging tiles off" );
		tileMap.draw();
		drawSignature();
		Graphics.swapBuffers();
		Sys.waitForKey();


		Graphics.setClearingColor( 0, 0, 0 );
	}

	
	public static void drawDoubleMap( DoubleMap map ) {
		tImage image = createImage( mapSize, mapSize );
		tPixmap pixmap = lockimage( image );
		clearPixels( pixmap, $fF000000 );
		map.pasteToPixmap( pixmap );
		unlockimage( image );
		setScale( mapScale, mapScale );
		drawImage( image, 400 - 0.5 * mapScale * mapSize, 300 - 0.5 * mapScale * mapSize );
		setScale( 1, 1 );
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