package examples;
import java.lang.Math;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.shapes.maps.TileMap;
import dwlab.base.Image;
import dwlab.base.Project;
import dwlab.shapes.maps.TileSet;
import dwlab.visualizers.Visualizer;

public class DrawTileExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DrawTileExample() ).act();
	}
	
	
	public final int tileMapWidth = 16;
	public final int tileMapHeight = 12;
	public final double shakingPeriod = 1.0;
	public final double periodBetweenShakes = 3.0;

	public TileMap tileMap = TileMap.create( TileSet.create( Image.fromFile( " incbintiles .png", 8, 4 ) ), tileMapWidth, tileMapHeight );
	public tSound hitSound = tSound.load( " incbinhit .ogg", false );
	public double shakingK;
	public double lastShakingTime = -100;

	
	@Override
	public void init() {
		initGraphics();
		tileMap.setSize( tileMapWidth * 2, tileMapHeight * 2 );
		for( int y = 0; y <= tileMapHeight; y++ ) {
			for( int x = 0; x <= tileMapWidth; x++ ) {
				tileMap.setTile( x, y, rand( 1, 31 ) );
			}
		}
		tileMap.visualizer = new ShakingVisualizer();
	}
	

	@Override
	public void logic() {
		if( time - lastShakingTime > periodBetweenShakes ) {
			lastShakingTime = time;
			hitSound.play();
		}
		if( time - lastShakingTime < shakingPeriod ) {
			shakingK = ( 1.0 - ( time - lastShakingTime ) / shakingPeriod ) ^ 2;
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

public class ShakingVisualizer extends Visualizer {
	public final double dAngle = 15;
	public final double dCoord = 0.2;

	public void drawTile( TileMap tileMap, double x, double y, double width, double height, int tileX, int tileY ) {
		TileSet tileSet =tilemap.tileSet;
		int tileValue = getTileValue( tileMap, tileX, tileY );
		if( tileValue == tileSet.emptyTile ) return;

		setRotation( Service.random( -dAngle * example.shakingK, dAngle * example.shakingK ) );
		x += Service.random( -dCoord * example.shakingK, dCoord * example.shakingK );
		y += Service.random( -dCoord * example.shakingK, dCoord * example.shakingK );

		double sX, double sY;
		currentCamera.fieldToScreen( x, y, sX, sY );

		tileSet.image.draw( sX, sY, width, height, tileValue )		;

		setRotation( 0 );
	}
}
