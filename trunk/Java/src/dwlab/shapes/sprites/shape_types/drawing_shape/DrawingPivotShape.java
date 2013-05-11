package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;

public class DrawingPivotShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Sprite spriteShape, Vector coords, Vector sizes ) {
		Graphics.drawOval( coords.x, coords.y, 5d, 5d );
	}
}
