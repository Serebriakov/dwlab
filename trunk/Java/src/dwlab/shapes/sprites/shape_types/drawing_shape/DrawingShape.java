package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Obj;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import java.util.HashMap;
import java.util.Map;

public abstract class DrawingShape extends Obj {
	public static HashMap<ShapeType, DrawingShape> handlersMap = new HashMap<ShapeType, DrawingShape>();



	public static void register( ShapeType shapeType, DrawingShape handler ) {
		handlersMap.put( shapeType, handler );
	}



	public abstract boolean drawing( Sprite sprite );


	public static DrawingShape handlersArray[];

	public static void initSystem() {
		int quantity = ShapeType.shapeTypes.size();

		handlersArray = new DrawingShape[ quantity ];
		for( Map.Entry<ShapeType, DrawingShape> entry : handlersMap.entrySet() ) {
			handlersArray[ entry.getKey().getNum() ] = entry.getValue();
		}
	}
}