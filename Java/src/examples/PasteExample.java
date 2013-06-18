package examples;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Sys;
import dwlab.base.images.Image;
import dwlab.base.images.ImageBuffer;
import dwlab.base.service.Align;
import dwlab.shapes.maps.DoubleMap;

public class PasteExample extends Project {
	static int mapSize = 128;
	
	static {
		Graphics.init();
	}
	
	
	public static void main(String[] argv) {
		DoubleMap sourceMap = new DoubleMap( mapSize, mapSize );
		sourceMap.drawCircle( mapSize * 0.375d, mapSize * 0.375d, mapSize * 0.35d, 0.6d );
		draw( sourceMap.toNewImage(), "Source map" );

		DoubleMap targetMap = new DoubleMap( mapSize, mapSize );
		targetMap.drawCircle( mapSize * 0.625d, mapSize * 0.625d, mapSize * 0.35d, 0.8d );
		draw( targetMap.toNewImage(), "Target map" );

		DoubleMap doubleMap = new DoubleMap( mapSize, mapSize );
		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.PasteMode.ADD );
		doubleMap.limit();
		draw( doubleMap.toNewImage(), "Adding source map to target map" );

		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.PasteMode.MULTIPLY );
		draw( doubleMap.toNewImage(), "Multiplying source map with target map" );

		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.PasteMode.MAXIMUM );
		draw( doubleMap.toNewImage(), "Maximum of source map and target map" );

		doubleMap.paste( targetMap );
		doubleMap.paste( sourceMap, 0, 0, DoubleMap.PasteMode.MINIMUM );
		draw( doubleMap.toNewImage(), "Minimum of source map and target map" );

		ImageBuffer buffer = sourceMap.toNewImageBuffer( DoubleMap.Channel.RED );
		targetMap.paste( buffer, DoubleMap.Channel.GREEN );
		draw( buffer.toImage(), "Pasting maps to different color channels" );
	}

	
	public static void draw( Image image, String text ) {
		image.draw( fPS, 400, 300, mapSize * 4d, mapSize * 4d );
		printText( text );
		printText( "Paste example", Align.TO_CENTER, Align.TO_BOTTOM );
		Graphics.swapBuffers();
		Sys.waitForKey();
		Graphics.clearScreen();
	}
}