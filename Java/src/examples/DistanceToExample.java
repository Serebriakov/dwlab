package examples;

import dwlab.platform.Platform;

import dwlab.base.service.Align;
import dwlab.base.service.Vector;
import dwlab.base.service.Service;
import dwlab.base.*;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.Shape;
import dwlab.shapes.layers.Layer;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;

public class DistanceToExample extends Project {
	static {
		Platform.current.init();
	}
	
	public static void main(String[] argv) {
		( new DistanceToExample() ).act();
	}
	
	
	int spritesQuantity = 20;

	Layer Layer = new Layer();
	LineSegment lineSegment = new LineSegment( null, null );
	Sprite minSprite;

	
	@Override
	public void init() {
		for( int n = 1; n <= spritesQuantity; n++ ) {
			Sprite sprite = new Sprite( ShapeType.oval, Service.random( -15, 15 ), Service.random( -11, 11 ), 1d, 1d );
			sprite.setDiameter( Service.random( 0.5, 1.5 ) );
			sprite.visualizer.setRandomColor();
			Layer.addLast( sprite );
		}
		cursor = new Sprite( ShapeType.oval, 0d, 0d, 0.5d, 0.5d );
		lineSegment.pivot[ 0 ] = cursor;
	}
	

	@Override
	public void logic() {
		minSprite = null;
		double minDistance = 0;
		for( Shape shape : Layer.children ) {
			Sprite sprite = shape.toSprite();
			if( cursor.distanceTo( sprite ) < minDistance || minSprite == null ) {
				minSprite = sprite;
				minDistance = cursor.distanceTo( sprite );
			}
		}
		lineSegment.pivot[ 1 ] = minSprite;
	}
	

	@Override
	public void render() {
		Layer.draw();

		lineSegment.draw();
		Platform.current.drawText( Service.trim( cursor.distanceTo( minSprite ) ), 0.5d * ( cursor.getX() + minSprite.getX() ), 0.5 * ( cursor.getY() + minSprite.getY() ) );

		Vector center = Vector.fieldToScreen( cursor );
		Platform.current.drawLine( center.x, center.y, 400, 300 );
		cursor.print( String.valueOf( time ) );
		cursor.print( Service.trim( cursor.distanceTo( 0, 0 ) ) );

		printText( "Direction to field center is " + Service.trim( cursor.directionTo( 0, 0 ) ) );
		printText( "Direction to nearest sprite is " + Service.trim( cursor.directionTo( minSprite ) ), 1 );
		printText( "DirectionTo, DirectionToPoint, DistanceTo, DistanceToPoint example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
