package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Camera;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.Color;
import java.util.HashMap;
import java.util.Map;

public class DrawingShape extends ServiceObjects {
	public static DrawingShape defaultHandler = new DrawingShape();
	
	public static HashMap<ShapeType, DrawingShape> handlersMap = new HashMap<ShapeType, DrawingShape>();



	public static void register( ShapeType shapeType, DrawingShape handler ) {
		handlersMap.put( shapeType, handler );
	}



	public void perform( Sprite sprite, Color drawingColor, boolean empty ) {
		Camera.current.fieldToScreen( sprite, vector1 );
		Camera.current.sizeFieldToScreen( sprite, vector2 );
		perform( drawingColor, empty );
	}
	
	protected void perform( Color drawingColor, boolean empty ) {
		Graphics.drawRectangle( vector1.x, vector1.y, vector2.x, vector2.y, 0d, drawingColor, empty );
	}
	
	public void perform( Sprite sprite, double x, double y, double width, double height, Color drawingColor, boolean empty ) {
		Camera.current.fieldToScreen( x, y, vector1 );
		Camera.current.sizeFieldToScreen( width, height, vector2 );
		perform( drawingColor, empty );
	}


	public static DrawingShape handlers[];

	public static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlers = new DrawingShape[ quantity ];
		for( int n = 0; n < quantity; n++ ) handlers[ n ] = defaultHandler;
		for( Map.Entry<ShapeType, DrawingShape> entry : handlersMap.entrySet() ) {
			handlers[ entry.getKey().getNum() ] = entry.getValue();
		}
	}
}