package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.visualizers.Color;

public class DrawingBottomRightTriangleShape extends DrawingShape {
	@Override
	public void perform( Color drawingColor, boolean empty ) {
		vector2.x *= 0.5d;
		vector2.y *= 0.5d;
		Graphics.startPolygon( 3, drawingColor, empty );
		Graphics.addPolygonVertex( vector1.x + vector2.x, vector1.y - vector2.y );
		Graphics.addPolygonVertex( vector1.x + vector2.x, vector1.y + vector2.y );
		Graphics.addPolygonVertex( vector1.x - vector2.x, vector1.y + vector2.y );
		Graphics.drawPolygon();
	}
}
