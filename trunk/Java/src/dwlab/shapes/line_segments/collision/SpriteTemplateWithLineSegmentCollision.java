/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.line_segments.collision;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class SpriteTemplateWithLineSegmentCollision extends CollisionWithLineSegment {
	public static SpriteTemplateWithLineSegmentCollision instance = new SpriteTemplateWithLineSegmentCollision();

	
	@Override
	public boolean check( Sprite sprite, Sprite lSPivot1, Sprite lSPivot2 ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			if( CollisionWithLineSegment.handlers[ sprite.shapeType.getNum() ].check( sprite, lSPivot1, lSPivot2 ) ) return true;
		}
		return false;
	}
}	