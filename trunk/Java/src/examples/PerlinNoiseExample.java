package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.shapes.maps.DoubleMap;

public class EnframeExample extends Project {
	public final int mapSize = 256;
	
	public static void main(String[] argv) {
		Graphics.init();

		DoubleMap doubleMap = new DoubleMap();
		doubleMap.setResolution( mapSize, mapSize );

		int frequency = 16;
		double amplitude = 0.25;
		double dAmplitude = 0.5;
		int layers = 4;

		while( true ) {
			cls;
			doubleMap.perlinNoise( frequency, frequency, amplitude, dAmplitude, layers );
			tPixmap pixmap = doubleMap.toNewPixmap();
			drawPixmap( pixmap, 400 - 0.5 * pixmapWidth( pixmap ), 300 - 0.5 * pixmapHeight( pixmap ) )		;
			printText( "Starting  frequency" + frequency + " ( +/- to change )" );
			drawText( "Starting  amplitude" + Service.trim( amplitude ) + " ( left / right arrow to change )", 0, 16 );
			drawText( "Starting amplitude  increment" + Service.trim( dAmplitude ) + " ( up / down arrow to change )", 0, 32 );
			drawText( "Layers  quantity" + layers + " ( page up / page down arrow to change )", 0, 48 );
			printText( "PerlinNoise example", Align.TO_CENTER, Align.TO_BOTTOM );
			Graphics.switchBuffers();

			while( true ) {
				if( keyHit( key_NumAdd ) ) {
					if( frequency < 256 ) frequency *= 2;
					break;
				} else if( keyHit( key_NumSubtract ) ) {
					if( frequency > 1 ) frequency /= 2;
					break;
				} else if( keyHit( key_Left ) ) {
					if( amplitude > 0.05 ) amplitude /= 2;
					break;
				} else if( keyHit( key_Right ) ) {
					if( amplitude < 1.0 ) amplitude *= 2;
					break;
				} else if( keyHit( key_Down ) ) {
					if( dAmplitude > 0.05 ) dAmplitude /= 2;
					break;
				} else if( keyHit( key_Up ) ) {
					if( dAmplitude < 2.0 ) dAmplitude *= 2;
					break;
				} else if( keyHit( key_PageUp ) ) {
					if( layers < 8 ) layers += 1;
					break;
				} else if( keyHit( key_PageDown ) ) {
					if( layers > 1 ) layers -= 1;
					break;
				}
			until keyDown( key_Escape );
		until keyDown( key_Escape );
	}
}