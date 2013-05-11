package dwlab.shapes.sprites.shape_types;

import dwlab.base.Obj;
import dwlab.base.Service;
import dwlab.base.Vector;
import dwlab.shapes.Line;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;

public class ServiceObjects extends Obj {
	protected static Sprite servicePivot1 = new Sprite( ShapeType.pivot );
	protected static Sprite servicePivot2 = new Sprite( ShapeType.pivot );
	protected static Sprite servicePivot3 = new Sprite( ShapeType.pivot );
	protected static Sprite servicePivot4 = new Sprite( ShapeType.pivot );
	protected static Sprite servicePivots[] = new Sprite[ 4 ];
	protected static Sprite serviceOval1 = new Sprite( ShapeType.oval );
	protected static Sprite serviceOval2 = new Sprite( ShapeType.oval );
	protected static Sprite serviceSprite = new Sprite();
	protected static LineSegment serviceLineSegment = new LineSegment();
	protected static Line serviceLine1 = new Line();
	protected static Line serviceLine2 = new Line();
	protected static Line serviceLines[] = new Line[ 2 ];
	protected static Service.Margins margins = new Service.Margins();
	protected static Vector vector1 = new Vector();
	protected static Vector vector2 = new Vector();
	
	static {
		for( int n = 0; n <= 3; n++ ) {
			servicePivots[ n ] = new Sprite( ShapeType.pivot );
			if( n < 2 ) serviceLines[ n ] = new Line();
		}
	}
}
