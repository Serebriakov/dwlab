package examples;

import dwlab.platform.Platform;
import dwlab.base.Project;
import dwlab.platform.Sound;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.base.service.Vector;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.maps.tilemaps.TileSet;
import dwlab.shapes.sprites.Camera;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;

public class DrawTileExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new DrawTileExample() ).act();
	}
	
	
	int tileMapWidth = 16;
	int tileMapHeight = 12;
	double shakingPeriod = 1d;
	double periodBetweenShakes = 3d;

	TileMap tileMap = TileMap.create( new TileSet( new Image( "res/tiles.png", 8, 4 ) ), tileMapWidth, tileMapHeight );
	Sound hitSound = new Sound( "res/hit.ogg" );
	double shakingK;
	double lastShakingTime = -100;

	
	@Override
	public void init() {
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y < tileMapHeight; y++ ) {
			for( int x = 0; x < tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}
		
		tileMap.visualizer = new Visualizer(){
			double dAngle = Math.PI / 6d;
			double dCoord = 0.2;

			Vector vector = new Vector();
			
			
			@Override
			public void drawTile( TileMap tileMap, double x, double y, double width, double height, int tileX, int tileY, Color drawingColor ) {
				TileSet tileSet =tileMap.tileSet;
				int tileValue = getTileValue( tileMap, tileX, tileY );
				if( tileValue == tileSet.emptyTile ) return;

				double power = Math.max( 0d, 1d - Service.distance( x, y ) / 24d );
				x += Service.random( -dCoord * shakingK, dCoord * shakingK ) * power;
				y += Service.random( -dCoord * shakingK, dCoord * shakingK ) * power;

				Camera.current.fieldToScreen( x, y, vector );
				tileSet.image.draw( tileValue, vector.x, vector.y, width, height, Service.random( -dAngle * shakingK, dAngle * shakingK ) * power );
			}
		};
	}
	

	@Override
	public void logic() {
		if( time - lastShakingTime > periodBetweenShakes ) {
			lastShakingTime = time;
			hitSound.play();
		}
		if( time - lastShakingTime < shakingPeriod ) {
			shakingK = Math.pow( 1.0 - ( time - lastShakingTime ) / shakingPeriod, 2 );
		} else {
			shakingK = 0.0;
		}
	}
	

	@Override
	public void render() {
		tileMap.draw();
		printText( "DrawTile example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
