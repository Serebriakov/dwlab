package examples;
import java.lang.Math;
import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;

public class DrawUsingSpriteExample extends Project {
	static {
		Graphics.init();
	}
	
	public static void main(String[] argv) {
		( new DrawUsingSpriteExample() ).act();
	}
	
	
	int flowersQuantity = 12;
	double flowerCircleDiameter = 9;
	double flowerDiameter = 1.8;

	Sprite flowers[] = new Sprite[ flowersQuantity ];
	Visualizer flowerVisualizer = new Visualizer(){
		int circlesQuantity = 30;
		int circlesPer360 = 7;
		double amplitude = 0.15;

		
		@Override
		public void drawUsingSprite( Sprite sprite, Sprite spriteShape, Color drawingColor ) {
			double spriteDiameter = sprite.getDiameter();
			double circleDiameter = Camera.current.distFieldToScreen( Math.PI * 2d * spriteDiameter / circlesQuantity ) * 1.5;
			for( int n = 0; n <= circlesQuantity; n++ ) {
				double angle = Math.PI * 2d * n / circlesQuantity;
				double angles = sprite.angle + angle;
				double distance = spriteDiameter * ( 1.0 + amplitude * Math.sin( Math.PI * 2d * ( time + 1d * n / circlesQuantity * circlesPer360 ) ) );
				Vector vector = new Vector();
				Camera.current.fieldToScreen( sprite.getX() + distance * Math.cos( angles ), sprite.getY() + distance * Math.sin( angles ), vector );
				Graphics.drawOval( vector.x - 0.5 * circleDiameter, vector.y - 0.5 * circleDiameter, circleDiameter, circleDiameter );
			}
		}
	};

	
	@Override
	public void init() {
		for( int n = 0; n < flowersQuantity; n++ ) {
			flowers[ n ] = new Sprite();
			flowers[ n ].setDiameter( flowerDiameter );
		}
	}
	

	@Override
	public void logic() {
		for( int n = 0; n < flowersQuantity; n++ ) {
			double angle = Math.PI * ( n * 2d / flowersQuantity + 0.25 * time );
			flowers[ n ].setCoords( Math.cos( angle ) * flowerCircleDiameter, Math.sin( angle ) * flowerCircleDiameter );
			flowers[ n ].angle = Math.PI * 0.5  * time;
		}
	}

	
	@Override
	public void render() {
		for( int n = 0; n < flowersQuantity; n++ ) {
			flowers[ n ].drawUsingVisualizer( flowerVisualizer );
		}
		printText( "DrawUsingSprite example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
