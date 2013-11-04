package examples;

import dwlab.base.service.Align;
import dwlab.base.Project;
import dwlab.platform.LWJGL;
import dwlab.shapes.sprites.Sprite;

public class LeftXExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new LeftXExample() ).act();
	}
	
	
	Sprite rectangle = new Sprite( 0d, 0d, 8d, 6d );
	Sprite ball = new Sprite( 0d, 0d, 1d );

	
	@Override
	public void init() {
		rectangle.visualizer.set( "FF0000" );
		ball.visualizer.set( "FFFF00" );
	}

	
	@Override
	public void logic() {
		rectangle.setMouseCoords();
	}
	

	@Override
	public void render() {
		rectangle.draw();
		ball.setCoords( rectangle.leftX(), rectangle.getY() );
		ball.draw();
		ball.setCoords( rectangle.getX(), rectangle.topY() );
		ball.draw();
		ball.setCoords( rectangle.rightX(), rectangle.getY() );
		ball.draw();
		ball.setCoords( rectangle.getX(), rectangle.bottomY() );
		ball.draw();
		printText( "LeftX, topY, RightX, bottomY example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
