package dwlab.shapes.sprites.shape_types;

import dwlab.shapes.sprites.Sprite;

public class Rectangle extends ShapeType {
	public static Sprite serviceRectangle = Rectangle.serviceRectangle;

	
	@Override
	public int getNum() {
		return 2;
	}

	
	@Override
	public String getName() {
		return "Rectangle";
	}

	
	@Override
	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		serviceRectangle.setCoords( sprite.getX() * xScale + dX, sprite.getY() * yScale + dY );
		serviceRectangle.setSize( sprite.getWidth() * xScale, sprite.getHeight() * yScale );
		return serviceRectangle;
	}
}

