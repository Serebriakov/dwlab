package examples;

import static dwlab.platform.Functions.*;

import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.platform.LWJGL;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.layers.World;
import dwlab.shapes.maps.tilemaps.TileMap;
import dwlab.shapes.sprites.Camera;

public class ParallaxExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		Classes.register();
		( new ParallaxExample() ).act();
		initCamera();
	}
	
	
	public TileMap ground;
	public TileMap grid;
	public TileMap clouds;

	@Override
	public void init() {
		Camera.current.setZoom( 32.0 );
		World world = World.fromFile( "res/parallax.lw" );
		Layer layer = (Layer) ( (Layer) world.findShape( Layer.class ) ).load();
		ground = (TileMap) layer.findShape( "Ground" );
		grid = (TileMap) layer.findShape( "Grid" );
		clouds = (TileMap) layer.findShape( "Clouds" );
	}

	
	@Override
	public void logic() {
		Camera.current.moveUsingArrows( 8.0 );
		Camera.current.limitWith( grid );
		ground.parallax( grid );
		clouds.parallax( grid );
	}
	

	@Override
	public void render() {
		ground.draw();
		grid.draw();
		clouds.draw();
		printText( "Move camera with arrow Keys" );
		printText( "Parallax example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
