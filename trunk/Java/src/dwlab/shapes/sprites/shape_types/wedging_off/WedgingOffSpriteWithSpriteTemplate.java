package dwlab.shapes.sprites.shape_types.wedging_off;

import dwlab.base.Vector;
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
