package examples;
import java.lang.Math;
import dwlab.base.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.Object;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;

public class SaveToFileExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new SaveToFileExample() ).act();
	}
	
	
	public final int spritesQuantity = 70;

	public Layer layer = new Layer();
	public double ang;
	public Sprite oldSprite;

	public void init() {
		initGraphics();
		for( int n = 1; n <= spritesQuantity; n++ ) {
			oldSprite = Sprite.fromShape( Service.random( -15, 15 ), Service.random( -11, 11 ), , , Sprite.oval, Service.random( 360 ), 5 );
			oldSprite.setDiameter( Service.random( 0.5, 1.5 ) );
			oldSprite.visualizer.setRandomColor();
			layer.addLast( oldSprite );
		}
	}

	public void logic() {
		ang = 1500 * Math.sin( 7 * time );
		for( Sprite sprite : layer ) {
			oldSprite.directTo( sprite );
			oldSprite.angle += perSecond( ang ) + Service.random( -45, 45 );
			sprite.moveForward();
			oldSprite = sprite;
		}

		if( keyHit( key_F2 ) ) layer.saveToFile( "sprites.lw" );
		if( keyHit( key_F3 ) ) layer = Layer( Object.loadFromFile( "sprites.lw" ) );

		if( appTerminate() || keyHit( key_Escape ) ) exiting = true;
	}

	public void render() {
		layer.draw();
		printText( "Press F2 to save and F3 to load position of sprites" );
		printText( "LoadFromFile, SaveToFile example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
