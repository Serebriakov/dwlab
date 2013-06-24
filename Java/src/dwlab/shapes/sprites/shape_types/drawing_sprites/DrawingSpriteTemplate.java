/* Digital Wizard's Lab - game development framework
 * Copyright (C) 2013, Matt Merkulov 

 * All rights reserved. Use of this code is allowed under the
 * Artistic License 2.0 terms, as specified in the license.txt
 * file distributed with this code, or available from
 * http://www.opensource.org/licenses/artistic-license-2.0.php
 */

package dwlab.shapes.sprites.shape_types.drawing_sprites;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;
import dwlab.visualizers.Color;
import dwlab.visualizers.Visualizer;

public class DrawingSpriteTemplate extends DrawingSprite {
	public static DrawingSpriteTemplate instance = new DrawingSpriteTemplate();
	
	
	@Override
	public void perform( Visualizer visualizer, Sprite sprite, Sprite spriteShape, Color drawingColor ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			handlers[ templateSprite.shapeType.getNum() ].perform( visualizer, templateSprite, serviceSprite, drawingColor );
		}
	}
}
