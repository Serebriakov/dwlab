package dwlab.shapes.sprites.shape_types;

import dwlab.shapes.sprites.Sprite;

public class Oval extends ShapeType {
	private static Sprite serviceOval = new Sprite( ShapeType.oval );

	
	@Override
	public int getNum() {
		return 1;
	}

	
	@Override
	public String getName() {
		return "Oval";
	}

	
	@Override
	public Sprite getTileSprite( Sprite sprite, double dX, double dY, double xScale, double yScale ) {
		serviceOval.setCoords( sprite.getX() * xScale + dX, sprite.getY() * yScale + dY );
		serviceOval.setSize( sprite.getWidth() * xScale, sprite.getHeight() * yScale );
		return serviceOval;
	}
}
