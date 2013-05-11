package examples;
import dwlab.base.*;
import dwlab.shapes.maps.TileMap;
import dwlab.shapes.maps.TileSet;
import dwlab.shapes.sprites.Camera;
import dwlab.visualizers.Visualizer;

public class DrawTileExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
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
		for( int y = 0; y <= tileMapHeight; y++ ) {
			for( int x = 0; x <= tileMapWidth; x++ ) {
				tileMap.setTile( x, y, (int) Service.random( 1, 31 ) );
			}
		}
		
		tileMap.visualizer = new Visualizer(){
			double dAngle = 15;
			double dCoord = 0.2;

			Vector vector = new Vector();
			
			
			@Override
			public void drawTile( TileMap tileMap, double x, double y, double width, double height, int tileX, int tileY ) {
				TileSet tileSet =tileMap.tileSet;
				int tileValue = getTileValue( tileMap, tileX, tileY );
				if( tileValue == tileSet.emptyTile ) return;

				x += Service.random( -dCoord * shakingK, dCoord * shakingK );
				y += Service.random( -dCoord * shakingK, dCoord * shakingK );

				Camera.current.fieldToScreen( x, y, vector );
				tileSet.image.draw( tileValue, vector.x, vector.y, width, height, Service.random( -dAngle * shakingK, dAngle * shakingK ) );
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
