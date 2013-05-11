/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2012, Matt Merkulov
 *
 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites;

import dwlab.base.Obj;
import dwlab.shapes.line_segments.LineSegment;

/**
 * Sprite and line segment collision handling class.
 * Collision check method with specified collision handler will execute this handler's method on collision sprite with line.

 * @see #active example
 */
public class SpriteAndLineSegmentCollisionHandler<E extends Sprite> extends Obj {
	public void handleCollision( E sprite, LineSegment lineSegment ) {
	}
}
