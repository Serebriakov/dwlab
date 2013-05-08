package examples;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.shapes.maps.TileMap;
import dwlab.base.Project;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;

public class ParallaxExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new ParallaxExample() ).act();
	}
	
	
	public TileMap ground;
	public TileMap grid;
	public TileMap clouds;

	public void init() {
		initGraphics();
	currentCamera.setMagnification( 32.0 );

	setIncbin( true );
		Layer layer = loadLayer( Layer( World.fromFile( "parallax.lw" ).findShapeWithType( "LTLayer" ) ) );
	setIncbin( false );

		ground = TileMap( layer.findShape( "Ground" ) );
		grid = TileMap( layer.findShape( "Grid" ) );
		clouds = TileMap( layer.findShape( "Clouds" ) );
	}

	public void logic() {
		currentCamera.moveUsingArrows( 8.0 );
		currentCamera.limitWith( grid );
		ground.parallax( grid );
		clouds.parallax( grid );
		if( keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		ground.draw();
		grid.draw();
		clouds.draw();
		printText( "Move camera with arrow keys" );
		printText( "Parallax example", currentCamera.x, currentCamera.y + 9, Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
