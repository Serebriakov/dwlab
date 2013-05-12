package examples;
import dwlab.base.images.Image;
import dwlab.base.*;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;


public class DirectToExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DirectToExample() ).act();
	}
	

	int koloboksQuantity = 50;

	Layer Layer = new Layer();
	Image kolobokImage = new Image( "res/kolobok.png" );

	
	@Override
	public void init() {
		for( int n = 1; n <= koloboksQuantity; n++ ) {
			Kolobok kolobok = new Kolobok();
			kolobok.setCoords( Service.random( -15, 15 ), Service.random( -11, 11 ) );
			kolobok.setDiameter( Service.random( 1, 3 ) );
			kolobok.shapeType = ShapeType.oval;
			kolobok.visualizer.setRandomColor();
			kolobok.visualizer.image = kolobokImage;
			Layer.addLast( kolobok );
		}
	}
	

	@Override
	public void logic() {
		Layer.act();
	}
	

	@Override
	public void render() {
		Layer.draw();
		printText( "DirectTo example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	

	public static class Kolobok extends Sprite {
		@Override
		public void act() {
			directTo( cursor );
		}
	}
}



