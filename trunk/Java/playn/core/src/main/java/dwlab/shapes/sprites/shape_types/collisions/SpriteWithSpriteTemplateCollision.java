/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.collisions;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class SpriteWithSpriteTemplateCollision extends SpritesCollision {
	public static SpriteWithSpriteTemplateCollision instance = new SpriteWithSpriteTemplateCollision();

	@Override
	public boolean check( Sprite sprite1, Sprite sprite2 ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite1.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite1, templateSprite, serviceSprite );
			serviceSprite.shapeType = templateSprite.shapeType;
			if( SpritesCollision.handlers[ serviceSprite.shapeType.getNum() ][ sprite2.shapeType.getNum() ].check( serviceSprite, sprite2 ) ) return true;
		}
		return false;
	}
}
