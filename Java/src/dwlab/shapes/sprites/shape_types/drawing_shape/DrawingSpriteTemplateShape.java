package dwlab.shapes.sprites.shape_types.drawing_shape;

import dwlab.base.Vector;
import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;
import dwlab.visualizers.Color;

public class DrawingSpriteTemplateShape extends DrawingShape {
	@Override
	public void perform( Sprite sprite, Color drawingColor, boolean empty ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			handlers[ templateSprite.shapeType.getNum() ].perform( serviceSprite, drawingColor, empty );
		}
	}
}
