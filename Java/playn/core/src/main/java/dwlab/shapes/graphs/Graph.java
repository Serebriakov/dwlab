/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.graphs;

import dwlab.base.XMLObject;
import static dwlab.platform.Functions.*;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.Shape;
import dwlab.shapes.sprites.Sprite;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Graph is a collection of pivots and line segments between them.
 */
public class Graph extends Shape {
	public HashMap<Sprite, LinkedList<LineSegment>> contents = new HashMap<Sprite, LinkedList<LineSegment>>();

	// ==================== Drawing ===================	

	/**
	 * Draws graph.
	 * LineSegments then pivots will be drawn using graph visualizer.
	 */
	@Override
	public void draw( Color drawingColor ) {
		if( visible ) {
			drawLineSegmentsUsing( visualizer, drawingColor );
			drawPivotsUsing( visualizer, drawingColor );
		}
	}


	/**
	 * Draws graph using another visualizer.
	 * LineSegments then pivots will be drawn using given visualizer.
	 */
	@Override
	public void drawUsingVisualizer( Visualizer vis, Color drawingColor ) {
		if( visible ) {
			drawLineSegmentsUsing( vis, drawingColor );
			drawPivotsUsing( vis, drawingColor );
		}
	}


	/**
	 * Draws pivots using given visualizer.
	 * @see #drawLineSegmentsUsing
	 */
	public void drawPivotsUsing( Visualizer visualizer, Color drawingColor ) {
		for( Sprite pivot: contents.keySet() ) {
			pivot.drawUsingVisualizer( visualizer, drawingColor );
		}
	}
	
	public void drawPivotsUsing( Visualizer visualizer ) {
		drawPivotsUsing( visualizer, Color.white );
	}


	/**
	 * Draws LineSegments using given visualizer.
	 * @see #drawPivotsUsing
	 */
	public void drawLineSegmentsUsing( Visualizer visualizer, Color drawingColor ) {
		for( LinkedList<LineSegment> list: contents.values() ) {
			for( LineSegment lineSegment: list ) lineSegment.drawUsingVisualizer( visualizer, drawingColor );
		}
	}
	
	public void drawLineSegmentsUsing( Visualizer visualizer ) {
		drawLineSegmentsUsing( visualizer, Color.white );
	}


	/**
	 * Draws path (given as list of pivots) using given visualizer.
	 */
	public static void drawPath( LinkedList<Sprite> path, Visualizer visualizer, Color drawingColor ) {
		if( path == null ) return;
		Sprite oldPivot = null;
		for( Sprite pivot: path ) {
			if( oldPivot != null ) ( new LineSegment( pivot, oldPivot ) ).drawUsingVisualizer( visualizer, drawingColor );
			oldPivot = pivot;
		}
	}
	
	public static void drawPath( LinkedList<Sprite> path, Visualizer visualizer ) {
		drawPath( path, visualizer, currentColor );
	}

	// ==================== Add / Remove items ===================	

	/**
	 * Adds pivot to the graph.
	 * @see #removePivot, #findPivotCollidingWith, #containsPivot
	 */
	public LinkedList<LineSegment> addPivot( Sprite pivot ) {
		LinkedList<LineSegment> list = contents.get( pivot );
		if( list == null ) {
			list = new LinkedList<LineSegment>();
			contents.put( pivot, list );
		}
		return list;
	}


	/**
	 * Adds line to the graph.
	 * If you'll try to add line which already exists in the graph, an error will occur.
	 * Pivots of the line will be also inserted into the graph if they are not already there.
	 * 
	 * @see #removeLine, #findLineCollidingWith, #containsLine, #findLine
	 */
	public void addLineSegment( LineSegment line ) {
		if( debug ) {
			LinkedList<LineSegment> list = contents.get( line.pivot[ 0 ] );
			if( list != null ) if( list.contains( line ) ) error( "This line already exists in the graph" );
			for( LineSegment otherLine: list ) {
				if( otherLine.pivot[ 0 ] == line.pivot[ 0 ] || otherLine.pivot[ 0 ] == line.pivot[ 1 ] ) {
					if( otherLine.pivot[ 1 ] == line.pivot[ 0 ] || otherLine.pivot[ 1 ] == line.pivot[ 1 ] ) {
						error( "Line with same pivots already exists in the graph" );
					}
				}
			}
		}

		for( int n=0; n <= 1; n++ ) {
			addPivot( line.pivot[ n ] ).addLast( line );
		}
	}


	/**
	 * Remove pivot from the graph.
	 * Line with this pivot will be also removed.
	 * 
	 * @see #addPivot, #findPivotCollidingWith, #containsPivot
	 */
	public void removePivot( Sprite pivot ) {
		LinkedList<LineSegment> list = contents.get( pivot );
		if( debug ) if( list == null ) error( "The deleting pivot doesn't belongs to the graph" );

		for( LineSegment lineSegment: list ) {
			removeLineSegment( lineSegment );
		}
	}


