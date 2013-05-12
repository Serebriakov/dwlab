package examples;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.Key;
import dwlab.controllers.KeyboardKey;
import dwlab.shapes.maps.DoubleMap;

public class PerlinNoiseExample extends Project {
	static int mapSize = 256;
	
	static ButtonAction increaseFrequency = ButtonAction.create( KeyboardKey.create( Key.ADD ) );
	static ButtonAction decreaseFrequency = ButtonAction.create( KeyboardKey.create( Key.SUBTRACT ) );
	static ButtonAction increaseAmplitude = ButtonAction.create( KeyboardKey.create( Key.RIGHT ) );
	static ButtonAction decreaseAmplitude = ButtonAction.create( KeyboardKey.create( Key.LEFT ) );
	static ButtonAction increaseDAmplitude = ButtonAction.create( KeyboardKey.create( Key.UP ) );
	static ButtonAction decreaseDAmplitude = ButtonAction.create( KeyboardKey.create( Key.DOWN ) );
	static ButtonAction addLayer = ButtonAction.create( KeyboardKey.create( Key.PAGE_UP ) );
	static ButtonAction removeLayer = ButtonAction.create( KeyboardKey.create( Key.PAGE_DOWN ) );
		
	DoubleMap doubleMap = new DoubleMap( mapSize, mapSize );
	int frequency = 16;
	double amplitude = 0.25;
	double dAmplitude = 0.5;
	int Layers = 4;
	Image image;
					

	public static void main(String[] argv) {
		Graphics.init();
	}
	
	
	@Override
	public void init() {
		update();
	}
	

	@Override
	public void logic() {
		if( increaseFrequency.wasPressed() ) {
			if( frequency < 256 ) frequency *= 2;
			update();
		} else if( decreaseFrequency.wasPressed() ) {
			if( frequency > 1 ) frequency /= 2;
			update();
		} else if( increaseAmplitude.wasPressed() ) {
			if( amplitude > 0.05 ) amplitude /= 2;
			update();
		} else if( decreaseAmplitude.wasPressed() ) {
			if( amplitude < 1.0 ) amplitude *= 2;
			update();
		} else if( increaseDAmplitude.wasPressed() ) {
			if( dAmplitude > 0.05 ) dAmplitude /= 2;
			update();
		} else if( decreaseDAmplitude.wasPressed() ) {
			if( dAmplitude < 2.0 ) dAmplitude *= 2;
			update();
		} else if( addLayer.wasPressed() ) {
			if( Layers < 8 ) Layers += 1;
			update();
		} else if( removeLayer.wasPressed() ) {
			if( Layers > 1 ) Layers -= 1;
			update();
		}
	}
	
	
	@Override
	public void render() {
		printText( "Starting  frequency" + frequency + " ( +/- to change )" );
		printText( "Starting  amplitude" + Service.trim( amplitude ) + " ( left / right arrow to change )", 1 );
		printText( "Starting amplitude  increment" + Service.trim( dAmplitude ) + " ( up / down arrow to change )", 2 );
		printText( "Layers  Quantity" + Layers + " ( page up / page down arrow to change )", 3 );
		printText( "PerlinNoise example", Align.TO_CENTER, Align.TO_BOTTOM );
		image.draw( 400, 300 );
	}
	
	
	@Override
	public void update() {
		Graphics.clearScreen();
		doubleMap.perlinNoise( frequency, frequency, amplitude, dAmplitude, Layers );
		image = doubleMap.toNewImage();
		Graphics.swapBuffers();
	}
}