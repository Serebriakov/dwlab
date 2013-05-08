package examples;
import java.lang.Math;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Service;
import dwlab.visualizers.Visualizer;

public class DrawEmptyRectangleExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		while( true ) {
			for( int n = 1; n <= 10; n++ ) {
				double width = Service.random( 700 );
				double height = Service.random( 500 );
				Graphics.setColor( Service.random( 128, 255 ), Service.random( 128, 255 ), Service.random( 128, 255 ) );
				Graphics.drawEmptyRectangle( Service.random( 800 - width ), Service.random( 600 - height ), width, height );
			}
			Graphics.setColor( 0d, 0d, 0d, 0.04d );
			Graphics.drawRectangle( 0, 0, 800, 600 );
			Visualizer.resetColor();
			printText( "L_Graphics.drawEmptyRectangle example", Align.TO_CENTER, Align.TO_BOTTOM );
			Graphics.switchBuffers();
		}
	}
}