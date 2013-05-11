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
