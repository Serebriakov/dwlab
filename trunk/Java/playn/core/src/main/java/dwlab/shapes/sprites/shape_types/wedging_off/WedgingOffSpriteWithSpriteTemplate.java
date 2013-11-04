/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.service.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class WedgingOffSpriteWithSpriteTemplate extends WedgingOffSprites {
	public static WedgingOffSpriteWithSpriteTemplate instance = new WedgingOffSpriteWithSpriteTemplate();

	@Override
	public void calculateVector( Sprite spriteTemplateSprite, Sprite sprite, Vector vector ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) spriteTemplateSprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( spriteTemplateSprite, templateSprite, serviceSprite );
			serviceSprite.shapeType = templateSprite.shapeType;
			handlers[ serviceSprite.shapeType.getNum() ][ sprite.shapeType.getNum() ].calculateVector( serviceSprite, sprite, vector );
		}
	}
}
