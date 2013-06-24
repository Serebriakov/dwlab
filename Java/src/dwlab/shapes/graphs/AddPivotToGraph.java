/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.graphs;

import dwlab.base.service.Action;
import dwlab.shapes.sprites.Sprite;

public class AddPivotToGraph extends Action {
	public Graph graph;
	public Sprite pivot;


	public static AddPivotToGraph create( Graph graph, Sprite pivot ) {
		AddPivotToGraph action = new AddPivotToGraph();
		action.graph = graph;
		action.pivot = pivot;
		return action;
	}


	@Override
	public void perform() {
		graph.addPivot( pivot );
		super.perform();
	}


	@Override
	public void undo() {
		graph.removePivot( pivot );
		super.undo();
	}
}