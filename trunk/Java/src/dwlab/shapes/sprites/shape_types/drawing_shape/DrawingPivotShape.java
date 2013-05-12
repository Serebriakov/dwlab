package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;

public class DrawingPivotShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Color drawingColor, boolean empty ) {
		Camera.current.fieldToScreen( sprite, vector1 );
		Graphics.drawOval( vector1.x, vector1.y, 5d, 5d, 0d, drawingColor, false );
	}
}
