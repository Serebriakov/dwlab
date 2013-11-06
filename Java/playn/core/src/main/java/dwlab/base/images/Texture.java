/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.base.images;

import dwlab.base.Obj;
import dwlab.visualizers.Color;
import java.util.LinkedList;

public abstract class Texture extends Obj {
	public static LinkedList<Texture> textures = new LinkedList<Texture>();
	
	public String fileName;
	protected int width, height;
	
	/**
	 * Returns width of image.
	 * @return Width of image in pixels.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns height of image.
	 * @return Height of image in pixels.
	 */
	public int getHeight() {
		return height;
	}
	
	
	public abstract void draw( double x1, double y1, double x2, double y2, double tx1, double ty1, double tx2, double ty2, double angle, Color color );
	
	
	public abstract ImageBuffer getBuffer();

	public abstract void applyBuffer( ImageBuffer buffer );

	public abstract void pasteBuffer( ImageBuffer buffer, int x, int y );
}
