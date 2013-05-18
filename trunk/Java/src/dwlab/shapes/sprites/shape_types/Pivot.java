package dwlab.shapes.sprites.shape_types;

import dwlab.shapes.sprites.Sprite;

public class Pivot extends ShapeType {
	public static Sprite servicePivot = new Sprite( ShapeType.pivot );

	
	protected Pivot() {
	}
	
	
	@Override
	public int getNum() {
		return 0;
	}
	
	
	@Override
	public String getName() {
		return "Pivot";
	}
	

	@Override
	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		servicePivot.setCoords( sprite.getX() * xScale + dX, sprite.getY() * yScale + dY );
		return servicePivot;
	}
}