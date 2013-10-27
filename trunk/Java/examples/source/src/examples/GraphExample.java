package examples;

import dwlab.base.Project;
import dwlab.base.service.Align;
import dwlab.base.service.Service;
import dwlab.controllers.ButtonAction;
import dwlab.controllers.MouseButton;
import dwlab.platform.LWJGL;
import dwlab.shapes.graphs.Graph;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.ShapeType;
import dwlab.visualizers.ContourVisualizer;
import dwlab.visualizers.Visualizer;
import java.util.LinkedList;

public class GraphExample extends Project {
	public static void main(String[] argv) {
		LWJGL.init();
		main();
	}
	
	public static void main() {
		( new GraphExample() ).act();
	}
	
	
	int pivotsQuantity = 150;
	double maxDistance = 3.0;
	double minDistance = 1.0;

	Graph graph = new Graph();
	Sprite selectedPivot;
	LinkedList path;
	Visualizer pivotVisualizer = new Visualizer( "4F4FFF", 1d, true );
	Visualizer lineSegmentVisualizer = new ContourVisualizer( 0.15, "FF4F4F", 3.0, true );
	Visualizer pathVisualizer = new ContourVisualizer( 0.15, "4FFF4F", 4.0, true );

	ButtonAction setSource = ButtonAction.create( MouseButton.create( MouseButton.LEFT_BUTTON ) );
	ButtonAction setDestination = ButtonAction.create( MouseButton.create( MouseButton.RIGHT_BUTTON ) );
	
	
	@Override
	public void init() {
		cursor = new Sprite( ShapeType.oval, 0, 0, 0.5, 0.5 );
		for( int n = 0; n <= pivotsQuantity; n++ ) {
			while( true ) {
				double x = Service.random( -15,15 );
				double y = Service.random( -11, 11 );
				boolean passed = true;
				for( Sprite pivot : graph.contents.keySet() ) {
					if( pivot.distanceTo( x, y ) < minDistance ) {
						passed = false ;
						break;
					}
				}
				if( passed ) {
					graph.addPivot( new Sprite( x, y, 0.3 ) );
					break;
				}
			}
		}
		for( Sprite pivot1 : graph.contents.keySet() ) {
			for( Sprite pivot2 : graph.contents.keySet() ) {
				if( pivot1 != pivot2 && pivot1.distanceTo( pivot2 ) <= maxDistance ) {
					if( graph.findLineSegment( pivot1, pivot2 ) == null ) {
						boolean passed = true;
						LineSegment newLineSegment = new LineSegment( pivot1, pivot2 );
						for( LinkedList<LineSegment> list : graph.contents.values() ) {
							for( LineSegment lineSegment : list ) {
								if( lineSegment.collidesWithLineSegment( newLineSegment, false ) ) {
									passed = false;
									break;
								}
							}
						}
						if( passed ) graph.addLineSegment( newLineSegment );
					}
				}
			}
		}
	}
	

	@Override
	public void logic() {
		if( setSource.wasPressed() ) {
			selectedPivot = graph.findPivotCollidingWithSprite( cursor );
			path = null;
		}
		if( setDestination.wasPressed() && selectedPivot != null ) {
			Sprite selectedPivot2 = graph.findPivotCollidingWithSprite( cursor );
			if( selectedPivot2 != null ) path = graph.findPath( selectedPivot, selectedPivot2 );
		}
	}
		

	@Override
	public void render() {
		graph.drawLineSegmentsUsing( lineSegmentVisualizer );
		Graph.drawPath( path, pathVisualizer );
		graph.drawPivotsUsing( pivotVisualizer );
		if( selectedPivot != null ) selectedPivot.drawUsingVisualizer( pathVisualizer );
		printText( "Select first pivot with left mouse button and second with right one" );
		printText( "LTGraph, FindPath, CollidesWithLineSegment example", Align.TO_CENTER, Align.TO_BOTTOM );
	}
}
