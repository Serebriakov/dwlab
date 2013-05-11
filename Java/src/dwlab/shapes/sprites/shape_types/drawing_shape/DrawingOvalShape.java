package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;

public class DrawingOvalShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Sprite spriteShape, Vector coords, Vector sizes ) {
		Graphics.drawLongOval( coords.x, coords.y, sizes.x, sizes.y );
	}
}
