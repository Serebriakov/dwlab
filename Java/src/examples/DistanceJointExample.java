package examples;

import dwlab.base.service.Align;
import dwlab.base.Graphics;
import dwlab.base.Project;
import dwlab.behavior_models.DistanceJoint;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.shapes.sprites.VectorSprite;
import dwlab.visualizers.ContourVisualizer;
import dwlab.visualizers.Visualizer;

public class DistanceJointExample extends Project {
	public static void main(String[] argv) {
		Graphics.init();
		( new DistanceJointExample() ).act();
	}
	
	
	Sprite hinge = new Sprite( ShapeType.oval, 0d, -8d, 1d, 1d );
	VectorSprite weight1 = new VectorSprite( ShapeType.oval, -8d, -6d, 3d, 3d );
	VectorSprite weight2 = new VectorSprite( ShapeType.oval, -12d, -9d, 3d, 3d );
	LineSegment rope1 = new LineSegment( hinge, weight1 );
	LineSegment rope2 = new LineSegment( weight1, weight2 );

	
	@Override
	public void init() {
		hinge.visualizer = new Visualizer( "FF0000", 1.0, true );
		weight1.visualizer = new Visualizer( "00FF00", 1.0, true );
		weight2.visualizer = new Visualizer( "FFFF00", 1.0, true );
		rope1.visualizer = new ContourVisualizer( 0.25d, "0000FF", 2.0d, true );
		rope2.visualizer = rope1.visualizer;
		weight1.attachModel( new DistanceJoint( hinge ) );
		weight2.attachModel( new DistanceJoint( weight1 ) );
	}
	

	@Override
	public void render() {
		hinge.draw();
		weight1.draw();
		weight2.draw();
		rope1.draw();
		rope2.draw();
		printText( "LTDistanceJoint example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
	

	@Override
	public void logic() {
		weight1.act();
		weight1.dY += perSecond( 2.0 );
		weight1.moveForward();
		weight2.act();
		weight2.dY += perSecond( 2.0 );
		weight2.moveForward();
	}
}

