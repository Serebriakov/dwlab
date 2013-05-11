package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;

public class DrawingSpriteTemplateShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Sprite spriteShape, Vector coords, Vector sizes ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			handlers[ templateSprite.shapeType.getNum() ].perform( templateSprite, serviceSprite, coords, sizes );
		}
	}
}
