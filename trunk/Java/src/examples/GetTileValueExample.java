package examples;

import dwlab.platform.Platform;

import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.IntVector;
import dwlab.base.service.Service;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;
import dwlab.visualizers.Visualizer;

public class GetTileValueExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new GetTileValueExample() ).act();
	}
	
	
	int tileMapWidth = 16;
	int tileMapHeight = 12;

	public TileMap tileMap = TileMap.create( new TileSet( new Image( "res/tiles.png", 8, 4 ) ), tileMapWidth, tileMapHeight );

	
	@Override
	public void init() {
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		tileMap.visualizer = new Visualizer(){
			double radius = 4;

			
			@Override
			public int getTileValue( TileMap tileMap, int tileX, int tileY ) {
				IntVector vector = new IntVector();
				tileMap.getTileForPoint( cursor.getX(), cursor.getY(), vector );
				if( Service.distance( tileX - vector.x, tileY - vector.y ) <= radius ) return 18; else return 26;
			}
		};
	}

	
	@Override
	public void render() {
		tileMap.draw();
		printText( "GetTileValue example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
