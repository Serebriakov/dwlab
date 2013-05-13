package examples;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.maps.DoubleMap;

public class DrawCircleExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DrawCircleExample() ).act();
	}
	
	
	int mapSize = 128;
	double mapScale = 4.0;
	double picScale = 5.0;

	DoubleMap doubleMap = new DoubleMap( mapSize, mapSize );
	
	ButtonAction next = ButtonAction.create( KeyboardKey.create( Key.SPACE ) );

	
	@Override
	public void init() {
		Graphics.setClearingColor( 0d, 0d, 1d );
		double Array[][][] = { { { 0, -7, 5 }, { 0, -1.5, 7 }, { -4, -3, 2 }, { 4, -3, 2 }, { 0, 6, 9 } }, 
				{ { 0, -7, 1.5 }, { -1, -8.0, 1 }, { 1, -8, 1 }, { 0, -3.5, 1.0 }, { 0.0, -2.0, 1.0 }, { 0.0, -0.5, 1.0 } } };
		for( int col = 0; col <= 1; col++ ) {
			for( double shape[] : Array[ col ] ) {
				doubleMap.drawCircle( shape[ 0 ] * picScale + 0.5 * mapSize, shape[ 1 ] * picScale + 0.5 * mapSize, 0.5 * shape[ 2 ] * picScale, 1.0 - 0.7 * col );
			}
		}
	}
	

	@Override
	public void logic() {
		if( next.wasPressed() ) doubleMap.blur();
	}
	

	@Override
	public void render() {
		doubleMap.toNewImage( DoubleMap.Channel.RGB ).draw( 0, 400d, 300d, mapScale * mapSize, mapScale * mapSize );
		printText( "Press space to blur map" );
		printText( "DrawCircle, Blur example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	

	@Override
	public void deInit() {
		Graphics.setClearingColor( 0, 0, 0 );
	}
}
