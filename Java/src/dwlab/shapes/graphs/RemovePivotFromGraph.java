/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.graphs;

import dwlab.base.service.Action;
import dwlab.shapes.line_segments.LineSegment;
import dwlab.shapes.sprites.Sprite;
import java.util.LinkedList;

public class RemovePivotFromGraph extends Action {
	public Graph graph;
	public Sprite pivot;
	public LinkedList<LineSegment> lineSegments;


	public static RemovePivotFromGraph create( Graph graph, Sprite pivot ) {
		if( ! graph.containsPivot( pivot ) ) error( "Cannot find pivot in the graph" );
		RemovePivotFromGraph action = new RemovePivotFromGraph();
		action.graph = graph;
		action.pivot = pivot;
		return action;
	}


	@Override
	public void perform() {
		lineSegments = graph.contents.get( pivot );
		graph.removePivot( pivot );
		super.perform();
	}


	@Override
	public void undo() {
		graph.addPivot( pivot );
		for( LineSegment lineSegment: lineSegments ) {
			graph.addLineSegment( lineSegment );
		}
		super.undo();
	}
}