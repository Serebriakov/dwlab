package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Image;
import dwlab.base.Project;
import dwlab.shapes.maps.DoubleMap;

public class EnframeExample extends Project {
	public final int mapSize = 128;
	
	public static void main(String[] argv) {
		Graphics.init();

		DoubleMap sourceMap = DoubleMap.create( mapSize, mapSize );
		sourceMap.drawCircle( mapSize * 0.375, mapSize * 0.375, mapSize * 0.35, 0.6 );
		draw( sourceMap.toNewImage(), "Source map" );

		DoubleMap targetMap = DoubleMap.create( mapSize, mapSize );
		targetMap.drawCircle( mapSize * 0.625, mapSize * 0.625, mapSize * 0.35, 0.8 );
		draw( targetMap.toNewImage(), "Target map" );

		DoubleMap doubleMap = DoubleMap.create( mapSize, mapSize );
		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.add );
		doubleMap.limit();
		draw( doubleMap.toNewImage(), "Adding source map to target map" );

		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.multiply );
		draw( doubleMap.toNewImage(), "Multiplying source map with target map" );

		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.maximum );
		draw( doubleMap.toNewImage(), "Maximum of source map and target map" );

		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.minimum );
		draw( doubleMap.toNewImage(), "Minimum of source map and target map" );

		setScale( 4.0, 4.0 );
		Image image = sourceMap.toNewImage( DoubleMap.red );
		targetMap.pasteToImage( image, 0, 0, 0, DoubleMap.green );
		draw( image, "Pasting maps to different color channels" );


		public static void draw( Image image, String text ) {
			setScale ( 4.0, 4.0 );
			drawImage( image.bMaxImage, 400, 300 );
			setScale( 1.0, 1.0 );
			printText( text );
			printText( "Paste example", Align.TO_CENTER, Align.TO_BOTTOM );
			Graphics.switchBuffers();
			waitkey;
			cls;
		}
	}
}