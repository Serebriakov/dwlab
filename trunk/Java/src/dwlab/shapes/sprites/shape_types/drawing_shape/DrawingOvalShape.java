package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.visualizers.Color;

public class DrawingOvalShape extends DrawingShape {
	@Override
	public void perform( Color drawingColor, boolean empty ) {
		Graphics.drawLongOval( vector1.x, vector1.y, vector2.x, vector2.y, 0d, drawingColor, empty );
	}
}
