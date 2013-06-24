/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php */

package dwlab.visualizers;

import dwlab.base.Graphics;
import dwlab.base.images.Image;
import dwlab.base.service.IntVector;
import dwlab.shapes.Shape;
import dwlab.shapes.Shape.Facing;
import dwlab.shapes.sprites.Sprite;

public class WindowedVisualizer extends Visualizer {
	public Shape[] viewports;
	public Visualizer visualizer;


	@Override
	public Image getImage() {
		return visualizer.getImage();
	}


	@Override
	public void setImage( Image newImage ) {
		visualizer.setImage( newImage );
	}


	private IntVector servicePivot = new IntVector();
	private IntVector serviceSizes = new IntVector();
	
	@Override
	public void drawUsingSprite( Sprite sprite, Sprite spriteShape, Color color ) {
		if( ! sprite.visible ) return;

		Graphics.getViewport( servicePivot, serviceSizes );

		for( Shape viewport : viewports ) {
			viewport.setAsViewport();
			visualizer.drawUsingSprite( sprite, spriteShape, color );
		}

		Graphics.setViewport( servicePivot.x, servicePivot.y, serviceSizes.x, serviceSizes.y );
	}


	@Override
	public Facing getFacing() {
		return visualizer.getFacing();
	}


	@Override
	public void setFacing( Facing newFacing ) {
		visualizer.setFacing( newFacing );
	}
}
