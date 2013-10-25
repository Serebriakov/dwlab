package examples;

import dwlab.platform.Platform;

import dwlab.base.Project;
import dwlab.base.images.Image;
import dwlab.base.service.Align;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.maps.DoubleMap;

public class DrawCircleExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		Platform.current.setClearingColor( 0d, 0d, 1d );
		( new DrawCircleExample() ).act();
		Platform.current.setClearingColor( 0d, 0d, 0d );
	}
	
	
	int mapSize = 128;
	double mapScale = 4.0;
	double picScale = 5.0;

	DoubleMap doubleMap = new DoubleMap( mapSize, mapSize );
	Image image;
	
	ButtonAction next = ButtonAction.create( KeyboardKey.create( Key.SPACE ) );

	
	@Override
	public void init() {
		double Array[][][] = { { { 0, -7, 5 }, { 0, -1.5, 7 }, { -4, -3, 2 }, { 4, -3, 2 }, { 0, 6, 9 } }, 
				{ { 0, -7, 1.5 }, { -1, -8, 1 }, { 1, -8, 1 }, { 0, -3, 1.0 }, { 0, -2, 1 }, { 0, -0.5, 1 } } };
		for( int col = 0; col <= 1; col++ ) {
			for( double shape[] : Array[ col ] ) {
				doubleMap.drawCircle( shape[ 0 ] * picScale + 0.5d * mapSize, shape[ 1 ] * picScale + 0.5d * mapSize, 0.5d * shape[ 2 ] * picScale, 1d - 0.7d * col );
			}
		}
		image = doubleMap.toNewImage( DoubleMap.Channel.RGB );
	}
	

	@Override
	public void logic() {
		if( next.wasPressed() ) {
			doubleMap.blur();
			image = doubleMap.toNewImage( DoubleMap.Channel.RGB );
		}
	}
	

	@Override
	public void render() {
		image.draw( 0, 400d, 300d, mapScale * mapSize, mapScale * mapSize );
		printText( "Press space to blur map" );
		printText( "DrawCircle, Blur example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	

	@Override
	public void deInit() {
		Platform.current.setClearingColor( 0, 0, 0 );
	}
}
