package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;

public class DrawingTopRightTriangleShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Sprite spriteShape, Vector coords, Vector sizes ) {
		sizes.x *= 0.5d;
		sizes.y *= 0.5d;
		Graphics.startPolygon( 3 );
		Graphics.addPolygonVertex( coords.x - sizes.x, coords.y - sizes.y );
		Graphics.addPolygonVertex( coords.x + sizes.x, coords.y - sizes.y );
		Graphics.addPolygonVertex( coords.x + sizes.x, coords.y + sizes.y );
		Graphics.drawPolygon();
	}
}
