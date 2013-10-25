package examples;

import dwlab.platform.Platform;
import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.visualizers.ContourVisualizer;
import dwlab.shapes.sprites.Sprite;

public class LimitWithExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new LimitWithExample() ).act();
	}
	
	
	Sprite ball[] = new Sprite[ 7 ];
	Sprite rectangle = new Sprite( 0, 0, 22, 14 );

	
	@Override
	public void init() {
		rectangle.visualizer = new ContourVisualizer( 0.1, "FF0000" );
		for( int n = 0; n <= 6; n++ ) {
			ball[ n ] = new Sprite( 0d, 0d, 0.5d * ( 7 - n ) );
			ball[ n ].visualizer.setRandomColor();
		}
	}
	

	@Override
	public void logic() {
		for( int n = 0; n <= 6; n++ ) {
			ball[ n ].setMouseCoords();
		}
		ball[ 0 ].limitWith( rectangle );
		ball[ 1 ].limitHorizontallyWith( rectangle );
		ball[ 2 ].limitVerticallyWith( rectangle );
		ball[ 3 ].limitLeftWith( rectangle );
		ball[ 4 ].limitTopWith( rectangle );
		ball[ 5 ].limitRightWith( rectangle );
		ball[ 6 ].limitBottomWith( rectangle );
	}
	

	@Override
	public void render() {
		rectangle.draw();
		for( int n = 0; n <= 6; n++ ) {
			ball[ n ].draw();
		}
		printText( "Move cursor to see how the balls are limited in movement" );
		printText( "Limit...With example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
