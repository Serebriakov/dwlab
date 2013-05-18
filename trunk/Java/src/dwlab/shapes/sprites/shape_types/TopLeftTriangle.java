package dwlab.shapes.sprites.shape_types;

import dwlab.shapes.sprites.Sprite;

public class TopLeftTriangle extends ShapeType {
	public static Sprite serviceTriangle = new Sprite();
	

	protected TopLeftTriangle() {
	}
	
	
	@Override
	public int getNum() {
		return 4;
	}
	

	@Override
	public String getName() {
		return "Top-left triangle";
	}

	
	@Override
	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		serviceTriangle.setCoords( sprite.getX() * xScale + dX, sprite.getY() * yScale + dY );
		serviceTriangle.setSize( sprite.getWidth() * xScale, sprite.getHeight() * yScale );
		serviceTriangle.shapeType = sprite.shapeType;
		return serviceTriangle;
	}
}