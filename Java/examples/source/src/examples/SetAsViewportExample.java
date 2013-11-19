package examples;

import static dwlab.platform.Functions.*;
import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.platform.LWJGL;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class SetAsViewportExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new SetAsViewportExample() ).act();
	}
	
	
	int spritesQuantity = 100;

	Layer layer = new Layer();
	Sprite rectangle = new Sprite( 0, 0, 30, 20 );

	
	@Override
	public void init() {
		cursor = new Sprite( 0, 0, 8, 6 );
		for( int n = 1; n < spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( ShapeType.oval, Service.random( -13d, 13d ), Service.random( -8d, 8d ), 0d, 0d, Service.random( Math.PI * 2d ),
					Service.random( 3d, 7d ) );
			sprite.setDiameter( Service.random( 0.5d, 1.5d ) );
			sprite.visualizer.setRandomColor();
			layer.addLast( sprite );
		}
	}

	
	@Override
	public void logic() {
		for( Shape shape : layer.children ) {
			Sprite sprite = shape.toSprite();
			sprite.moveForward();
			sprite.bounceInside( rectangle );
		}
	}
	

	@Override
	public void render() {
		cursor.setAsViewport();
		layer.draw();
		rectangle.drawContour( 2d );
		resetViewport();
		cursor.drawContour( 2d );
		printText( "SetAsViewport, ResetViewport example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