	/**
	 * Removes line from the graph.
	 * If line is not in the graph, you will encounter an error.
	 * 
	 * @see #addLine, #findLineCollidingWith, #containsLine, #findLine
	 */
	public void removeLineSegment( LineSegment line ) {
		LinkedList<LineSegment> list1 = contents.get( line.pivot[ 0 ] );
		LinkedList<LineSegment> list2 = contents.get( line.pivot[ 1 ] );
		if( debug ) if( list1 == null || list2 == null || !list1.contains( line ) ) error( "The deleting line doesn't belongs to the graph" );
		list1.remove( line );
		list2.remove( line );
	}

	// ==================== Collisions ===================

	/**
	 * Finds pivot which collides with given sprite.
	 * @see #addPivot, #removePivot, #containsPivot
	 */
	public Sprite findPivotCollidingWithSprite( Sprite sprite ) {
		for( Sprite pivot: contents.keySet() ) {
			if( sprite.collidesWith( pivot ) ) return pivot;
		}
		return null;
	}


	/**
	 * Finds line which collides with given sprite.
	 * @see #addLine, #removeLine, #containsLine, #findLine
	 */
	public LineSegment findLineCollidingWithSprite( Sprite sprite ) {
		for( LinkedList<LineSegment> list: contents.values() ) {
			for( LineSegment lineSegment: list ) if( sprite.collidesWith( lineSegment ) ) return lineSegment;
		}
		return null;
	}

	// ==================== Contents ====================

	/**
	 * Checks if graph contains given pivot.
	 * @return True if pivot is in the graph, otherwise False.
	 * @see #addPivot, #removePivot, #findPivotCollidingWith
	 */
	public boolean containsPivot( Sprite pivot ) {
		if( contents.get( pivot ) != null ) return true; else return false;
	}


	/**
	 * Checks if graph contains given line.
	 * @return True if line is in the graph, otherwise False.
	 * @see #addLine, #removeLine, #findLineCollidingWith, #findLine
	 */
	public boolean containsLineSegment( LineSegment line ) {
		LinkedList<LineSegment> list = contents.get( line.pivot[ 0 ] );
		if( list == null ) return false;
		if( list.contains( line ) ) return true; else return false;
	}


	/**
	 * Finds a line in the graph for given pivots.
	 * @see #addLine, #removeLine, #findLineCollidingWith, #containsLine
	 */
	public LineSegment findLineSegment( Sprite pivot1, Sprite pivot2 ) {
		LinkedList<LineSegment> list = contents.get( pivot1 );
		if( list == null ) return null;
		for( LineSegment lineSegment : list ) {
			if( lineSegment.pivot[ 0 ] == pivot1 && lineSegment.pivot[ 1 ] == pivot2 ) return lineSegment;
			if( lineSegment.pivot[ 0 ] == pivot2 && lineSegment.pivot[ 1 ] == pivot1 ) return lineSegment;
		}
		return null;
	}

	// ==================== Other ====================

	private static double maxLength;
	private HashMap<Sprite, Double> lengthMap;
	private LinkedList<Sprite> shortestPath;


	/**
	 * Finds a path  between 2 given pivots of the graph.
	 * @return List of pivots forming path between 2 give pivots if it's possible, otherwise null.
	 * @see #lTGraph example
	 */
	public LinkedList findPath( Sprite fromPivot, Sprite toPivot ) {
		shortestPath = null;
		maxLength = 999999;
		lengthMap = new HashMap();
		LinkedList path = new LinkedList();
		path.addLast( fromPivot );
		spread( path, fromPivot, toPivot, 0 );
		return shortestPath;
	}



	public void spread( LinkedList<Sprite> path, Sprite fromPivot, Sprite toPivot, double length ) {
		for( LineSegment line: contents.get( fromPivot ) ) {
			Sprite otherPivot = ( line.pivot[ 0 ] == fromPivot ? line.pivot[ 1 ] : line.pivot[ 0 ] ) ;
			double newLength = length + fromPivot.distanceTo( otherPivot );
			if( newLength + otherPivot.distanceTo( toPivot ) > maxLength ) continue;
			while( true ) {
				if( lengthMap.containsKey( otherPivot ) ) {
					if( lengthMap.get( otherPivot ) < newLength ) break;
				}
				LinkedList<Sprite> newPath = new LinkedList<Sprite>();
				for( Sprite pivot : path ) newPath.add( pivot );
				newPath.addLast( otherPivot );
				lengthMap.put( otherPivot, newLength );
				if( otherPivot == toPivot ) {
					shortestPath = newPath;
					maxLength = newLength;
				} else {
					spread( newPath, otherPivot, toPivot, newLength );
				}
				break;
			}
		}
	}


	@Override
	public void xMLIO( XMLObject xMLObject ) {
		/*
		super.xMLIO( xMLObject );
		HashMap map = null;
		if( XMLObject.xMLMode == XMLMode.GET ) {
			xMLObject.manageObjectSetField( "pivots", map );
			for( Sprite piv: map.keySet() ) {
				addPivot( piv );
			}

			xMLObject.manageObjectSetField( "lines", map );
			for( LineSegment lineSegment: map.keySet() ) {
				addLineSegment( lineSegment );
			}
		} else {
			contents = xMLObject.manageObjectSetField( "pivots", contents );
			xMLObject.manageObjectSetField( "lines", lineSegments );
		}
		*/
	}
}