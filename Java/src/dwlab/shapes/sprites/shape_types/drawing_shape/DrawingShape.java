package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Graphics;
import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ServiceObjects;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map;

public class DrawingShape extends ServiceObjects {
	public static DrawingShape defaultHandler = new DrawingShape();
	
	public static HashMap<ShapeType, DrawingShape> handlersMap = new HashMap<ShapeType, DrawingShape>();



	public static void register( ShapeType shapeType, DrawingShape handler ) {
		handlersMap.put( shapeType, handler );
	}



	public void perform( Sprite sprite, Sprite spriteShape, Vector coords, Vector sizes ) {
		Graphics.drawRectangle( coords.x, coords.y, sizes.x, sizes.y );
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