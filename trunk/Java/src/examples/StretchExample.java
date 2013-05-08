package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.shapes.maps.TileMap;
import dwlab.base.Image;
import dwlab.base.Project;
import dwlab.shapes.maps.TileSet;


public class EnframeExample extends Project {
	public final int tileMapWidth = 4;
	public final int tileMapHeight = 3;
	
	public static void main(String[] argv) {
		Graphics.init();
		
		TileSet tileSet = TileSet.create( Image.fromFile( " incbintiles .png", 8, 4 ) );
		TileMap tileMap = TileMap.create( tileSet, tileMapWidth, tileMapHeight );

		initGraphics();
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y <= tileMapHeight; y++ ) {
			for( int x = 0; x <= tileMapWidth; x++ ) {
				tileMap.setTile( x, y, rand( 1, 31 ) );
			}
		}

		for( int n = 1; n <= 3; n++ ) {
			cls;
			tileMap.draw();
			printText( "Press any key to stretch tilemap by 2 times" );
			printText( "Stretch example", Align.TO_CENTER, Align.TO_BOTTOM );
			Graphics.switchBuffers();
			waitKey;
			tileMap.stretch( 2, 2 );
			tileMap.alterSize( 2, 2 );
		}
	}
}