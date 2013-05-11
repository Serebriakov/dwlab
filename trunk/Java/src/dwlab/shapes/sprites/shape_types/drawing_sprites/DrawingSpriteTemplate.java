package dwlab.shapes.sprites.shape_types.drawing_sprites;

import dwlab.shapes.sprites.Sprite;
import dwlab.shapes.sprites.shape_types.SpriteTemplate;
import dwlab.visualizers.Visualizer;

public class DrawingSpriteTemplate extends DrawingSprite {
	@Override
	public void perform( Visualizer visualizer, Sprite sprite, Sprite spriteShape, double drawingAlpha ) {
		SpriteTemplate spriteTemplate = (SpriteTemplate) sprite.shapeType;
		for( Sprite templateSprite : spriteTemplate.sprites ) {
			spriteTemplate.setShape( sprite, templateSprite, serviceSprite );
			handlers[ templateSprite.shapeType.getNum() ].perform( visualizer, templateSprite, serviceSprite, drawingAlpha );
		}
	}
}
