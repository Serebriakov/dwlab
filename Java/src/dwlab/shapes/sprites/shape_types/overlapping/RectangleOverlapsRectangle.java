package dwlab.shapes.sprites.shape_types.overlapping;

import dwlab.shapes.sprites.Sprite;

public class RectangleOverlapsRectangle extends SpritesOverlapping {
	public static RectangleOverlapsRectangle instance = new RectangleOverlapsRectangle();


	@Override
	public boolean check( Sprite rectangle1, Sprite rectangle2 ) {
		if( ( rectangle1.getX() - 0.5d * rectangle1.getWidth() <= rectangle2.getX() - 0.5d * rectangle2.getWidth() ) &&
				( rectangle1.getY() - 0.5d * rectangle1.getHeight() <= rectangle2.getY() - 0.5d * rectangle2.getHeight() ) &&
				( rectangle1.getX() + 0.5d * rectangle1.getWidth() >= rectangle2.getX() + 0.5d * rectangle2.getWidth() ) &&
				( rectangle1.getY() + 0.5d * rectangle1.getHeight() >= rectangle2.getY() + 0.5d * rectangle2.getHeight() ) ) return true; else return false;
	}
}
