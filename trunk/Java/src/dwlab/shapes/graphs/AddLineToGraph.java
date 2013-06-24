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

public class AddLineToGraph extends Action {
	public Graph graph;
	public LineSegment lineSegment;


	public static AddLineToGraph create( Graph graph, LineSegment lineSegment ) {
		AddLineToGraph action = new AddLineToGraph();
		action.graph = graph;
		action.lineSegment = lineSegment;
		return action;
	}


	@Override
	public void perform() {
		graph.addLineSegment( lineSegment );
		super.perform();
	}


	@Override
	public void undo() {
		graph.removeLineSegment( lineSegment );
		super.undo();
	}
}